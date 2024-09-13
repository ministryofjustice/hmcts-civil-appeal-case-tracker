package com.calander.util;

import com.calander.beans.Calander;
import org.hibernate.Session;
import au.com.bytecode.opencsv.CSVReader;

import java.io.IOException;

public class CsvProcessor {
    public static int processCSV(CSVReader reader, Session session) throws IOException {
        String[] nextLine;
        Calander calander;
        int rowCount = 0;

        while ((nextLine = reader.readNext()) != null) {
            calander = new Calander();
            calander.setProperties(nextLine);
            session.save(calander);
            rowCount++;
        }

        return rowCount;
    }
}