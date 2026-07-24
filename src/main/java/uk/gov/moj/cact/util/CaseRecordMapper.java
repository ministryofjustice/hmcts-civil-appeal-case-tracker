package uk.gov.moj.cact.util;

import uk.gov.moj.cact.entity.CaseRecord;

/**
 * Maps a raw CSV row to a CaseRecord.
 * Ported from the legacy Calander.setProperties and Calander.buildTrackLines
 */
public final class CaseRecordMapper {

    static final String TRACK_LINE_SEPARATOR = "\n";

    private static final int MINIMUM_COLUMNS = 25;
    private static final int MAX_FIELD_LENGTH = 255;
    private static final int FIRST_TRACKING_COLUMN = 17;
    private static final int MAX_TRACK_LINES = 8;

    private CaseRecordMapper() {
    }

    public static CaseRecord setProperties(String[] data) {
        if (data.length < MINIMUM_COLUMNS) {
            throw new IllegalArgumentException("Row has insufficient columns: " + data.length);
        }

        CaseRecord record = new CaseRecord();
        record.setSearchDate(data[0]);
        record.setCaseNo(data[1]);
        record.setHeadingStatus(data[2]);
        record.setJudge1(data[3]);
        record.setJudge2(data[4]);
        record.setJudge3(data[5]);
        record.setLcourt(data[6]);
        record.setVenue(data[7]);
        record.setCaseRef(data[8]);
        record.setTitle1(truncate((data[9] + " " + data[10]).trim()));
        record.setTitle2(truncate(data[10].trim()));
        record.setType(data[11]);
        record.setLcJudge(data[12]);
        record.setNature(data[13]);
        record.setLastUpdated(data[14]);
        record.setResult(data[15]);
        record.setStatus(data[16]);

        String[] tracks = buildTrackLines(data);
        record.setTrackLine1(tracks[0]);
        record.setTrackLine2(tracks[1]);
        record.setTrackLine3(tracks[2]);
        record.setTrackLine4(tracks[3]);
        record.setTrackLine5(tracks[4]);
        record.setTrackLine6(tracks[5]);
        record.setTrackLine7(tracks[6]);
        record.setTrackLine8(tracks[7]);
        return record;
    }

    private static String[] buildTrackLines(String[] data) {
        String[] tracks = new String[MAX_TRACK_LINES];
        int source = FIRST_TRACKING_COLUMN;
        int target = 0;

        while (source < data.length && target < MAX_TRACK_LINES) {
            String first = trimOrEmpty(data[source]);
            String second = source + 1 < data.length ? trimOrEmpty(data[source + 1]) : "";

            String combined = first + TRACK_LINE_SEPARATOR + second;
            if (!second.isEmpty() && combined.length() <= MAX_FIELD_LENGTH) {
                tracks[target] = combined;
                source += 2;
            } else {
                tracks[target] = first;
                source += 1;
            }
            target++;
        }
        return tracks;
    }

    private static String truncate(String value) {
        return value.length() > MAX_FIELD_LENGTH ? value.substring(0, MAX_FIELD_LENGTH) : value;
    }

    private static String trimOrEmpty(String s) {
        return s == null ? "" : s.trim();
    }
}
