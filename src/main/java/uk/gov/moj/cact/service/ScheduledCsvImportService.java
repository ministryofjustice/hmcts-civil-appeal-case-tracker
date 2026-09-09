package uk.gov.moj.cact.service;

import io.micrometer.core.annotation.Counted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uk.gov.moj.cact.exception.CsvImportException;
import uk.gov.moj.cact.repository.CaseRecordRepository;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Random;

@Service
public class ScheduledCsvImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledCsvImportService.class);

    private static final DateTimeFormatter LAST_UPDATED_FORMAT =
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("dd-MMM-yyyy")
                    .toFormatter(Locale.ENGLISH);

    private final CsvImportService csvImportService;
    private final CaseRecordRepository repository;
    private final S3BucketClient s3BucketClient;
    private final int maxJitterSeconds;
    private final Clock clock;
    private final Random random = new Random();

    public ScheduledCsvImportService(CsvImportService csvImportService,
                                     CaseRecordRepository repository,
                                     S3BucketClient s3BucketClient,
                                     @Value("${app.csv-import.max-jitter-seconds:3600}") int maxJitterSeconds,
                                     Clock clock
    ) {
        this.csvImportService = csvImportService;
        this.repository = repository;
        this.s3BucketClient = s3BucketClient;
        this.maxJitterSeconds = maxJitterSeconds;
        this.clock = clock;
    }

    /**
     * Scheduled entry point for the nightly refresh.
     */
    @Scheduled(cron = "${app.csv-import.cron}")
    @Counted(value = "scheduled_task_exceptions", recordFailuresOnly = true)
    public void run() {
        // Random wait so replicas don't import simultaneously and duplicate data
        int waitSeconds = maxJitterSeconds > 0 ? random.nextInt(maxJitterSeconds) : 0;
        LOGGER.info("Cron job running! Waiting {} seconds before import", waitSeconds);
        try {
            Thread.sleep(waitSeconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.warn("Scheduled CSV import interrupted before it started");
            return;
        }
        downloadCsvAndReplaceDatabase();
        LOGGER.info("Scheduler Finished");
    }

    /**
     * Downloads the published CSV and replaces the table contents with it.
     *
     * @throws CsvImportException if the download or import fails
     */
    public void downloadCsvAndReplaceDatabase() {
        try {
            LOGGER.info("Checking if database was updated yesterday");
            if (isLastUpdatedYesterday()) {
                LOGGER.info("Database already contains the latest CSV update. Skipping CSV import.");
                return;
            }
            LOGGER.info("Database does not contain the latest CSV update. Proceeding with CSV import.");

            try (InputStream inputStream = s3BucketClient.downloadCsv();
                 BufferedReader reader = new BufferedReader(
                         new InputStreamReader(
                                 inputStream,
                                 StandardCharsets.UTF_8))) {

                int rowCount = csvImportService.replaceDatabase(reader);
                LOGGER.info("Success: {} rows added in database", rowCount);
            }
        } catch (Exception ex) {
            throw new CsvImportException("CSV import failed", ex);
        }
    }

    boolean isLastUpdatedYesterday() {
        String lastUpdated = repository.findMaxLastUpdated().orElse(null);

        if (lastUpdated == null || lastUpdated.isBlank()) {
            LOGGER.info("No last updated date found in database");
            return false;
        }

        LOGGER.info("Last updated date from database: {}", lastUpdated);

        try {
            LocalDate lastUpdatedDate = LocalDate.parse(lastUpdated, LAST_UPDATED_FORMAT);
            LocalDate yesterday = LocalDate.now(clock).minusDays(1);
            return lastUpdatedDate.equals(yesterday);

        } catch (DateTimeParseException e) {
            LOGGER.error("Error parsing last updated date: {}. Update will proceed", lastUpdated, e);
            return false;
        }
    }
}
