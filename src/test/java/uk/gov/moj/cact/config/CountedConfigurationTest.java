package uk.gov.moj.cact.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import uk.gov.moj.cact.exception.CsvImportException;
import uk.gov.moj.cact.repository.CaseRecordRepository;
import uk.gov.moj.cact.service.CsvImportService;
import uk.gov.moj.cact.service.S3BucketClient;
import uk.gov.moj.cact.service.ScheduledCsvImportService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CountedConfigurationTest {

    @Mock
    private CaseRecordRepository repository;

    @Mock
    S3BucketClient s3BucketClient;

    @Mock
    private CsvImportService csvImportService;

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(AopAutoConfiguration.class))
            .withUserConfiguration(CountedConfiguration.class)
            .withBean(MeterRegistry.class, SimpleMeterRegistry::new);

    @Test
    void recordsCounterWhenScheduledTaskThrows() {
        contextRunner
                .withBean(ScheduledCsvImportService.class, this::failingImportService)
                .run(context -> {
                    ScheduledCsvImportService service = context.getBean(ScheduledCsvImportService.class);

                    assertThrows(CsvImportException.class, service::run);

                    double count = context.getBean(MeterRegistry.class)
                            .get("scheduled_task_exceptions")
                            .tag("exception", "CsvImportException")
                            .tag("method", "run")
                            .tag("result", "failure")
                            .counter()
                            .count();

                    assertEquals(1.0, count);
                });
    }

    @Test
    void doesNotRecordCounterWhenScheduledTaskSucceeds() {
        contextRunner
                .withBean(ScheduledCsvImportService.class, this::skippingImportService)
                .run(context -> {
                    context.getBean(ScheduledCsvImportService.class).run();

                    assertEquals(0,
                            context.getBean(MeterRegistry.class)
                                    .find("scheduled_task_exceptions").counters().size());
                });
    }

    private ScheduledCsvImportService failingImportService() {
        when(repository.findMaxLastUpdated()).thenReturn(Optional.of(today()));
        when(s3BucketClient.downloadCsv()).thenThrow(new IllegalStateException("S3 unavailable"));

        return new ScheduledCsvImportService(csvImportService, repository, s3BucketClient, 0);
    }

    private ScheduledCsvImportService skippingImportService() {
        when(repository.findMaxLastUpdated()).thenReturn(Optional.of(yesterday()));

        return new ScheduledCsvImportService(csvImportService, repository, mock(S3BucketClient.class), 0);
    }

    private static String today() {
        return format(new Date());
    }

    private static String yesterday() {
        return format(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L));
    }

    private static String format(Date date) {
        return new SimpleDateFormat("dd-MMM-yyyy", Locale.UK).format(date);
    }
}
