package com.calander.actions;

import com.calander.plugin.HibernatePlugin;
import com.calander.util.CsvImportJob;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;


public class searchAction extends Action {

    private static final Logger LOGGER = LoggerFactory.getLogger(searchAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws IOException, ServletException, Exception {

        String searchString = sanitizeSearchInput(request.getParameter("search"));

        SessionFactory factory = (SessionFactory) servlet.getServletContext()
                .getAttribute(HibernatePlugin.KEY_NAME);

        Session session = null;

        try {
            session = factory.openSession();

            boolean isUi = isUiRequest(request);

            String hql = "from Calander c " +
                    "where lower(c.search_date) like :search " +
                    "or lower(c.case_no) like :search " +
                    "or lower(c.title1) like :search " +
                    "order by c.case_no";

            Query query = session.createQuery(hql);
            query.setString("search", "%" + searchString.toLowerCase() + "%");
            query.setTimeout(5); // safety

            if (isUi) {

                // UI mode - restrict query to 1000 results

                query.setMaxResults(1000); // IMPORTANT: prevent abuse

                List results = query.list();

                LOGGER.info(MessageFormat.format(
                        "[UI] Returned <{0}> rows for search <{1}>",
                        results.size(), searchString
                ));

                request.getSession(true).setAttribute("results", results);
                request.getSession(true).setAttribute("isUI", "true");


                return mapping.findForward("success");

            } else {

                // Non-ui request now requires paging to support scraping
                int page = getPage(request);
                int pageSize = getPageSize(request);

                // Get total count first so we can validate the requested page number
                Long totalResults = getTotalCount(session, searchString);
                int totalPages = (int) Math.ceil((double) totalResults / pageSize);

                if ((totalResults > 0 ) && (page > totalPages)) {
                    LOGGER.info(MessageFormat.format(
                            "[API] Invalid page requested: page={0} totalPages={1} for search <{2}>",
                            page, totalPages, searchString
                    ));
                    request.setAttribute("invalidPage", Boolean.TRUE);
                    request.setAttribute("totalResults", totalResults);
                    request.setAttribute("totalPages", totalPages);
                    return mapping.findForward("error");
                }

                query.setFirstResult((page - 1) * pageSize);
                query.setMaxResults(pageSize);

                List results = query.list();

                boolean hasNextPage = results.size() == pageSize;

                LOGGER.info(MessageFormat.format(
                        "[API] Search <{0}> page={1} size={2} returned={3}",
                        searchString, page, pageSize, results.size()
                ));

                int startIndex = (page - 1) * pageSize + 1;
                int endIndex = startIndex + results.size() - 1;

                request.setAttribute("isUI", "false");
                request.setAttribute("searchString", searchString);
                request.setAttribute("totalResults", totalResults);
                request.setAttribute("results", results);
                request.setAttribute("startIndex", Integer.valueOf(startIndex));
                request.setAttribute("endIndex", Integer.valueOf(endIndex));
                request.setAttribute("totalPages", Integer.valueOf(totalPages));
                request.setAttribute("hasNextPage", String.valueOf(hasNextPage));
                request.setAttribute("page", Integer.valueOf(page));
                request.setAttribute("pageSize", Integer.valueOf(pageSize));

                return mapping.findForward("success");
            }

        } catch (Exception ex) {
            LOGGER.error("Search failed", ex);
            throw ex;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    private Long getTotalCount(Session session, String searchString) {

        String hql = "select count(*) from Calander c " +
                "where lower(c.search_date) like :search " +
                "or lower(c.case_no) like :search " +
                "or lower(c.title1) like :search";

        Query countQuery = session.createQuery(hql);
        countQuery.setString("search", "%" + searchString.toLowerCase() + "%");
        countQuery.setTimeout(5);

        Number result = (Number) countQuery.uniqueResult();
        return result.longValue();
    }

    private int getPage(HttpServletRequest request) {

        // Standard param first
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                return Math.max(Integer.parseInt(pageParam), 1);
            } catch (Exception ignored) {
                LOGGER.info(MessageFormat.format("getPage Exception {0}", ignored.getMessage()));
            }
        }

        // DisplayTag fallback (d-xxx-p)
        Enumeration<?> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String name = (String) params.nextElement();
            if (name.startsWith("d-") && name.endsWith("-p")) {
                try {
                    return Math.max(Integer.parseInt(request.getParameter(name)), 1);
                } catch (Exception ignored) {
                    LOGGER.info(MessageFormat.format("getPage Exception {0}", ignored.getMessage()));
                }
            }
        }

        return 1;
    }

