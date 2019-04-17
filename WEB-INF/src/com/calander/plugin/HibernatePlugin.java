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
        implements PlugIn {

    private Configuration config;
    private SessionFactory factory;
    private static Class myClass;
    public static final String KEY_NAME;

    public HibernatePlugin() {
    }

    public void init(ActionServlet servlet, ModuleConfig modConfig)
            throws ServletException {
        try {
            String dbHost = System.getenv("DB_HOST");
            String dbPort = System.getenv("DB_PORT");
            String dbUser = System.getenv("DB_USER");
            String dbPass = System.getenv("DB_PASSWORD");
            String dbName = System.getenv("DB_NAME");

            String urlString = String.format("jdbc:postgresql://%s:%s/%s", dbHost, dbPort, dbName);

            Configuration cfg = new Configuration();
            cfg.addResource("/hibernate.cfg.xml");
            cfg.setProperty("hibernate.connection.url", urlString);
            cfg.setProperty("hibernate.connection.username", dbUser);
            cfg.setProperty("hibernate.connection.password", dbPass);
            cfg.configure();

            factory = cfg.buildSessionFactory();
            servlet.getServletContext().setAttribute(KEY_NAME, factory);
        } catch (Exception e) {
            System.out.println((new StringBuilder("Could not build Hibernate config: ")).append(e.getMessage()).toString());
            e.printStackTrace();
        }
    }

    public void destroy() {
        try {
            factory.close();
        } catch (HibernateException hibernateexception) {
        }
    }

    static {
        myClass = HibernatePlugin.class;
        KEY_NAME = myClass.getName();
    }
}
