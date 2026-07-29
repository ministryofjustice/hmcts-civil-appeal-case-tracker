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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Random;

@Service
public class ScheduledCsvImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledCsvImportService.class);

    private final CsvImportService csvImportService;
    private final CaseRecordRepository repository;
    private final S3BucketClient s3BucketClient;
    private final int maxJitterSeconds;
    private final Random random = new Random();

    public ScheduledCsvImportService(CsvImportService csvImportService,
                                     CaseRecordRepository repository,
                                     S3BucketClient s3BucketClient,
                                     @Value("${app.csv-import.max-jitter-seconds:3600}") int maxJitterSeconds
    ) {
        this.csvImportService = csvImportService;
        this.repository = repository;
        this.s3BucketClient = s3BucketClient;
        this.maxJitterSeconds = maxJitterSeconds;
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
            LOGGER.info("Checking if database was updated today");
            if (isLastUpdatedYesterday()) {
                LOGGER.info("Database already updated today. Skipping CSV import.");
                return;
            }
            LOGGER.info("Database not updated today. Proceeding with CSV import.");

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
        LOGGER.info("Last updated date from database: {}", lastUpdated);

        if (lastUpdated == null) {
            LOGGER.info("No last updated date found in database");
            return false;
        }

        try {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.UK);

            LocalDate lastUpdatedDate =
                    LocalDate.parse(lastUpdated, formatter);

            LocalDate yesterday =
                    LocalDate.now().minusDays(1);

            boolean isUpdatedYesterday =
                    lastUpdatedDate.equals(yesterday);

            LOGGER.info("Is updated yesterday: {}", isUpdatedYesterday);

            return isUpdatedYesterday;

        } catch (DateTimeParseException e) {
            LOGGER.error(
                    "Error parsing last updated date: {}",
                    lastUpdated,
                    e
            );
            return false;
        }
    }
}
