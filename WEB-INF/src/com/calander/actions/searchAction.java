package com.calander.actions;

import com.calander.plugin.HibernatePlugin;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;


public class searchAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, Exception {

        // Null check for "search" parameter
        String searchString = request.getParameter("search");
        if (searchString != null) {
            searchString = searchString.toLowerCase();
        } else {
            // Handle the case where "search" parameter is not present.
            // Set a default value or log a message.
            searchString = "";  // Setting a default value
            System.out.println("Warning: 'search' parameter is missing in the request.");
        }

        Pattern pattern = Pattern.compile("^[A-Za-z0-9_, \\-\\)\\(\\.]++$");
        if(!pattern.matcher(searchString).matches()) {
            searchString = "";
        }

        //getting session object from Hibernate Util class
        SessionFactory factory = (SessionFactory) servlet.getServletContext().getAttribute(HibernatePlugin.KEY_NAME);
        Session session = factory.openSession();

        if (session != null) {
            try {
                // Query creation and execution
                Query query = session.createQuery("from Calander c where lower(c.search_date) like :date or lower(c.case_no) like :case or lower(title1) like :title order by c.case_no");

                if (query != null) {
                    query.setString("date", "%" + searchString + "%");
                    query.setString("case", "%" + searchString + "%");
                    query.setString("title", "%" + searchString + "%");

                    // Execute the query and process the results
                    List arrResults = query.list();

                    // Clear and close the session
                    session.clear();
                    session.close();

                    // Set results in session
                    request.getSession().setAttribute("results", arrResults);

                    return mapping.findForward("success");
                } else {
                    // Handle the case where the query object is not successfully created.
                    System.err.println("Error: Unable to create Hibernate query.");
                }
            } catch (Exception e) {
                // Handle any other exceptions that might occur during query execution
                System.err.println("Error executing Hibernate query: " + e.getMessage());
            } finally {
                // Ensure session is closed in case of any exception
                if (session.isOpen()) {
                    session.close();
                }
            }
        } else {
            // Handle the case where the session is not successfully opened.
            System.err.println("Error: Unable to open Hibernate session.");
        }

        // Return a forward in case of errors
        return mapping.findForward("error");
    }
}
