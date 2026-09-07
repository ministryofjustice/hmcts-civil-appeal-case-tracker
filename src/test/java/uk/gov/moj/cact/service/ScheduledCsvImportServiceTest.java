package uk.gov.moj.cact.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
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

    // Pinned for deterministic testing and to cover the September "Sep" CSV regression.
    private static final LocalDate TEST_DATE = LocalDate.of(2026, 9, 7);
    private static final DateTimeFormatter CSV_DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);

    @Mock
    private CaseRecordRepository repository;

    @Mock
    private CsvImportService csvImportService;

    @Mock
    private S3BucketClient s3BucketClient;

    private final Clock clock = fixedClockOn(TEST_DATE);

    private ScheduledCsvImportService scheduledCsvImportService;

    @BeforeEach
    void setUp() {
        // jitter 0 so run() doesn't sleep during tests
        scheduledCsvImportService = new ScheduledCsvImportService(
                csvImportService, repository, s3BucketClient, 0, clock);
    }

    @Test
    void shouldReturnTrueWhenLastUpdatedWasYesterday() {
        LocalDate yesterday = TEST_DATE.minusDays(1);

        when(repository.findMaxLastUpdated()).thenReturn(Optional.of(yesterday.format(CSV_DATE_FORMAT)));
        assertTrue(scheduledCsvImportService.isLastUpdatedYesterday());
    }

    @Test
    void shouldReturnFalseWhenLastUpdatedWasToday() {
        when(repository.findMaxLastUpdated()).thenReturn(Optional.of(TEST_DATE.format(CSV_DATE_FORMAT)));

        assertFalse(scheduledCsvImportService.isLastUpdatedYesterday());
    }

    @Test
    void shouldReturnFalseWhenNoLastUpdatedDateExists() {
        when(repository.findMaxLastUpdated()).thenReturn(Optional.empty());
        assertFalse(scheduledCsvImportService.isLastUpdatedYesterday());
    }

    @Test
    void shouldReturnFalseWhenLastUpdatedDateIsInvalid() {
        when(repository.findMaxLastUpdated()).thenReturn(Optional.of("invalid-date"));

        assertFalse(scheduledCsvImportService.isLastUpdatedYesterday());
    }

    @Test
    void shouldSkipImportWhenDatabaseWasUpdatedYesterday() {
        LocalDate yesterday = TEST_DATE.minusDays(1);

        when(repository.findMaxLastUpdated()).thenReturn(Optional.of(yesterday.format(CSV_DATE_FORMAT)));
        scheduledCsvImportService.downloadCsvAndReplaceDatabase();

        verify(s3BucketClient, never()).downloadCsv();
        verify(csvImportService, never()).replaceDatabase(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldDownloadCsvAndReplaceDatabase() {
        when(repository.findMaxLastUpdated()).thenReturn(Optional.of(TEST_DATE.format(CSV_DATE_FORMAT)));

        String csvContent = """
                id,name
                1,John
                2,Jane
                """;
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        when(s3BucketClient.downloadCsv()).thenReturn(inputStream);
        when(csvImportService.replaceDatabase(org.mockito.ArgumentMatchers.any())).thenReturn(2);

        scheduledCsvImportService.downloadCsvAndReplaceDatabase();

        verify(s3BucketClient).downloadCsv();
        verify(csvImportService).replaceDatabase(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldNotImportWhenS3DownloadFails() {
        when(repository.findMaxLastUpdated()).thenReturn(Optional.of(TEST_DATE.format(CSV_DATE_FORMAT)));

        when(s3BucketClient.downloadCsv()).thenThrow(new RuntimeException("S3 download failed"));

        assertThrows(CsvImportException.class,
                () -> scheduledCsvImportService.downloadCsvAndReplaceDatabase());
        verify(s3BucketClient).downloadCsv();
        verify(csvImportService, never()).replaceDatabase(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldLetImportFailureThrowSoItCanBeCounted() {
        when(repository.findMaxLastUpdated()).thenReturn(Optional.of(TEST_DATE.format(CSV_DATE_FORMAT)));
        when(s3BucketClient.downloadCsv()).thenThrow(new IllegalStateException("S3 unavailable"));

        assertThrows(CsvImportException.class, () -> scheduledCsvImportService.run());
    }

    @Test
    void shouldNotThrowFromRunWhenImportSucceeds() {
        when(repository.findMaxLastUpdated()).thenReturn(Optional.of(TEST_DATE.format(CSV_DATE_FORMAT)));
        when(s3BucketClient.downloadCsv()).thenReturn(
                new ByteArrayInputStream("a,b\n1,2\n".getBytes(StandardCharsets.UTF_8)));
        when(csvImportService.replaceDatabase(org.mockito.ArgumentMatchers.any())).thenReturn(2);

        scheduledCsvImportService.run();

        verify(csvImportService).replaceDatabase(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldParseTheSeptemberAbbreviationUsedByTheCsv() {
        when(repository.findMaxLastUpdated()).thenReturn(Optional.of("06-Sep-2026"));

        assertTrue(scheduledCsvImportService.isLastUpdatedYesterday());
    }

    @Test
    void shouldReturnFalseWhenLastUpdatedDateIsBlank() {
        when(repository.findMaxLastUpdated()).thenReturn(Optional.of(" "));

        assertFalse(scheduledCsvImportService.isLastUpdatedYesterday());
    }

    @Test
    void shouldThrowWhenCsvImportFails() {
        when(repository.findMaxLastUpdated()).thenReturn(Optional.of(TEST_DATE.format(CSV_DATE_FORMAT)));

        when(s3BucketClient.downloadCsv()).thenReturn(new ByteArrayInputStream("a,b\n1,2\n".getBytes(StandardCharsets.UTF_8)));

        when(csvImportService.replaceDatabase(org.mockito.ArgumentMatchers.any())).thenThrow(new RuntimeException("Import failed"));

        assertThrows(CsvImportException.class,
                () -> scheduledCsvImportService.downloadCsvAndReplaceDatabase());
    }

    private static Clock fixedClockOn(LocalDate today) {
        return Clock.fixed(today.atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneOffset.UTC);
    }
}
