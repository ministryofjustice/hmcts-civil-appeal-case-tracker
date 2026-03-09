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

            // Enforce DB length limits
            int limit = VARCHAR50_COLUMNS.contains(i) ? 50 : 255;
            if (value.length() > limit) {
                LOGGER.warn("Row {} column {} exceeds max length {}. Truncating from {}",
                        rowNumber, i, limit, value.length());
                value = value.substring(0, limit);
            }

            if ( (i > EXPECTED_IMPORT_COLUMNS) && (!value.isEmpty())) {
                LOGGER.warn("Row {} column {} has data for Tracking Lines",rowNumber, i);
            }

            sanitized[i] = value;
        }

        return sanitized;
    }
}
