package com.calander.util;

import com.calander.beans.Calander;
import org.hibernate.Session;
import au.com.bytecode.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;

public class CsvProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvProcessor.class);

    public static int processCSV(CSVReader reader, Session session) throws IOException {
        String[] nextLine;
        Calander calander;
        int rowCount = 0;

        while ((nextLine = reader.readNext()) != null) {
            LOGGER.info("Row " + rowCount + ": " + Arrays.toString(nextLine));
            calander = new Calander();
            calander.setProperties(nextLine);
            session.save(calander);
            session.flush();
            session.clear();
            LOGGER.info("Saved row " + rowCount);

            rowCount++;
        }

        return rowCount;
    }
}