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

public class searchAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, Exception {
        String strQry;
        List arrResults = null;
        strQry = (String) request.getParameter("search");

        Session session = null;
        SessionFactory factory = null;
        Query qry = null;

        //getting session object from Hibernate Util class
        factory = (SessionFactory) servlet.getServletContext().getAttribute(HibernatePlugin.KEY_NAME);
        session = factory.openSession();


        qry = session.createQuery("from Calander c where c.search_date like :date or c.case_no like :case or title1 like :title order by c.case_no");
        qry.setString("date", "%" + strQry + "%");
        qry.setString("case", "%" + strQry + "%");
        qry.setString("title", "%" + strQry + "%");

        arrResults = qry.list();

        session.clear();
        session.close();
        request.getSession().setAttribute("results", arrResults);

        return mapping.findForward("success");
    }

}
