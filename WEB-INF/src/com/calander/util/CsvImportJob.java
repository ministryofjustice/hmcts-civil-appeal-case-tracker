package com.calander.util;

import au.com.bytecode.opencsv.CSVReader;
import com.calander.beans.Calander;
import com.calander.plugin.HibernatePlugin;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.ParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Random;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.hibernate.Query;

public class CsvImportJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvImportJob.class);

    public static void main(String args[]) throws Exception {
        downloadCsvFromS3BucketAndReplaceDatabaseWithContents();
    }

    public void execute(JobExecutionContext arg0)
            throws JobExecutionException {

        // Generates random wait time so that the tasks dont upload simultaneously
        // causing a duplication of data
        int waitTime = randomWaitTimeInMillis();
        System.out.println(MessageFormat.format("Wait {0} milliseconds, {1} seconds", waitTime, (waitTime / 1000)));

        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) { // Thread interrupted by another thread
            e.printStackTrace();
        }

        downloadCsvFromS3BucketAndReplaceDatabaseWithContents();
    }

    public static int randomWaitTimeInMillis() {
        Random rand = new Random();
        int upperBound = 3600; // Random wait time in seconds (1 hour)
        return rand.nextInt(upperBound * 1000); // convert seconds to milliseconds
    }

    public static void downloadCsvFromS3BucketAndReplaceDatabaseWithContents() {
        System.out.println("Cron job running!");
        HibernatePlugin hibernatePlugin = new HibernatePlugin();
        SessionFactory factory = null;
        try {
            factory = (SessionFactory) hibernatePlugin.getconnection();
        } catch (Exception e) {
            throw new RuntimeException("Hibernate Plugin unable to get connection");
        }
        Session session = factory.openSession();

        try {
            LOGGER.info("Checking if database was updated today");
            if (isLastUpdatedYesterday(session)) {
                LOGGER.info("Database already updated today. Skipping CSV import.");
                return;
            }
            LOGGER.info("Database not updated today. Proceeding with CSV import.");

            String result = null;
            String url = "https://cloud-platform-ab00007072890fd153cef39e574f738e.s3.eu-west-2.amazonaws.com/data.csv";
            URL u = new URL(url);
            InputStream is = u.openStream();
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(is));
            CSVReader reader = new CSVReader(reader1);

            System.out.println("coming in run schedular>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            session.beginTransaction();
            session.createQuery("delete Calander").executeUpdate();

            int rowCount = CsvProcessor.processCSV(reader, session);
            session.getTransaction().commit();
            LOGGER.info("Success {} rows added in database", rowCount);
        } catch (Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
            LOGGER.error("Import Failed: {}", ex.getMessage());
            session.flush();
            session.clear();
            session.close();
            // TODO send alert to slack or have a healthcheck page that shows last time db
            // was updated
        } finally {
            session.flush();
            session.clear();
            session.close();
        }
        System.out.println("Scheduler Finished");
    }

    public static boolean isLastUpdatedYesterday(Session session) {
        Query query = session.createQuery("SELECT MAX(c.last_updated) FROM Calander c");
        String lastUpdated = (String) query.uniqueResult();
        LOGGER.info("Last updated date from database: {}", lastUpdated);

        if (lastUpdated == null) {
            LOGGER.info("No last updated date found in database");
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date today = new Date();
        Date yesterday = new Date(today.getTime() - 24 * 60 * 60 * 1000);
        String yesterdayStr = sdf.format(yesterday);
        LOGGER.info("Yesterday's date: {}", yesterdayStr);

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