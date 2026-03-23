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
import java.util.List;
import java.util.regex.Pattern;


public class searchAction extends Action {

    private static final Logger LOGGER = LoggerFactory.getLogger(searchAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, Exception {


        String searchString = sanitizeSearchInput(request.getParameter("search"));
        /*
        if (searchString.isEmpty()) {
            return mapping.findForward("success");
        }
        */

        //getting session object from Hibernate Util class
        SessionFactory factory = (SessionFactory) servlet.getServletContext().getAttribute(HibernatePlugin.KEY_NAME);
        Session session = factory.openSession();

        Query query = session.createQuery("from Calander c where lower(c.search_date) like :date or lower(c.case_no) like :case or lower(title1) like :title order by c.case_no");
        query.setString("date", "%" + searchString + "%");
        query.setString("case", "%" + searchString + "%");
        query.setString("title", "%" + searchString + "%");

        List arrResults = query.list();

        LOGGER.info(MessageFormat.format("Returned <{0}> rows for search using string <{1}> on search_date or case_no or title1 ", arrResults.size(), searchString));

        session.clear();
        session.close();

        if (isUiRequest(request.getHeader("Referer"))) {
            request.getSession(true).setAttribute("results", arrResults);
        } else {
            request.setAttribute("results", arrResults);
        }

        try {
            return mapping.findForward("success");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex; // or return mapping.findForward("error");
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

    public static boolean isUiRequest(String referer) {
        if (referer != null && (referer.contains("casetracker.justice.gov.uk") || referer.contains("localhost")) ) {
            return true;
        }
        return false;
    }
}
