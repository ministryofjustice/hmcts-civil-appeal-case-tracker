package com.calander.actions;

import com.calander.plugin.HibernatePlugin;
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
import java.util.List;
import java.util.regex.Pattern;


public class searchAction extends Action {

    private static final Logger LOGGER = LoggerFactory.getLogger(caseDetailAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, Exception {
        Session session = null;
        try {
            String searchString = request.getParameter("search");
            if (searchString != null) {
                searchString = searchString.toLowerCase();
            }

            Pattern pattern = Pattern.compile("^[A-Za-z0-9_, \\-\\)\\(\\.]++$");
            if (searchString == null || !pattern.matcher(searchString).matches()) {
                searchString = "";
            }

            SessionFactory factory = (SessionFactory) servlet.getServletContext().getAttribute(HibernatePlugin.KEY_NAME);
            session = factory.openSession();

            Query query = session.createQuery("from Calander c where lower(c.search_date) like :date or lower(c.case_no) like :case or lower(title1) like :title order by c.case_no");
            query.setString("date", "%" + searchString + "%");
            query.setString("case", "%" + searchString + "%");
            query.setString("title", "%" + searchString + "%");
            
            // Add result size limit to prevent memory issues
            query.setMaxResults(1000);

            List arrResults = query.list();
            request.getSession().setAttribute("results", arrResults);

            return mapping.findForward("success");
        } catch (Exception e) {
            LOGGER.error("Error in searchAction: ", e);
            throw e;
        } finally {
            if (session != null) {
                try {
                    session.clear();
                    session.close();
                } catch (Exception e) {
                    LOGGER.error("Error closing session: ", e);
                }
            }
        }
    }
}
