package com.calander.actions;

import com.calander.beans.Calander;
import com.calander.plugin.HibernatePlugin;
import org.apache.struts.action.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Query;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.logging.Logger;

public class searchAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(searchAction.class.getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        
        Session session = null;
        try {
            // Get SessionFactory from context
            SessionFactory factory = (SessionFactory) servlet.getServletContext()
                    .getAttribute(HibernatePlugin.KEY_NAME);
            if (factory == null) {
                LOGGER.info("SessionFactory not found in servlet context");
                return mapping.findForward("error");
            }

            // Open session and perform search
            session = factory.openSession();
            
            String searchString = request.getParameter("searchString");
            Query query;
            
            if (searchString == null || searchString.trim().isEmpty()) {
                // Return all results if search string is null or empty
                LOGGER.info("Empty search string - returning all results");
                query = session.createQuery("from Calander c order by c.case_no");
            } else {
                searchString = searchString.toLowerCase();
                query = session.createQuery(
                    "from Calander c where lower(c.search_date) like :date " +
                    "or lower(c.case_no) like :case " +
                    "or lower(title1) like :title " +
                    "order by c.case_no");
                
                query.setString("date", "%" + searchString + "%");
                query.setString("case", "%" + searchString + "%");
                query.setString("title", "%" + searchString + "%");
            }
            
            List<?> arrResults = query.list();
            request.getSession().setAttribute("results", arrResults);

            return mapping.findForward("success");
            
        } catch (Exception e) {
            LOGGER.info("Search error: " + e.getMessage());
            return mapping.findForward("error");
            
        } finally {
            if (session != null && session.isOpen()) {
                try {
                    session.clear();
                    session.close();
                } catch (Exception e) {
                    LOGGER.info("Error closing session: " + e.getMessage());
                }
            }
        }
    }
}
