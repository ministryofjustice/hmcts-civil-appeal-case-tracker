package uk.gov.moj.cact.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.moj.cact.entity.CaseRecord;
import uk.gov.moj.cact.monitoring.UiRequestValidator;
import uk.gov.moj.cact.repository.CaseRecordRepository;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Controller
public class SearchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

    private static final Pattern SEARCH_PATTERN = Pattern.compile("^[A-Za-z0-9_, \\-\\)\\(\\.]++$");
    private static final int UI_MAX_RESULTS = 1000;
    private static final int UI_PAGE_SIZE = 15;
    private static final int API_DEFAULT_PAGE_SIZE = 15;
    private static final int API_MAX_PAGE_SIZE = 50;

    /** Page links shown at once - displaytag's default group size. */
    private static final int UI_PAGE_LINKS = 8;

    private final CaseRecordRepository repository;

    public SearchController(CaseRecordRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "search", required = false) String searchParam,
                         @RequestParam(name = "page", required = false) String pageParam,
                         @RequestParam(name = "pageSize", required = false) String pageSizeParam,
                         HttpServletRequest request,
                         Model model) {

        if (searchParam == null) {
            return "search";
        }

        String searchString = sanitizeSearchInput(searchParam);
        String like = "%" + searchString.toLowerCase() + "%";
        boolean isUi = UiRequestValidator.isUiRequest(request);

        if (isUi) {
            int page = parsePage(pageParam);

            long total = Math.min(repository.search(like, PageRequest.of(0, 1)).getTotalElements(), UI_MAX_RESULTS);
            int totalPages = Math.max((int) Math.ceil((double) total / UI_PAGE_SIZE), 1);
            page = Math.min(page, totalPages);

            int startIndex = (page - 1) * UI_PAGE_SIZE + 1;

            List<CaseRecord> results = total == 0
                    ? List.of()
                    : repository.search(like, PageRequest.of(page - 1, UI_PAGE_SIZE)).getContent();

            int roomLeft = (int) Math.max(0, total - startIndex + 1);
            if (results.size() > roomLeft) {
                results = results.subList(0, roomLeft);
            }

            LOGGER.info("[UI] Returned <{}> of <{}> rows for search <{}> page={}",
                    results.size(), total, searchString, page);
            model.addAttribute("uiMode", true);
            model.addAttribute("results", results);
            model.addAttribute("totalResults", total);
            model.addAttribute("page", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("startIndex", startIndex);
            model.addAttribute("endIndex", startIndex + results.size() - 1);
            model.addAttribute("hasNextPage", page < totalPages);
            model.addAttribute("pageWindow", pageWindow(page, totalPages));
            model.addAttribute("searchString", searchString);
            return "search";
        }

        int page = parsePage(pageParam);
        int pageSize = parsePageSize(pageSizeParam);

        Page<CaseRecord> result = repository.search(like, PageRequest.of(page - 1, pageSize));
        long totalResults = result.getTotalElements();
        int totalPages = (int) Math.ceil((double) totalResults / pageSize);

        if (totalResults > 0 && page > totalPages) {
            LOGGER.info("[API] Invalid page requested: page={} totalPages={} for search <{}>",
                    page, totalPages, searchString);
            model.addAttribute("invalidPage", Boolean.TRUE);
            model.addAttribute("totalResults", totalResults);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("pageSize", pageSize);
            return "error";
        }

        List<CaseRecord> results = result.getContent();
        boolean hasNextPage = (long) page * pageSize < totalResults;
        int startIndex = (page - 1) * pageSize + 1;

        LOGGER.info("[API] Search <{}> page={} size={} returned={}",
                searchString, page, pageSize, results.size());

        model.addAttribute("uiMode", false);
        model.addAttribute("searchString", searchString);
        model.addAttribute("totalResults", totalResults);
        model.addAttribute("results", results);
        model.addAttribute("startIndex", startIndex);
        model.addAttribute("endIndex", startIndex + results.size() - 1);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("hasNextPage", hasNextPage);
        model.addAttribute("page", page);
        model.addAttribute("pageSize", pageSize);
        return "search";
    }

    /**
     * The page numbers to show at once. Legacy displaytag showed a sliding
     * window of 8 rather than every page, which matters at the 1000-result cap
     * where listing all 67 pages would be unusable.
     */
    private static List<Integer> pageWindow(int page, int totalPages) {
        if (totalPages <= UI_PAGE_LINKS) {
            return IntStream.rangeClosed(1, totalPages).boxed().toList();
        }
        int start = Math.max(1,
                Math.min(page - (UI_PAGE_LINKS / 2 - 1), totalPages - UI_PAGE_LINKS + 1));
        return IntStream.rangeClosed(start, start + UI_PAGE_LINKS - 1).boxed().toList();
    }

    private static int parsePage(String pageParam) {
        if (pageParam != null) {
            try {
                return Math.max(Integer.parseInt(pageParam), 1);
            } catch (NumberFormatException ignored) {
                // fall through to default, as legacy did
            }
        }
        return 1;
    }

    private static int parsePageSize(String sizeParam) {
        try {
            return Math.clamp(Integer.parseInt(sizeParam), 1, API_MAX_PAGE_SIZE);
        } catch (RuntimeException e) {
            LOGGER.warn("getPageSize Exception: ", e);
            return API_DEFAULT_PAGE_SIZE;
        }
    }

    private static String sanitizeSearchInput(String searchString) {
        if (searchString == null || searchString.trim().isEmpty()) {
            return "";
        }
        if (!SEARCH_PATTERN.matcher(searchString.trim()).matches()) {
            return "";
        }
        return searchString.toLowerCase();
    }


}
