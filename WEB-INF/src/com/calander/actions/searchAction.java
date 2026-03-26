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

                return mapping.findForward("success");

            } else {

                // Non-ui request now requires paging to support scraping
                int page = getPage(request);
                int pageSize = getPageSize(request);

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

                Long totalResults = getTotalCount(session, searchString);
                int totalPages = (int) Math.ceil((double) totalResults / pageSize);

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
            } catch (Exception ignored) {}
        }

        // DisplayTag fallback (d-xxx-p)
        Enumeration<?> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String name = (String) params.nextElement();
            if (name.startsWith("d-") && name.endsWith("-p")) {
                try {
                    return Math.max(Integer.parseInt(request.getParameter(name)), 1);
                } catch (Exception ignored) {}
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

        // Explicit override (future-proof)
        String mode = request.getParameter("mode");
        if ("api".equalsIgnoreCase(mode)) return false;
        if ("ui".equalsIgnoreCase(mode)) return true;

        // Accept header (best signal)
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            return false;
        }

        // Default to UI
        return true;
    }

    public static boolean isUiRequestOld(String referer) {
        if (referer != null && (referer.contains("casetracker.justice.gov.uk") || referer.contains("localhost")) ) {
            return true;
        }
        return false;
    }
}
