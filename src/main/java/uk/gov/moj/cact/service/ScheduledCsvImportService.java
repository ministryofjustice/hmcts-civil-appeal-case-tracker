package uk.gov.moj.cact.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uk.gov.moj.cact.repository.CaseRecordRepository;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

@Service
public class ScheduledCsvImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledCsvImportService.class);
    private static final int MAX_JITTER_SECONDS = 3600;


    private final CsvImportService csvImportService;
    private final CaseRecordRepository repository;
    private final S3BucketClient s3BucketClient;
    private final Random random = new Random();

    public ScheduledCsvImportService(CsvImportService csvImportService,
                                     CaseRecordRepository repository, S3BucketClient s3BucketClient
    ) {
        this.csvImportService = csvImportService;
        this.repository = repository;
        this.s3BucketClient = s3BucketClient;
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void run() {
        // Generates random wait time so that the tasks dont upload simultaneously
        // causing a duplication of data
        int waitSeconds = random.nextInt(MAX_JITTER_SECONDS);
        LOGGER.info("Cron job running! Waiting {} seconds before import", waitSeconds);
        try {
            Thread.sleep(waitSeconds * 1000L);
        } catch (InterruptedException e) {
            LOGGER.error("Cron job: thread interrupted: ", e);
            Thread.currentThread().interrupt();
            return;
        }
        downloadCsvAndReplaceDatabase();
    }

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

                LOGGER.info(
                        "Success: {} rows added in database",
                        rowCount
                );
            }
        } catch (Exception ex) {
            LOGGER.error("Exception occurred during CSV import: ", ex);
            LOGGER.error("Import Failed: {}", ex.getMessage());
        }
        LOGGER.info("Scheduler Finished");
    }

    boolean isLastUpdatedYesterday() {
        String lastUpdated = repository.findMaxLastUpdated().orElse(null);
        LOGGER.info("Last updated date from database: {}", lastUpdated);

        if (lastUpdated == null) {
            LOGGER.info("No last updated date found in database");
            return false;
        }

        // Legacy relied on the JVM default locale for the month names; pinning
        // Locale.UK so a different container locale can't break the parse.
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.UK);
        Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L);
        String yesterdayStr = sdf.format(yesterday);

        try {
            Date lastUpdatedDate = sdf.parse(lastUpdated);
            Date yesterdayDate = sdf.parse(yesterdayStr);
            boolean isUpdatedYesterday = lastUpdatedDate.equals(yesterdayDate);
            LOGGER.info("Is updated yesterday: {}", isUpdatedYesterday);
            return isUpdatedYesterday;
        } catch (ParseException e) {
            LOGGER.error("Error parsing date: {}", e.getMessage());
            return false;
        }
    }
}
