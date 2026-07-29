package uk.gov.moj.cact.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CaseRecordTest {

    @Test
    void splitsNewlineJoinedColumnsIntoIndividualEventsInOrder() {
        CaseRecord record = new CaseRecord();
        record.setTrackLine1("07-Jul: event A\n06-Jul: event B");
        record.setTrackLine2("05-Jul: event C");

        assertEquals(
                List.of("07-Jul: event A", "06-Jul: event B", "05-Jul: event C"),
                record.getTrackingLines());
    }

    @Test
    void skipsNullColumns() {
        CaseRecord record = new CaseRecord();
        record.setTrackLine1("only event");

        assertEquals(List.of("only event"), record.getTrackingLines());
    }

    @Test
    void trimsWhitespaceAndDropsBlankEntries() {
        CaseRecord record = new CaseRecord();
        record.setTrackLine1("  first  \n\n   \n  second ");

        assertEquals(List.of("first", "second"), record.getTrackingLines());
    }

    @Test
    void returnsEmptyListWhenThereIsNoTrackingData() {
        assertTrue(new CaseRecord().getTrackingLines().isEmpty());
    }
}
