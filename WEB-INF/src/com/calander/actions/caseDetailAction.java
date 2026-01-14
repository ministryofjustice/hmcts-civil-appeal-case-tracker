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

        if (case_id == null || case_id.trim().isEmpty()) {
            return mapping.findForward("success");
        }

        //getting session object from Hibernate Util class
        SessionFactory factory = (SessionFactory) servlet.getServletContext().getAttribute(HibernatePlugin.KEY_NAME);
        Session session = factory.openSession();

        Query query = session.createQuery("from Calander c where c.case_no=:case");
        query.setString("case", case_id);
        String mappingResult = "success";
        if (query.list().size() > 0) {
            Calander calander = (Calander) query.list().get(0);
            request.setAttribute("detail", calander);
            request.setAttribute("case", case_id);
        } else {
            mappingResult = "error";
        }
        session.clear();
        session.close();
        return mapping.findForward(mappingResult);
    }
}
