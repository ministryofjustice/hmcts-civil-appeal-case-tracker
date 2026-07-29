package uk.gov.moj.cact.util;

import org.junit.jupiter.api.Test;
import uk.gov.moj.cact.entity.CaseRecord;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CaseRecordMapperTest {

    private static final int MINIMUM_COLUMNS = 25;

    private static String[] row(String... trackingColumns) {
        String[] head = {
                "09-Feb-16", "20140016", "Heading status",
                "Judge One", "Judge Two", "Judge Three",
                "Lower court", "London", "C5/2014/0016",
                "BK (India) & Anr", "v Secretary of State",
                "Appeal", "LC judge", "Nature",
                "13-Jun-17", "Result", "Awaiting a hearing"
        };
        int total = Math.max(head.length + trackingColumns.length, MINIMUM_COLUMNS);
        String[] full = new String[total];
        Arrays.fill(full, "");
        System.arraycopy(head, 0, full, 0, head.length);
        System.arraycopy(trackingColumns, 0, full, head.length, trackingColumns.length);
        return full;
    }


    @Test
    void mapsTheLeadingColumnsToTheMatchingFields() {
        CaseRecord record = CaseRecordMapper.setProperties(row());

        assertEquals("09-Feb-16", record.getSearchDate());
        assertEquals("20140016", record.getCaseNo());
        assertEquals("Heading status", record.getHeadingStatus());
        assertEquals("Judge One", record.getJudge1());
        assertEquals("Judge Two", record.getJudge2());
        assertEquals("Judge Three", record.getJudge3());
        assertEquals("Lower court", record.getLcourt());
        assertEquals("London", record.getVenue());
        assertEquals("C5/2014/0016", record.getCaseRef());
        assertEquals("Appeal", record.getType());
        assertEquals("LC judge", record.getLcJudge());
        assertEquals("Nature", record.getNature());
        assertEquals("13-Jun-17", record.getLastUpdated());
        assertEquals("Result", record.getResult());
        assertEquals("Awaiting a hearing", record.getStatus());
    }

    @Test
    void combinesColumnsNineAndTenIntoTitle1AndKeepsColumnTenAsTitle2() {
        CaseRecord record = CaseRecordMapper.setProperties(row());

        assertEquals("BK (India) & Anr v Secretary of State", record.getTitle1());
        assertEquals("v Secretary of State", record.getTitle2());
    }

    @Test
    void truncatesTitlesToTheColumnLength() {
        String[] data = row();
        data[9] = "A".repeat(200);
        data[10] = "B".repeat(200);

        CaseRecord record = CaseRecordMapper.setProperties(data);

        assertEquals(255, record.getTitle1().length());
        assertEquals(200, record.getTitle2().length());
    }

    @Test
    void doesNotPairWhenTheCombinedLengthWouldExceedTheColumnLimit() {
        String first = "A".repeat(200);
        String second = "B".repeat(200);

        CaseRecord record = CaseRecordMapper.setProperties(row(first, second));

        // 200 + 1 + 200 > 255, so each event stays in its own column.
        assertEquals(first, record.getTrackLine1());
        assertEquals(second, record.getTrackLine2());
    }


    @Test
    void rejectsRowsWithFewerThanTwentyFiveColumns() {
        String[] tooShort = new String[24];
        Arrays.fill(tooShort, "x");

        assertThrows(IllegalArgumentException.class, () -> CaseRecordMapper.setProperties(tooShort));
    }
}
