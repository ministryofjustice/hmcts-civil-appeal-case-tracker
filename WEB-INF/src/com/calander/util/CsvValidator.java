package com.calander.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashSet;
import java.util.Set;

public class CsvValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvValidator.class);

    private static final int MAX_COLUMNS = 67;
    private static final int EXPECTED_IMPORT_COLUMNS = 25;

    // Columns with varchar(50)
    private static final Set<Integer> VARCHAR50_COLUMNS = new HashSet<Integer>() {{
        add(0);
        add(1);
        add(8);
        add(14);
    }};

    /**
     * Cleans and validates the first 25 columns of the row.
     * Returns a new array with sanitized values for import, or null if row is too short.
     */
    public static String[] cleanRow(String[] row, int rowNumber) {

        if (row.length < EXPECTED_IMPORT_COLUMNS) {
            LOGGER.error("Row {} has only {} columns, expected {}", rowNumber, row.length, EXPECTED_IMPORT_COLUMNS);

        }

        if (row.length > MAX_COLUMNS) {
            LOGGER.error("Row {} has unusually large column count: {}", rowNumber, row.length);
            return null;
        }

        if (row.length > EXPECTED_IMPORT_COLUMNS) {
            LOGGER.warn("Row {} contains {} columns ({} expected for base import, remaining used for tracking lines)",
                    rowNumber, row.length, EXPECTED_IMPORT_COLUMNS);
        }

        String[] sanitized = new String[row.length];

        for (int i = 0; i < row.length; i++) {

            String value = row[i] != null ? row[i] : "";

            // Remove BOM
            if (rowNumber == 0 && i == 0 && value.startsWith("\uFEFF")) {
                LOGGER.warn("BOM detected and removed at row {}, column {}", rowNumber, i);
                value = value.substring(1);
            }

            // Remove control characters
            StringBuilder sb = new StringBuilder();
            for (char c : value.toCharArray()) {
                if (!Character.isISOControl(c) || Character.isWhitespace(c)) {
                    sb.append(c);
                }
            }

            value = sb.toString().trim();

            // Only enforce DB length limits for imported columns
            if (i < EXPECTED_IMPORT_COLUMNS) {

                int limit = VARCHAR50_COLUMNS.contains(i) ? 50 : 255;

                if (value.length() > limit) {
                    LOGGER.warn("Row {} column {} exceeds max length {}. Truncating from {}",
                            rowNumber, i, limit, value.length());
                    value = value.substring(0, limit);
                }
            }

            sanitized[i] = value;
        }

        return sanitized;
    }


    private String[] buildTrackLines(String[] data) {

        String[] tracks = new String[8];

        int source = 17;
        int target = 0;

        while (source < data.length && target < 8) {

            String first = safe(data[source]);

            String second = "";
            if (source + 1 < data.length) {
                second = safe(data[source + 1]);
            }

            String combined = first + "<br/>" + second;

            if (!second.isEmpty() && combined.length() <= 255) {

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

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }



}
