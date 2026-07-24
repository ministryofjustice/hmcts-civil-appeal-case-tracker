package uk.gov.moj.cact.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Permanent redirects from every legacy Struts/JSP URL to the new endpoints
 */
@Controller
public class LegacyRedirectController {

    @GetMapping("/index.jsp")
    public ResponseEntity<Void> indexJsp() {
        return movedPermanently("/");
    }

    @GetMapping("/search.jsp")
    public ResponseEntity<Void> searchJsp() {
        return movedPermanently("/search");
    }

    @GetMapping("/search.do")
    public ResponseEntity<Void> searchDo(HttpServletRequest request) {
        String query = request.getQueryString();
        return movedPermanently(query == null ? "/search" : "/search?" + query);
    }

    @GetMapping("/getDetail.do")
    public ResponseEntity<Void> getDetailDo(@RequestParam(name = "case_id", required = false) String caseId) {
        if (caseId == null || caseId.trim().isEmpty()) {
            return movedPermanently("/case");
        }
        return movedPermanently("/case/" + URLEncoder.encode(caseId, StandardCharsets.UTF_8));
    }

    @GetMapping("/loginform.do")
    public ResponseEntity<Void> loginFormDo() {
        return movedPermanently("/admin/login");
    }

    @PostMapping("/validateLogin.do")
    public ResponseEntity<Void> validateLoginDo() {
        return permanentRedirect("/admin/login");
    }

    @GetMapping("/simpleUpload.do")
    public ResponseEntity<Void> simpleUploadDoGet() {
        return movedPermanently("/admin/upload");
    }

    @PostMapping("/simpleUpload.do")
    public ResponseEntity<Void> simpleUploadDoPost() {
        return permanentRedirect("/admin/upload");
    }


    @GetMapping("/dumpData.do")
    public ResponseEntity<Void> dumpDataDo() {
        return movedPermanently("/admin/import");
    }

    @GetMapping("/info.do")
    public ResponseEntity<Void> infoDo() {
        return movedPermanently("/admin/upload");
    }

    @GetMapping("/invalidate.do")
    public ResponseEntity<Void> invalidateDo() {
        return movedPermanently("/admin/logout");
    }

    // Replaces the legacy Tomcat RewriteValve rule (WEB-INF/rewrite.config)
    @RequestMapping("/listing_calendar/**")
    public ResponseEntity<Void> listingCalendar(HttpServletRequest request) {
        String stripped = request.getRequestURI().substring("/listing_calendar".length());
        if (stripped.isEmpty()) {
            stripped = "/";
        }
        String query = request.getQueryString();
        return movedPermanently(query == null ? stripped : stripped + "?" + query);
    }

    private ResponseEntity<Void> movedPermanently(String location) {
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).location(URI.create(location)).build();
    }

    private ResponseEntity<Void> permanentRedirect(String location) {
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).location(URI.create(location)).build();
    }
}
