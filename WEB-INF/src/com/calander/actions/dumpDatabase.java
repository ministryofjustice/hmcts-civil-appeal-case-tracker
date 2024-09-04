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

        System.out.println(HibernatePlugin.KEY_NAME);

        //getting session object from Hibernate Util class
        HibernatePlugin hp=new HibernatePlugin();
        SessionFactory factory = (SessionFactory) hp.getconnection();
        Session session = factory.openSession();

        ServletContext context = servlet.getServletContext();

        String FILE_PATH = context.getRealPath("/HMCSFormUpload/CASE_TRACKER.CSV");

        String result = null;

        try {
        	

        	CSVReader reader = new CSVReader(new FileReader(FILE_PATH));


            String[] nextLine;
            Calander calander = null;
		
            session.beginTransaction();
            session.createQuery("delete Calander").executeUpdate();
//            session.getTransaction().commit();
//
//            session.beginTransaction();

            int rows = 0;

            try {
                while ((nextLine = reader.readNext()) != null) {

                    int line_length = nextLine.length;
                    int column = 0;

                    calander = new Calander();

                    if(nextLine[0].length() > 50) {
                        System.out.println("******** SearchDate > 50 <" + nextLine[0] + "> for Case No <" + nextLine[1] + ">");
                    }
                    if(nextLine[1].length() > 50) {
                        System.out.println("******** CaseNo > 50 <" + nextLine[1] + ">");
                    }
                    if(nextLine[8].length() > 50) {
                        System.out.println("******** CaseRef > 50 <" + nextLine[8] + "> for Case No <" + nextLine[1] + ">");
                    }
                    if(nextLine[14].length() > 50) {
                        System.out.println("******** LastUpdated > 50 <" + nextLine[14] + "> for Case No <" + nextLine[1] + ">");
                    }

                    if (column < line_length)
                        calander.setSearch_date(nextLine[0]);
                    column++;

                    if (column < line_length)
                        calander.setCase_no(nextLine[1]);
                    column++;

                    if (column < line_length)
                        calander.setHeading_status(nextLine[2]);
                    column++;

                    if (column < line_length)
                        calander.setJudge1(nextLine[3]);
                    column++;

                    if (column < line_length)
                        calander.setJudge2(nextLine[4]);
                    column++;

                    if (column < line_length)
                        calander.setJudge3(nextLine[5]);
                    column++;

                    if (column < line_length)
                        calander.setLcourt(nextLine[6]);
                    column++;

                    if (column < line_length)
                        calander.setVenue(nextLine[7]);
                    column++;

                    if (column < line_length)
                        calander.setCase_ref(nextLine[8]);
                    column++;

                    if (column < line_length)
                        calander.setTitle1(nextLine[9] + " " + nextLine[10]);
                    column++;

                    if (column < line_length)
                        calander.setTitle2(nextLine[10]);
                    column++;

                    if (column < line_length)
                        calander.setType(nextLine[11]);
                    column++;

                    if (column < line_length)
                        calander.setLc_judge(nextLine[12]);
                    column++;

                    if (column < line_length)
                        calander.setNature(nextLine[13]);
                    column++;

                    if (column < line_length)
                        calander.setLast_updated(nextLine[14]);
                    column++;

                    if (column < line_length)
                        calander.setResult(nextLine[15]);
                    column++;

                    if (column < line_length)
                        calander.setStatus(nextLine[16]);
                    column++;

                    if (column < line_length)
                        calander.setTrack_line1(nextLine[17]);
                    column++;

                    if (column < line_length)
                        calander.setTrack_line2(nextLine[18]);
                    column++;

                    if (column < line_length)
                        calander.setTrack_line3(nextLine[19]);
                    column++;

                    if (column < line_length)
                        calander.setTrack_line4(nextLine[20]);
                    column++;

                    if (column < line_length)
                        calander.setTrack_line5(nextLine[21]);
                    column++;

                    if (column < line_length)
                        calander.setTrack_line6(nextLine[22]);
                    column++;

                    if (column < line_length)
                        calander.setTrack_line7(nextLine[23]);
                    column++;

                    if (column < line_length)
                        calander.setTrack_line8(nextLine[24]);

                    rows++;
                    session.save(calander);
                }

                session.getTransaction().commit();
                result = "<ul><li><strong>" + rows + "</strong> Records added in database</li>";

            } catch (Exception ex) {
                session.getTransaction().rollback();
                ex.printStackTrace();
                result = "<font color='red'>an error has occured while reading file.</font>";
            }

            session.clear();
            session.close();

        } catch (FileNotFoundException e) {
            result = "<font color='red'>Cannot find CASE_TRACKER.CSV file.</font>";
            e.printStackTrace();
            session.clear();
            session.close();
        }

        request.setAttribute("msg", result);

        return mapping.findForward("success");
    }
    
  
}
