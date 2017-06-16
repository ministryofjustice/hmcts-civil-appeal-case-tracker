package com.calander.actions;

import com.calander.beans.Calander;
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
import java.util.regex.Pattern;

public class caseDetailAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, Exception {

        String case_id = request.getParameter("case_id");

        Pattern pattern = Pattern.compile("^[0-9]++$");
        if(!pattern.matcher(case_id).matches()) {
            case_id = "";
        }

        //getting session object from Hibernate Util class
        SessionFactory factory = (SessionFactory) servlet.getServletContext().getAttribute(HibernatePlugin.KEY_NAME);
        Session session = factory.openSession();

        Query query = session.createQuery("from Calander c where c.case_no=:case");
        query.setString("case", case_id);

        Calander calander = (Calander) query.list().get(0);

        System.out.println(calander.getCase_no());
        request.setAttribute("detail", calander);
        request.setAttribute("case", case_id);

        session.close();
        return mapping.findForward("success");
    }
}
