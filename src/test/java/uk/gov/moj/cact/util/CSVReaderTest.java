package uk.gov.moj.cact.util;

import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CSVReaderTest {

    @Test
    void testParseLine() throws Exception {
        String testInput = "column1,column2,column3\nvalue1,value2,value3";
        CSVReader reader = new CSVReader(new StringReader(testInput));

        String[] parsedLine = reader.readNext();
        assertNotNull(parsedLine);
        assertEquals(3, parsedLine.length);
        assertEquals("column1", parsedLine[0]);
        assertEquals("column2", parsedLine[1]);
        assertEquals("column3", parsedLine[2]);

        parsedLine = reader.readNext();
        assertNotNull(parsedLine);
        assertEquals(3, parsedLine.length);
        assertEquals("value1", parsedLine[0]);
        assertEquals("value2", parsedLine[1]);
        assertEquals("value3", parsedLine[2]);
    }

    @Test
    void testParseLineWithEscapedQuote() throws Exception {
        // Simulates a CSV line with an escaped quote in the first field:
        // "\"test string","second string"
        String input = "\"\\\"test string\",\"second string\"";
        CSVReader reader = new CSVReader(new StringReader(input));

        String[] parsedLine = reader.readNext();
        assertNotNull(parsedLine);
        assertEquals(2, parsedLine.length);
        assertEquals("\"test string", parsedLine[0]);
        assertEquals("second string", parsedLine[1]);
    }
}
