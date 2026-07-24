package uk.gov.moj.cact.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.moj.cact.entity.CaseRecord;
import uk.gov.moj.cact.repository.CaseRecordRepository;

import java.util.Optional;

@Controller
@RequestMapping("case")
public class CaseDetailController {

    private final CaseRecordRepository repository;


    public CaseDetailController(CaseRecordRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String getCaseDetailWithoutCase(HttpServletResponse response) {
        response.setHeader("X-Robots-Tag", "noindex, nofollow");
        return "detail";
    }

    @GetMapping("{caseNo}")
    public String getCaseDetail(@PathVariable String caseNo, HttpServletResponse response, Model model) {
        response.setHeader("X-Robots-Tag", "noindex, nofollow");

        if (caseNo.trim().isEmpty()) {
            return "detail";
        }

        Optional<CaseRecord> caseRecord = repository.findFirstByCaseNo(caseNo);

        if (caseRecord.isEmpty()) {
            return "error";
        }

        model.addAttribute("detail", caseRecord.get());
        model.addAttribute("caseNo", caseNo);
        return "detail";
    }

}
