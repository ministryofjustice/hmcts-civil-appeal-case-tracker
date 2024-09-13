package com.calander.util;

import org.junit.Test;
import static org.junit.Assert.*;
import au.com.bytecode.opencsv.CSVReader;
import java.io.StringReader;
import java.io.IOException;

public class CSVReaderTest {

    @Test
    public void testParseLine() throws Exception {
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
    public void testParseLineWithEscapedQuote() throws IOException {
        // This input simulates a CSV line with an escaped quote in the first field
        // and a second field. The actual input being tested is: "\"test string","second string"
        String input = "\"\\\"test string\",\"second string\"";
        CSVReader reader = new CSVReader(new StringReader(input));
        
        String[] parsedLine = reader.readNext();
        assertNotNull(parsedLine);
        assertEquals(2, parsedLine.length);
        assertEquals("\"test string", parsedLine[0]);
        assertEquals("second string", parsedLine[1]);
    }
}