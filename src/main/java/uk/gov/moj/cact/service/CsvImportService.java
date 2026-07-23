package uk.gov.moj.cact.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import uk.gov.moj.cact.repository.CaseRecordRepository;
import uk.gov.moj.cact.entity.CaseRecord;
import uk.gov.moj.cact.util.CsvRowValidator;


@Service
public class CsvImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvImportService.class);
    private static final int BATCH_SIZE = 50;

    private final CaseRecordRepository repository;


    public CsvImportService(CaseRecordRepository repository) {
        this.repository = repository;
    }


}
