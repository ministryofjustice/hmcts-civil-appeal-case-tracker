package uk.gov.moj.cact.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.cact.repository.CaseRecordRepository;

@ExtendWith(MockitoExtension.class)
public class ScheduledCsvImportServiceTest {

    @Mock
    private CaseRecordRepository repository;

    @Mock
    private CsvImportService csvImportService;

    @InjectMocks
    private ScheduledCsvImportService scheduledCsvImportService;

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
}
