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

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, Exception {

        Session session = null;
        SessionFactory factory = null;
        System.out.println(HibernatePlugin.KEY_NAME);

        //getting session object from Hibernate Util class
        factory = (SessionFactory) servlet.getServletContext().getAttribute(HibernatePlugin.KEY_NAME);
        System.out.println("coming here before factory open session");

        session = factory.openSession();
        System.out.println("coming here after factory open session");

        ServletContext context = servlet.getServletContext();
        String FILE_PATH = context.getRealPath("/HMCSFormUpload/CASE_TRACKER.CSV");
        System.out.println("file path");
        String result = null;

        try {
            CSVReader reader = new CSVReader(new FileReader(FILE_PATH));
            String[] nextLine;
            Calander obj = null;


            session.beginTransaction();
            session.createQuery("delete Calander").executeUpdate();
            session.getTransaction().commit();

            session.beginTransaction();

            int ctr = 0;

            try {
                while ((nextLine = reader.readNext()) != null) {

                    int line = nextLine.length;
                    int counter = 0;


                    obj = new Calander();


                    if (counter < line)
                        obj.setSearch_date(nextLine[0]);
                    counter++;

                    if (counter < line)
                        obj.setCase_no(nextLine[1]);
                    counter++;

                    if (counter < line)
                        obj.setHeading_status(nextLine[2]);
                    counter++;

                    if (counter < line)
                        obj.setJudge1(nextLine[3]);
                    counter++;

                    if (counter < line)
                        obj.setJudge2(nextLine[4]);
                    counter++;

                    if (counter < line)
                        obj.setJudge3(nextLine[5]);
                    counter++;

                    if (counter < line)
                        obj.setLcourt(nextLine[6]);
                    counter++;

                    if (counter < line)
                        obj.setVenue(nextLine[7]);
                    counter++;

                    if (counter < line)
                        obj.setCase_ref(nextLine[8]);
                    counter++;

                    if (counter < line)
                        obj.setTitle1(nextLine[9] + " " + nextLine[10]);
                    counter++;

                    if (counter < line)
                        obj.setTitle2(nextLine[10]);
                    counter++;

                    if (counter < line)
                        obj.setType(nextLine[11]);
                    counter++;

                    if (counter < line)
                        obj.setLc_judge(nextLine[12]);
                    counter++;

                    if (counter < line)
                        obj.setNature(nextLine[13]);
                    counter++;

                    if (counter < line)
                        obj.setLast_updated(nextLine[14]);
                    counter++;

                    if (counter < line)
                        obj.setResult(nextLine[15]);
                    counter++;

                    if (counter < line)
                        obj.setStatus(nextLine[16]);
                    counter++;

                    if (counter < line)
                        obj.setTrack_line1(nextLine[17]);
                    counter++;

                    if (counter < line)
                        obj.setTrack_line2(nextLine[18]);
                    counter++;

                    if (counter < line)
                        obj.setTrack_line3(nextLine[19]);
                    counter++;

                    if (counter < line)
                        obj.setTrack_line4(nextLine[20]);
                    counter++;

                    if (counter < line)
                        obj.setTrack_line5(nextLine[21]);
                    counter++;

                    if (counter < line)
                        obj.setTrack_line6(nextLine[22]);
                    counter++;

                    if (counter < line)
                        obj.setTrack_line7(nextLine[23]);
                    counter++;

                    if (counter < line)
                        obj.setTrack_line8(nextLine[24]);

                    ctr++;
                    session.save(obj);


                }
            } catch (Exception ex) {
                ex.printStackTrace();
                result = "<font color='red'>an error has occured while reading file.</font>";
            }

            session.getTransaction().commit();
            result = "<ul><li><strong>" + ctr + "</strong> Records added in database</li>";
            session.clear();
            // session.close();


        } catch (FileNotFoundException e) {
            result = "<font color='red'>Cannot find CASE_TRACKER.CSV file.</font>";
            e.printStackTrace();
        }


        request.setAttribute("msg", result);

        return mapping.findForward("success");
    }
}