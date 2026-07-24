package uk.gov.moj.cact.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.moj.cact.entity.CaseRecord;
import uk.gov.moj.cact.repository.CaseRecordRepository;
import uk.gov.moj.cact.util.CaseRecordMapper;
import uk.gov.moj.cact.util.CsvValidator;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Re-write of the legacy import pipeline of dumpDatabase action + CsvProcessor, still using
 * Calander.setProperties. The CSV is parsed into CaseRecord entities, then the
 * table is cleared and rewritten in a single transaction, so a failed import
 * rolls back rather than leaving the table empty.
 */
@Service
public class CsvImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvImportService.class);

    private final CaseRecordRepository repository;

    public CsvImportService(CaseRecordRepository repository) {
        this.repository = repository;
    }

    /**
     * Replaces every row in db_calander with the contents of the given CSV and
     * returns the number of rows imported.
     */
    @Transactional
    public int replaceDatabase(Reader csvContent) throws IOException, CsvValidationException {
        List<CaseRecord> records = parse(csvContent);

        LOGGER.info("Replacing db_calander contents with {} imported rows", records.size());
        repository.deleteAllInBatch();

        return repository.saveAll(records).size();
    }

    private List<CaseRecord> parse(Reader csvContent) throws IOException, CsvValidationException {
        List<CaseRecord> records = new ArrayList<>();

        try (CSVReader reader = new CSVReader(csvContent)) {
            String[] row;
            int rowNumber = 0;

            while ((row = reader.readNext()) != null) {
                // cleanRow only gate-keeps oversized rows; the raw row is mapped,
                // matching the legacy behaviour.
                if (CsvValidator.cleanRow(row, rowNumber) == null) {
                    LOGGER.error("Skipping row {}", rowNumber);
                } else {
                    records.add(CaseRecordMapper.setProperties(row));
                }
                rowNumber++;
            }
        }
        return records;
    }
}
