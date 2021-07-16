package com.calander.util;



import au.com.bytecode.opencsv.CSVReader;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.calander.beans.Calander;
import com.calander.plugin.HibernatePlugin;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class GetObject2 {
   public static void main(String args[]) throws Exception{
	   //GetObject2 gettobj=new GetObject2();
	  //gettobj.getReaderobj();
	   wgetobject();
   }
    public void getReaderobj() throws IOException {
      	
          try {
            
        	
            wgetobject();
          
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        } 
        catch(Exception e)
        {
        	e.printStackTrace();
        }finally {
            // To ensure that the network connection doesn't remain open, close any open input streams.
           
          
        }
		//return reader;
    }

    private static Reader displayTextInputStream(InputStream input) throws IOException {
        // Read the text input stream one line at a time and display each line.
        
	 BufferedReader reader=null;
	 BufferedWriter writer=null;
	String line;
	try {
			reader = new BufferedReader(new InputStreamReader(input));
		/*System.out.println("writting to file");
			 writer = new BufferedWriter(new FileWriter(new File("opt/data.csv")));
			 System.out.println("writting to file2");
		   // while ((line = reader.readLine()) != null) {
		        //doSomethingWith(line);
		        writer.write(line);
		        // must do this: .readLine() will have stripped line endings
		        writer.newLine();
		    }
	  writer.close();
	  System.out.println("writting to file doneeeeeeeeeeeeeeeeeeeeeeeeeeee");
	  */
	}
       // BufferedReader reader1 = new BufferedReader(new InputStreamReader(input));
        catch(Exception e)
        {
		//writer.close();
		e.printStackTrace();
        }
        return reader;
    }
    public static void runscheduler(InputStream input) throws Exception
    {
    	System.out.println("coming here in run schedular");

        //getting session object from Hibernate Util class
    	HibernatePlugin hp=new HibernatePlugin();
        SessionFactory factory = (SessionFactory) hp.getconnection();
        Session session = factory.openSession();
        
        //ServletContext context = servlet.getServletContext();
        //String FILE_PATH = context.getRealPath("/HMCSFormUpload/CASE_TRACKER.CSV");
       String result = null;

        try {
        	
/*		CSVReader reader = new CSVReader(new FileReader("opt/data.csv"));
            String[] nextLine;
            Calander calander = null;
*/
        	 BufferedReader reader1=null;
        	 BufferedWriter writer=null;
        	String line;
        	
        			reader1 = new BufferedReader(new InputStreamReader(input));
        	
            CSVReader reader = new CSVReader(reader1);
            //CSVReader reader1 = new CSVReader(getobj.getReaderobj());
            System.out.println("coming in run schedular>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            String[] nextLine;
            Calander calander = null;


            session.beginTransaction();
            session.createQuery("delete Calander").executeUpdate();
            session.getTransaction().commit();

            session.beginTransaction();

            int rows = 0;

            
                while ((nextLine = reader.readNext()) != null) {

                    int line_length = nextLine.length;
                    int column = 0;

                    calander = new Calander();

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
                
                }}
        catch (Exception ex) {
                ex.printStackTrace();
                result = "<font color='red'>an error has occured while reading file.</font>";
            }

            session.getTransaction().commit();
            result = "<ul><li><strong> </strong> Records added in database</li>";
            session.clear();
            session.close();

        

        //request.setAttribute("msg", result);

       // return mapping.findForward("success");
    }
    
    public static InputStream wgetobject() throws Exception
    {
    	
    	HibernatePlugin hp=new HibernatePlugin();
        SessionFactory factory = (SessionFactory) hp.getconnection();
        Session session = factory.openSession();
        String result = null;

        String url = "https://case-tracker.s3.eu-west-2.amazonaws.com/data.csv";

        URL u;
        InputStream is = null;
        DataInputStream dis;
        String s;
        Random random = new Random();

     // generate a random integer from 0 to 899, then add 100
          int x = (random.nextInt(300) + 100)/60;
          System.out.println("sleeping for x minutes"+x);
          TimeUnit.MINUTES.sleep(x);
		try
        {
			
	        //ServletContext context = servlet.getServletContext();
	        //String FILE_PATH = context.getRealPath("/HMCSFormUpload/CASE_TRACKER.CSV");
	       
          u = new URL(url);
          is = u.openStream();
          BufferedReader reader1=null;
     	 BufferedWriter writer=null;
     	String line;
     	
     			reader1 = new BufferedReader(new InputStreamReader(is));
     	
         CSVReader reader = new CSVReader(reader1);
         //CSVReader reader1 = new CSVReader(getobj.getReaderobj());
         System.out.println("coming in run schedular>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
         String[] nextLine;
         Calander calander = null;


         session.beginTransaction();
         session.createQuery("delete Calander").executeUpdate();
         session.getTransaction().commit();

         session.beginTransaction();

         int rows = 0;

         
             while ((nextLine = reader.readNext()) != null) {

                 int line_length = nextLine.length;
                 int column = 0;

                 calander = new Calander();

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
             
             }}
     catch (Exception ex) {
             ex.printStackTrace();
             result = "<font color='red'>an error has occured while reading file.</font>";
         }
		
         session.getTransaction().commit();
         result = "<ul><li><strong>rows </strong> Records added in database</li>";
         session.clear();
         session.close();

        
          return is;
        
    }

}



