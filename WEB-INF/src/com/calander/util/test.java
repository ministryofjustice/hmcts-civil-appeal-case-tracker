package com.calander.util;


import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.ServletContext;

import org.quartz.*;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.hibernate.SessionFactory;

import com.calander.actions.dumpDatabase;
import com.calander.plugin.HibernatePlugin;

//import com.MOJICT.IACFee.Actions.RetrieveAction;
//import com.MOJICT.IACFee.Util.LastSubmission;


public class test implements Job
{
	
 	 
   
    public void execute(JobExecutionContext arg0)
        throws JobExecutionException
    {
    	
    	
       
        try
        {System.out.println("coing here in shcedular");
        	ServletContext servletContext = (ServletContext) arg0.getMergedJobDataMap().get("servletContext");
        	System.out.println("calling dump dtabases");
        	GetObject2 getobj=new GetObject2();
        	getobj.getReaderobj();
        	//dumpDatabase dumpdb=new dumpDatabase();
		//dumpdb.runscheduler();
        	
        	
         }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    private Object getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String args[]) throws Exception{
	    
		//dumpDatabase dumpdb=new dumpDatabase();
	//dumpdb.runscheduler();
	}

}
