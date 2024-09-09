package com.calander.actions;

import au.com.bytecode.opencsv.CSVReader;

import com.calander.beans.Calander;
import com.calander.plugin.HibernatePlugin;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class dumpDatabase extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HibernatePlugin hibernatePlugin = new HibernatePlugin();
        SessionFactory factory = null;
        try {
            factory = (SessionFactory) hibernatePlugin.getconnection();
        } catch (Exception e) {
            throw new RuntimeException("Hibernate Plugin unable to get connection");
        }
        Session session = factory.openSession();

        try {
            session.beginTransaction();
            session.createQuery("delete Calander").executeUpdate();

            String filePath = getServlet().getServletContext().getRealPath("/") + "uploadfile/data.csv";
            CSVReader reader = new CSVReader(new FileReader(filePath));

            int rowCount = CsvProcessor.processCSV(reader, session);

            session.getTransaction().commit();
            request.setAttribute("msg", rowCount + " rows added in database");
        } catch (Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
            request.setAttribute("msg", "Import Failed: " + ex.getMessage());
        } finally {
            session.close();
        }

        return mapping.findForward("success");
    }

}
