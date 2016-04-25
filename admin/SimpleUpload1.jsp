<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%@ page language="java" import="javazoom.upload.*,java.util.*" %>
<%@ page errorPage="ExceptionHandler.jsp" %>
<%
if (request.getSession(true).getAttribute("UserName") == null)
{
%>

<jsp:forward page="error.jsp"/>

<%
}
%>

<% String path = application.getRealPath("/HMCSFormUpload/"); %>


<jsp:useBean id="upBean" scope="page" class="javazoom.upload.UploadBean" >
	<jsp:setProperty name="upBean" property="folderstore" value="<%= path %>"  />
	<jsp:setProperty name="upBean" property="overwrite" value="true" />
	<jsp:setProperty name="upBean" property="whitelist" value="*.txt,*.csv,*.xml" />
</jsp:useBean>


<!DOCTYPE html>
<!--[if lt IE 9]><html class="lte-ie8" lang="en"><![endif]-->
<!--[if gt IE 8]><!--><html lang="en"><!--<![endif]-->
  <head>
    <jsp:include page="../includes/head.jsp" />
  </head>

  <body>
    <script type="text/javascript">document.body.className = ((document.body.className) ? document.body.className + ' js-enabled' : 'js-enabled');</script>

    <jsp:include page="../includes/cookie.jsp" />

    <jsp:include page="../includes/banner-public.jsp" />

    <!-- Page content goes here -->
    <main id="content" role="main">
			<div class="holder">
				<!-- InstanceBeginEditable name="main" -->
				<h2>Case Tracker Administration</h2>

				<form action="simpleUpload.do" method="post" enctype="multipart/form-data" id="upform">
					<p>Select a file to upload :</p>
					<p><input type="file" name="uploadfile" size="50" /></p>
					<p>
						<input type="hidden" name="todo" value="upload" />&nbsp;
						<input type="submit" name="Submit" value="Upload Form" />&nbsp;
						<input type="reset" name="Reset" value="Cancel" />
					</p>
				</form>

				<ul>
				<%
							if (MultipartFormDataRequest.isMultipartFormData(request))
							{
								 // Uses MultipartFormDataRequest to parse the HTTP request.
								 MultipartFormDataRequest mrequest = new MultipartFormDataRequest(request);
								 String todo = null;
								 if (mrequest != null) todo = mrequest.getParameter("todo");
					 if ( (todo != null) && (todo.equalsIgnoreCase("upload")) )
					 {
												Hashtable files = mrequest.getFiles();
												if ( (files != null) && (!files.isEmpty()) )
												{
														UploadFile file = (UploadFile) files.get("uploadfile");
													 file.setFileName( file.getFileName().toUpperCase());
														if (file != null) out.println("<p><strong>Uploaded file :</strong> "+file.getFileName()+"</p>");
														// Uses the bean now to store specified by jsp:setProperty at the top.
														upBean.store(mrequest, "uploadfile");

												}
												else
												{
													out.println("<p>No uploaded files</p>");
												}
					 }
								 else out.println("<br /> todo="+todo);
							}
				%>
			</ul>

			<br />
			<%
			session.setMaxInactiveInterval(3600);
			%>

			<p><html:link href="dumpData.do">Click here to add data in database</html:link></p>
			<logic:present name="msg">
				<%=request.getAttribute("msg") %>
			</logic:present>

			<p><a href="invalidate.do">Logout</a></p>

			</div>

    </main>

    <jsp:include page="../includes/footer.jsp" />

    <div id="global-app-error" class="app-error hidden"></div>

    <script src="assets/javascripts/govuk-template.js" type="text/javascript"></script>

  </body>
</html>
