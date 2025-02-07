package com.calander.actions;

import com.calander.plugin.HibernatePlugin;
import com.calander.beans.Calander;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class searchAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(searchAction.class);

    private void logMemoryUsage(String phase) {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory() / (1024 * 1024);
        long freeMemory = runtime.freeMemory() / (1024 * 1024);
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = runtime.maxMemory() / (1024 * 1024);
        
        LOGGER.info(String.format("%s - Memory Usage: Total=%dMB, Used=%dMB, Free=%dMB, Max=%dMB", 
            phase, totalMemory, usedMemory, freeMemory, maxMemory));
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        Session session = null;
        try {
            logMemoryUsage("Before Search");
            
            String searchString = request.getParameter("search").toString().toLowerCase();
            LOGGER.info("Search request received with query: " + searchString);

            Pattern pattern = Pattern.compile("^[A-Za-z0-9_, \\-\\)\\(\\.]++$");
            if(!pattern.matcher(searchString).matches()) {
                LOGGER.info("Search string did not match pattern, defaulting to empty string");
                searchString = "";
            }

            //getting session object from Hibernate Util class
            SessionFactory factory = (SessionFactory) servlet.getServletContext().getAttribute(HibernatePlugin.KEY_NAME);
            if (factory == null) {
                LOGGER.error("SessionFactory not found in servlet context");
                return mapping.findForward("error");
            }
            
            session = factory.openSession();

            Query query = session.createQuery("from Calander c where lower(c.search_date) like :date or lower(c.case_no) like :case or lower(title1) like :title order by c.case_no");
            query.setString("date", "%" + searchString + "%");
            query.setString("case", "%" + searchString + "%");
            query.setString("title", "%" + searchString + "%");

            List<Calander> arrResults = query.list();
            LOGGER.info("Search completed. Number of results found: " + arrResults.size());
            
            if (arrResults.size() > 0) {
                Calander firstResult = arrResults.get(0);
                LOGGER.info(String.format("First result - Case: %s, Date: %s, Title: %s",
                    firstResult.getCase_no(),
                    firstResult.getSearch_date(),
                    firstResult.getTitle1()));
                
                if (arrResults.size() > 1) {
                    Calander lastResult = arrResults.get(arrResults.size() - 1);
                    LOGGER.info(String.format("Last result - Case: %s, Date: %s, Title: %s",
                        lastResult.getCase_no(),
                        lastResult.getSearch_date(),
                        lastResult.getTitle1()));
                }
            }

            request.getSession().setAttribute("results", arrResults);

            logMemoryUsage("After Search");
            return mapping.findForward("success");
            
        } catch (Exception e) {
            LOGGER.error("Search error: " + e.getMessage(), e);
            logMemoryUsage("After Error");
            return mapping.findForward("error");
            
        } finally {
            if (session != null && session.isOpen()) {
                try {
                    session.clear();
                    session.close();
                } catch (Exception e) {
                    LOGGER.error("Error closing session: " + e.getMessage(), e);
                }
            }
        }
    }
}