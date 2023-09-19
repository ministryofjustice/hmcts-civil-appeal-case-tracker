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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Random;


public class CsvImportJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvImportJob.class);

    public static void main(String args[]) throws Exception {
        downloadCsvFromS3BucketAndReplaceDatabaseWithContents();
    }


    public void execute(JobExecutionContext arg0)
        throws JobExecutionException {

        //Generates random wait time so that the tasks dont upload simultaneously causing a duplication of data
        int waitTime = randomWaitTimeInMillis();
        System.out.println(MessageFormat.format("Wait {0} milliseconds, {1} seconds", waitTime, (waitTime / 1000)));

        try {
            Thread.sleep(waitTime);
        }
        catch (InterruptedException e) { //Thread interrupted by another thread
            e.printStackTrace();
        }

        downloadCsvFromS3BucketAndReplaceDatabaseWithContents();
    }

    public static int randomWaitTimeInMillis() {
        Random rand = new Random();
        int upperBound = 3600; //Random wait time in seconds 3600 = 1 Hour
        return rand.nextInt(upperBound * 1000); //convert seconds to milliseconds -> finer granularity
    }


    public static void downloadCsvFromS3BucketAndReplaceDatabaseWithContents() {

        System.out.println("Cron job running!");
        int rowCount = 0;
        HibernatePlugin hibernatePlugin = new HibernatePlugin();
        SessionFactory factory = null;
        try {
            factory = (SessionFactory) hibernatePlugin.getconnection();
        } catch (Exception e) {
            throw new RuntimeException("Hibernate Plugin unable to get connection");
        }
        Session session = factory.openSession();

        try {

            String result = null;

            String url = "https://cloud-platform-ab00007072890fd153cef39e574f738e.s3.eu-west-2.amazonaws.com/data.csv";

            URL u;
            InputStream is = null;


            u = new URL(url);
            is = u.openStream();
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(is));

            CSVReader reader = new CSVReader(reader1);
            System.out.println("coming in run schedular>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            String[] nextLine;
            Calander calander = null;


            session.beginTransaction();
            session.createQuery("delete Calander").executeUpdate();
//            session.getTransaction().commit();
//            session.beginTransaction();
            rowCount = populateRows(rowCount, session, reader);
            session.getTransaction().commit();
            LOGGER.info("Success {} rows added in database", rowCount);
        } catch (Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
            LOGGER.error("Import Failed: {}", ex.getMessage());
            session.flush();
            session.clear();
            session.close();
            //TODO send alert to slack or have a healthcheck page that shows last time db was updated
        } finally {

            session.flush();
            session.clear();
            session.close();
        }
        System.out.println("Scheduler Finished");

    }




    private static int populateRows(int rowCount, Session session, CSVReader reader) throws IOException {
        String[] nextLine;
        Calander calander;
        while ((nextLine = reader.readNext()) != null) {
            LOGGER.info("Processing Row {}", rowCount);

            int line_length = nextLine.length;
            int column = 0;

            calander = new Calander();

            if (column < line_length) {
                calander.setSearch_date(nextLine[0]);
            }
            column++;

            if (column < line_length) {
                calander.setCase_no(nextLine[1]);
            }
            column++;

            if (column < line_length) {
                calander.setHeading_status(nextLine[2]);
            }
            column++;

            if (column < line_length) {
                calander.setJudge1(nextLine[3]);
            }
            column++;

            if (column < line_length) {
                calander.setJudge2(nextLine[4]);
            }
            column++;

            if (column < line_length) {
                calander.setJudge3(nextLine[5]);
            }
            column++;

            if (column < line_length) {
                calander.setLcourt(nextLine[6]);
            }
            column++;

            if (column < line_length) {
                calander.setVenue(nextLine[7]);
            }
            column++;

            if (column < line_length) {
                calander.setCase_ref(nextLine[8]);
            }
            column++;

            if (column < line_length) {
                calander.setTitle1(nextLine[9] + " " + nextLine[10]);
            }
            column++;

            if (column < line_length) {
                calander.setTitle2(nextLine[10]);
            }
            column++;

            if (column < line_length) {
                calander.setType(nextLine[11]);
            }
            column++;

            if (column < line_length) {
                calander.setLc_judge(nextLine[12]);
            }
            column++;

            if (column < line_length) {
                calander.setNature(nextLine[13]);
            }
            column++;

            if (column < line_length) {
                calander.setLast_updated(nextLine[14]);
            }
            column++;

            if (column < line_length) {
                calander.setResult(nextLine[15]);
            }
            column++;

            if (column < line_length) {
                calander.setStatus(nextLine[16]);
            }
            column++;

            if (column < line_length) {
                calander.setTrack_line1(nextLine[17]);
            }
            column++;

            if (column < line_length) {
                calander.setTrack_line2(nextLine[18]);
            }
            column++;

            if (column < line_length) {
                calander.setTrack_line3(nextLine[19]);
            }
            column++;

            if (column < line_length) {
                calander.setTrack_line4(nextLine[20]);
            }
            column++;

            if (column < line_length) {
                calander.setTrack_line5(nextLine[21]);
            }
            column++;

            if (column < line_length) {
                calander.setTrack_line6(nextLine[22]);
            }
            column++;

            if (column < line_length) {
                calander.setTrack_line7(nextLine[23]);
            }
            column++;

            if (column < line_length) {
                calander.setTrack_line8(nextLine[24]);
            }

            rowCount++;
            session.save(calander);

        }
        return rowCount;
    }


}
