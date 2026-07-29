package uk.gov.moj.cact.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.cact.exception.CsvImportException;
import uk.gov.moj.cact.repository.CaseRecordRepository;

@ExtendWith(MockitoExtension.class)
public class ScheduledCsvImportServiceTest {

    @Mock
    private CaseRecordRepository repository;

    @Mock
    private CsvImportService csvImportService;

    @Mock
    private S3BucketClient s3BucketClient;

    private ScheduledCsvImportService scheduledCsvImportService;

    @BeforeEach
    void setUp() {
        // jitter 0 so run() doesn't sleep during tests
        scheduledCsvImportService = new ScheduledCsvImportService(
                csvImportService, repository, s3BucketClient, 0);
    }

    @Test
    void shouldReturnTrueWhenLastUpdatedWasYesterday() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.UK);
        Date today = new Date();
        Date yesterday = new Date(today.getTime() - 24 * 60 * 60 * 1000);

        when(repository.findMaxLastUpdated()).thenReturn(Optional.of(sdf.format(yesterday)));
        assertTrue(scheduledCsvImportService.isLastUpdatedYesterday());
    }

    @Test
    void shouldReturnFalseWhenLastUpdatedWasToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.UK);
        Date today = new Date();
        Date yesterday = new Date(today.getTime() - 24 * 60 * 60 * 1000);


        when(repository.findMaxLastUpdated()).thenReturn(Optional.of(sdf.format(today)));
        assertFalse(scheduledCsvImportService.isLastUpdatedYesterday());
    }

    @Test
    void testIsLastUpdatedYesterday() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.UK);
        Date today = new Date();
        Date yesterday = new Date(today.getTime() - 24 * 60 * 60 * 1000);

        when(repository.findMaxLastUpdated()).thenReturn(Optional.of(sdf.format(yesterday)));
        assertTrue(scheduledCsvImportService.isLastUpdatedYesterday());

        when(repository.findMaxLastUpdated()).thenReturn(Optional.of(sdf.format(today)));
        assertFalse(scheduledCsvImportService.isLastUpdatedYesterday());

        when(repository.findMaxLastUpdated()).thenReturn(Optional.empty());
        assertFalse(scheduledCsvImportService.isLastUpdatedYesterday());
    }

    @Test
    void shouldReturnFalseWhenNoLastUpdatedDateExists() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.UK);
        Date today = new Date();
        Date yesterday = new Date(today.getTime() - 24 * 60 * 60 * 1000);

        when(repository.findMaxLastUpdated()).thenReturn(Optional.empty());
        assertFalse(scheduledCsvImportService.isLastUpdatedYesterday());
    }

    @Test
    void shouldReturnFalseWhenLastUpdatedDateIsInvalid() {
        when(repository.findMaxLastUpdated()).thenReturn(Optional.of("invalid-date"));

        boolean result = scheduledCsvImportService.isLastUpdatedYesterday();
        assertFalse(result);
    }

    @Test
    void shouldSkipImportWhenDatabaseWasUpdatedYesterday() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.UK);
        Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L);

        when(repository.findMaxLastUpdated()).thenReturn(Optional.of(sdf.format(yesterday)));
        scheduledCsvImportService.downloadCsvAndReplaceDatabase();

        verify(s3BucketClient, never()).downloadCsv();
        verify(csvImportService, never()).replaceDatabase(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldDownloadCsvAndReplaceDatabase() throws Exception {
        when(repository.findMaxLastUpdated()).thenReturn(Optional.of(
                new SimpleDateFormat("dd-MMM-yyyy", Locale.UK)
                        .format(new Date())));

        String csvContent = "id,name\n" + "1,John\n" + "2,Jane\n";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        when(s3BucketClient.downloadCsv()).thenReturn(inputStream);
        when(csvImportService.replaceDatabase(org.mockito.ArgumentMatchers.any())).thenReturn(2);

        scheduledCsvImportService.downloadCsvAndReplaceDatabase();

        verify(s3BucketClient).downloadCsv();
        verify(csvImportService).replaceDatabase(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldNotImportWhenS3DownloadFails() {
        when(repository.findMaxLastUpdated()).thenReturn(Optional.of(
                new SimpleDateFormat("dd-MMM-yyyy", Locale.UK)
                        .format(new Date())));

        when(s3BucketClient.downloadCsv()).thenThrow(new RuntimeException("S3 download failed"));

        assertThrows(CsvImportException.class,
                () -> scheduledCsvImportService.downloadCsvAndReplaceDatabase());
        verify(s3BucketClient).downloadCsv();
        verify(csvImportService, never()).replaceDatabase(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldLetImportFailureThrowSoItCanBeCounted() {
        when(repository.findMaxLastUpdated()).thenReturn(Optional.of(
                new SimpleDateFormat("dd-MMM-yyyy", Locale.UK).format(new Date())));
        when(s3BucketClient.downloadCsv()).thenThrow(new IllegalStateException("S3 unavailable"));

        assertThrows(CsvImportException.class, () -> scheduledCsvImportService.run());
    }

    @Test
    void shouldNotThrowFromRunWhenImportSucceeds() {
        when(repository.findMaxLastUpdated()).thenReturn(Optional.of(
                new SimpleDateFormat("dd-MMM-yyyy", Locale.UK).format(new Date())));
        when(s3BucketClient.downloadCsv()).thenReturn(
                new ByteArrayInputStream("a,b\n1,2\n".getBytes(StandardCharsets.UTF_8)));
        when(csvImportService.replaceDatabase(org.mockito.ArgumentMatchers.any())).thenReturn(2);

        scheduledCsvImportService.run();

        verify(csvImportService).replaceDatabase(org.mockito.ArgumentMatchers.any());
    }
}
