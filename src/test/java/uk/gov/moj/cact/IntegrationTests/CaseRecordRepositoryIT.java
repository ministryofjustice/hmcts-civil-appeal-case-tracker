package uk.gov.moj.cact.IntegrationTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;
import uk.gov.moj.cact.entity.CaseRecord;
import uk.gov.moj.cact.repository.CaseRecordRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Testcontainers
class CaseRecordRepositoryIT {

    private static final String SCHEMA_SQL =
            System.getProperty("cact.schema.sql", "files/setup.sql");

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withCopyFileToContainer(
                    MountableFile.forHostPath(SCHEMA_SQL),
                    "/docker-entrypoint-initdb.d/setup.sql");


    @Autowired
    private CaseRecordRepository repository;

    @Test
    void searchMatchesDateCaseNoAndTitleCaseInsensitively() {
        repository.save(record("01-Jan-2026", "CA-2026-000001", "SMITH v JONES"));
        repository.save(record("02-Feb-2026", "CA-2026-000002", "ALPHA LTD v BETA LTD"));

        Page<CaseRecord> byTitle = repository.search("%smith%", PageRequest.of(0, 15));
        assertEquals(1, byTitle.getTotalElements());
        assertEquals("CA-2026-000001", byTitle.getContent().getFirst().getCaseNo());

        Page<CaseRecord> byCaseNo = repository.search("%ca-2026%", PageRequest.of(0, 15));
        assertEquals(2, byCaseNo.getTotalElements());

        Page<CaseRecord> byDate = repository.search("%02-feb%", PageRequest.of(0, 15));
        assertEquals(1, byDate.getTotalElements());
        assertEquals("CA-2026-000002", byDate.getContent().getFirst().getCaseNo());
    }

    @Test
    void idsComeFromHibernateSequence() {
        CaseRecord saved = repository.save(record("01-Jan-2026", "CA-2026-000099", "A v B"));
        assertTrue(saved.getCaseId() > 0);
    }

    @Test
    void findFirstByCaseNo() {
        repository.save(record("01-Jan-2026", "CA-2026-000010", "X v Y"));
        assertTrue(repository.findFirstByCaseNo("CA-2026-000010").isPresent());
        assertTrue(repository.findFirstByCaseNo("NOPE").isEmpty());
    }

    private static CaseRecord record(String searchDate, String caseNo, String title1) {
        CaseRecord r = new CaseRecord();
        r.setSearchDate(searchDate);
        r.setCaseNo(caseNo);
        r.setTitle1(title1);
        return r;
    }
}