    private int getPageSize(HttpServletRequest request) {

        String sizeParam = request.getParameter("pageSize");
        int defaultSize = 15;
        int maxSize = 50;

        try {
            int size = Integer.parseInt(sizeParam);
            return Math.min(Math.max(size, 1), maxSize);
        } catch (Exception e) {
            LOGGER.info(MessageFormat.format("getPageSize Exception {0}", e.getMessage()));
            return defaultSize;
        }
    }

    private static String sanitizeSearchInput(String searchString) {
        if (searchString == null || searchString.trim().isEmpty()) {
            return "";
        }

        Pattern pattern = Pattern.compile("^[A-Za-z0-9_, \\-\\)\\(\\.]++$");
        if (!pattern.matcher(searchString.trim()).matches()) {
            return "";
        }
        return searchString.toLowerCase();
    }

    public static boolean isUiRequest(HttpServletRequest request) {

        String accept   = request.getHeader("Accept");
        String referer  = request.getHeader("Referer");
        String uiParam  = request.getParameter("ui");
        String userAgent = request.getHeader("User-Agent");

        // Explicit override parameter - most reliable signal
        // UI users can add ?ui=true to a bookmarked URL if needed
        if ("true".equalsIgnoreCase(uiParam)) {
            LOGGER.info("isUiRequest: explicit ui=true parameter");
            return true;
        }
        if ("false".equalsIgnoreCase(uiParam)) {
            LOGGER.info("isUiRequest: explicit ui=false parameter");
            return false;
        }

        // Definite non-UI signals
        if (accept != null && accept.contains("application/json")) {
            LOGGER.info("isUiRequest: Accept header is JSON - non-UI");
            return false;
        }

        // curl and common API tools identify themselves clearly
        if (userAgent != null && (
                userAgent.startsWith("curl/") ||
                        userAgent.startsWith("wget/") ||
                        userAgent.startsWith("Python") ||
                        userAgent.startsWith("Java/")  ||
                        userAgent.startsWith("Apache-HttpClient"))) {
            LOGGER.info(MessageFormat.format("isUiRequest: API user agent detected - non-UI: {0}", userAgent));
            return false;
        }

        // Referer present and matches known domain - definite UI
        if (referer != null && (
                referer.contains("casetracker.justice.gov.uk") ||
                        referer.contains("localhost"))) {
            LOGGER.info("isUiRequest: Referer matches known domain - UI");
            return true;
        }

        // Accept header contains text/html - likely a browser
        if (accept != null && accept.contains("text/html")) {
            LOGGER.info("isUiRequest: Accept header is text/html - UI");
            return true;
        }

        // No Referer but Accept looks like a browser (bookmarked URL case)
        if (accept != null && accept.contains("*/*") && userAgent != null && (
                userAgent.contains("Mozilla") ||
                        userAgent.contains("Chrome")  ||
                        userAgent.contains("Safari")  ||
                        userAgent.contains("Firefox") ||
                        userAgent.contains("Edge"))) {
            LOGGER.info("isUiRequest: Browser user agent detected - UI");
            return true;
        }

        // Default to non-UI - safer for your session/memory concerns
        LOGGER.info("isUiRequest: no signals matched - defaulting to non-UI");
        return false;
    }
}
