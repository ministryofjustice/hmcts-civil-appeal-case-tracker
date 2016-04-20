package com.calander.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.calander.beans.*;

import com.calander.plugin.HibernatePlugin;

public class caseDetailAction extends Action {

		 public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	     throws IOException, ServletException, Exception
	 {

				String case_id;
				case_id= request.getParameter("case_id");
		
				Session session = null;
				SessionFactory factory=null;
				
				//getting session object from Hibernate Util class	
				factory = (SessionFactory) servlet.getServletContext().getAttribute(HibernatePlugin.KEY_NAME);
				session = factory.openSession();
		
				Query qry=null;
				
				qry = session.createQuery("from Calander c where c.case_no=:case");
				qry.setString("case",case_id);
				
				Calander obj = (Calander) qry.list().get(0);
				
				System.out.println(obj.getCase_no());
				request.setAttribute("detail",obj);
				request.setAttribute("case",case_id);
				
				session.close();
			 return mapping.findForward("success");
	 }
		 
}
