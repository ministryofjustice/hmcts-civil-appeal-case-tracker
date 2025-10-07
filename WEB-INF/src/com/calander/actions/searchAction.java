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

        System.out.println("searchAction 1");

        String searchString = request.getParameter("search").toString().toLowerCase();

        System.out.println("searchAction 2");

        Pattern pattern = Pattern.compile("^[A-Za-z0-9_, \\-\\)\\(\\.]++$");
        System.out.println("searchAction 3");
        if(!pattern.matcher(searchString).matches()) {
            searchString = "";
        }
        System.out.println("searchAction 4");

        //getting session object from Hibernate Util class
        SessionFactory factory = (SessionFactory) servlet.getServletContext().getAttribute(HibernatePlugin.KEY_NAME);
        Session session = factory.openSession();
        System.out.println("searchAction 5");

        Query query = session.createQuery("from Calander c where lower(c.search_date) like :date or lower(c.case_no) like :case or lower(title1) like :title order by c.case_no");
        query.setString("date", "%" + searchString + "%");
        query.setString("case", "%" + searchString + "%");
        query.setString("title", "%" + searchString + "%");
        System.out.println("searchAction 6");

        List arrResults = query.list();
        System.out.println("searchAction 7");

        session.clear();
        session.close();
        System.out.println("searchAction 8");

        if (isUiRequest(request)) {
            System.out.println("searchAction 9");
            request.getSession(true).setAttribute("results", arrResults);
            System.out.println("searchAction 10");
        } else {
            request.setAttribute("results", arrResults);
        }
        System.out.println("searchAction 11");
        return mapping.findForward("success");
    }

    private boolean isUiRequest(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer != null && (referer.contains("casetracker.justice.gov.uk") || referer.contains("localhost")) ) {
            return true;
        }
        return false;
    }
}
