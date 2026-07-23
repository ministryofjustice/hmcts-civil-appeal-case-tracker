package uk.gov.moj.cact.repository;

import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.gov.moj.cact.entity.CaseRecord;

import java.util.Optional;

@Repository
public interface CaseRecordRepository extends JpaRepository<CaseRecord, Integer> {

    String SEARCH_WHERE = "lower(c.searchDate) like :search "
            + "or lower(c.caseNo) like :search "
            + "or lower(c.title1) like :search";

    @Query(value = "select c from CaseRecord c where " + SEARCH_WHERE + " order by c.caseNo",
            countQuery = "select count(c) from CaseRecord c where " + SEARCH_WHERE)
    @QueryHints(@QueryHint(name = "jakarta.persistence.query.timeout", value = "5000"))
    Page<CaseRecord> search(@Param("search") String search, Pageable pageable);


    @Query("select max(c.lastUpdated) from CaseRecord c")
    Optional<String> findMaxLastUpdated();

    Optional<CaseRecord> findFirstByCaseNo(String caseNo);
}
