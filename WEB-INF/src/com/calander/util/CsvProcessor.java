package com.calander.util;

import com.calander.beans.Calander;
import org.hibernate.Session;
import au.com.bytecode.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.MessageFormat;

public class CsvProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvProcessor.class);

    public static int processCSV(CSVReader reader, Session session) throws IOException {
        String[] nextLine;
        Calander calander;
        int rowCount = 0;

        while ((nextLine = reader.readNext()) != null) {
            LOGGER.info(MessageFormat.format("{0}: <{0}>", rowCount, nextLine));
            calander = new Calander();
            calander.setProperties(nextLine);
            session.save(calander);
            LOGGER.info(MessageFormat.format("Saved row {0}", rowCount));
            rowCount++;
        }

        return rowCount;
    }
}