package com.calander.plugin;


import java.io.PrintStream;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernatePlugin
    implements PlugIn
{

    private Configuration config;
    private SessionFactory factory;
    private static Class clazz;
    public static String KEY_NAME;

    public HibernatePlugin()
    {
    }

    public void init(ActionServlet servlet, ModuleConfig modConfig)
        throws ServletException
    {
        try
        {
        	 Configuration cfg = new Configuration();
             cfg.addResource("/hibernate.cfg.xml");
             cfg.configure();
             factory = cfg.buildSessionFactory();
             factory = (new Configuration()).configure().buildSessionFactory();
             servlet.getServletContext().setAttribute(KEY_NAME, factory);
         }
        catch(Exception e)
        {
            System.out.println((new StringBuilder("Exception 11111>>>>>>>>>>>>>>>>")).append(e.getMessage()).toString());
            e.printStackTrace();
        }
    }

    public void destroy()
    {
        try
        {
            factory.close();
        }
        catch(HibernateException hibernateexception) { }
    }

    static 
    {
       clazz=HibernatePlugin.class;
        KEY_NAME = clazz.getName();
    }
}
