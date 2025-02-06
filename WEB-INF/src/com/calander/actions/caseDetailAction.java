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
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

public class caseDetailAction extends Action {

    private static final Logger LOGGER = LoggerFactory.getLogger(caseDetailAction.class);
    private static final int MAX_RESULTS = 1000;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Session session = null;
        Transaction tx = null;
        
        try {
            String caseId = request.getParameter("case_id");
            if (caseId == null || caseId.trim().isEmpty()) {
                return mapping.findForward("error");
            }

            SessionFactory factory = (SessionFactory) servlet.getServletContext().getAttribute(HibernatePlugin.KEY_NAME);
            session = factory.openSession();
            tx = session.beginTransaction();

            Query query = session.createQuery("from Calander where case_id = :caseId")
                    .setParameter("caseId", caseId)
                    .setMaxResults(1);  // We only want one result
            
            Calander result = (Calander) query.uniqueResult();
            
            if (result == null) {
                return mapping.findForward("error");
            }

            request.setAttribute("detail", result);
            tx.commit();
            
            return mapping.findForward("success");
            
        } catch (Exception e) {
            LOGGER.error("Error in caseDetailAction: ", e);
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception rollbackEx) {
                    LOGGER.error("Error rolling back transaction: ", rollbackEx);
                }
            }
            request.setAttribute("exception", e);
            return mapping.findForward("exception");
            
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
