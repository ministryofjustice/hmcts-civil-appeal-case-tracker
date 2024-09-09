package com.calander.actions;

import au.com.bytecode.opencsv.CSVReader;
import com.calander.util.CsvProcessor;
import com.calander.plugin.HibernatePlugin;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.util.logging.Logger;

public class dumpDatabase extends Action {

    private static final Logger LOGGER = Logger.getLogger(dumpDatabase.class.getName());

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

            // Get the actual path of the uploaded file
            String uploadDir = "/usr/local/tomcat/webapps/ROOT/uploadfile/";
            File dir = new File(uploadDir);
            File[] files = dir.listFiles((d, name) -> name.endsWith(".CSV") || name.endsWith(".csv"));
            
            if (files == null || files.length == 0) {
                throw new FileNotFoundException("No CSV file found in " + uploadDir);
            }
            
            String filePath = files[0].getAbsolutePath();
            LOGGER.info("Processing file: " + filePath);

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
