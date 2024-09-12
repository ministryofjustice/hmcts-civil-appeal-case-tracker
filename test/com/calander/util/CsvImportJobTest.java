package com.calander.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.hibernate.Session;
import org.hibernate.Query;
import org.mockito.Mockito;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CsvImportJobTest {

    @Test
    public void testIsLastUpdatedYesterday() throws Exception {
        Session mockSession = Mockito.mock(Session.class);
        Query mockQuery = Mockito.mock(Query.class);

        Mockito.when(mockSession.createQuery(Mockito.anyString())).thenReturn(mockQuery);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date today = new Date();
        Date yesterday = new Date(today.getTime() - 24 * 60 * 60 * 1000);

        // Test when last updated was yesterday
        Mockito.when(mockQuery.uniqueResult()).thenReturn(sdf.format(yesterday));
        assertTrue(CsvImportJob.isLastUpdatedYesterday(mockSession));

        // Test when last updated was today
        Mockito.when(mockQuery.uniqueResult()).thenReturn(sdf.format(today));
        assertFalse(CsvImportJob.isLastUpdatedYesterday(mockSession));

        // Test when last updated was null
        Mockito.when(mockQuery.uniqueResult()).thenReturn(null);
        assertFalse(CsvImportJob.isLastUpdatedYesterday(mockSession));
    }
}