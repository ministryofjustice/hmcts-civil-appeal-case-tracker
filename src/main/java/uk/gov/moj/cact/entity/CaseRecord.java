package uk.gov.moj.cact.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "db_calander")
@AllArgsConstructor
@Setter
@Getter
public class CaseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_seq")
    @SequenceGenerator(name = "hibernate_seq", sequenceName = "hibernate_sequence", allocationSize = 1)
    @Column(name = "case_id")
    private Integer caseId;

    @Column(name = "search_date")
    private String searchDate;

    @Column(name = "case_no", unique = true)
    private String caseNo;

    @Column(name = "heading_status")
    private String headingStatus;

    @Column(name = "judge1")
    private String judge1;

    @Column(name = "judge2")
    private String judge2;

    @Column(name = "judge3")
    private String judge3;

    @Column(name = "lcourt")
    private String lcourt;

    @Column(name = "venue")
    private String venue;

    @Column(name = "case_ref")
    private String caseRef;

    @Column(name = "title1")
    private String title1;

    @Column(name = "title2")
    private String title2;

    @Column(name = "type")
    private String type;

    @Column(name = "lc_judge")
    private String lcJudge;

    @Column(name = "nature")
    private String nature;

    @Column(name = "last_updated")
    private String lastUpdated;

    @Column(name = "result")
    private String result;

    @Column(name = "status")
    private String status;

    @Column(name = "track_line1")
    private String trackLine1;

    @Column(name = "track_line2")
    private String trackLine2;

    @Column(name = "track_line3")
    private String trackLine3;

    @Column(name = "track_line4")
    private String trackLine4;

    @Column(name = "track_line5")
    private String trackLine5;

    @Column(name = "track_line6")
    private String trackLine6;

    @Column(name = "track_line7")
    private String trackLine7;

    @Column(name = "track_line8")
    private String trackLine8;
}
