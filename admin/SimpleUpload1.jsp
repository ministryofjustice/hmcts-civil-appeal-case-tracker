<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%@ page language="java" import="javazoom.upload.*,java.util.*" %>
<%@ page errorPage="ExceptionHandler.jsp" %>
<%
    if (request.getSession(true).getAttribute("UserName") == null) {
%>

<jsp:forward page="error.jsp"/>

<%
    }
%>

<%
    String path = application.getRealPath("/HMCSFormUpload/");
%>


<jsp:useBean id="upBean" scope="page" class="javazoom.upload.UploadBean">
    <jsp:setProperty name="upBean" property="folderstore" value="<%= path %>"/>
    <jsp:setProperty name="upBean" property="overwrite" value="true"/>
    <jsp:setProperty name="upBean" property="whitelist" value="*.txt,*.csv,*.xml"/>
</jsp:useBean>

<?xml version="1.0" encoding="utf-8"?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en"><!-- InstanceBegin template="/Templates/default.dwt" codeOutsideHTMLIsLocked="false" -->
<head>
    <!-- InstanceBeginEditable name="doctitle" -->
    <title>Case tracker &ndash; Justice UK</title>
    <!-- InstanceEndEditable -->

    <!-- InstanceBeginEditable name="metadata" -->
    <meta name="DC.title" content="Publications - Ministry of Justice"/>
    <meta name="revisit-after" content="1 days"/>
    <meta name="Author" content="Ministry of Justice"/>
    <meta name="Keywords" content="justice, courts, prisons, probation,"/>
    <meta name="Description" content="Latest publications from across the organisation"/>

    <meta name="eGMS.accessibility" scheme="WCAG" content="Double-A"/>
    <meta name="eGMS.subject.category" scheme="GCL" content="Crime, Law, Justice and Rights"/>

    <meta name="DC.subject.keyword" content="justice, courts, prisons, probation,"/>
    <meta name="DC.description" content="Latest publications from across the organisation"/>
    <meta name="DC.creator" content="Ministry of Justice, Communications Directorate, Web team, Internet manager"/>
    <meta name="DC.contributor" content="Ministry of Justice"/>
    <meta name="DC.identifier" content="/index.jsp"/>
    <meta name="DC.date.created" content="2011-02-21"/>
    <meta name="DC.date.modified" content="2011-02-21"/>
    <meta name="DC.publisher" content="Ministry of Justice, 102 Petty France, London SW1H 9AJ"/>
    <meta name="DC.format" content="Text/HTML"/>
    <meta name="DC.language" content="ENG"/>
    <meta name="DC.coverage" content="England, Wales"/>
    <meta name="DC.rights.copyright" content="Crown Copyright"/>
    <!-- InstanceEndEditable -->

    <link rel="shortcut icon" href="favicon.ico"/>
    <link rel="stylesheet" type="text/css" href="asset/css/dg.css"/>

    <!--[if lte IE 7]>
    <link href="asset/css/dgie.css" rel="stylesheet" type="text/css" media="screen"/>
    <![endif]-->
    <!--[if IE 6]>
    <link href="asset/css/dgie6.css" rel="stylesheet" type="text/css" media="screen"/>
    <![endif]-->

    <link rel="alternate stylesheet" type="text/css" href="asset/css/standard.css" title="Standard"/>
    <link rel="alternate stylesheet" type="text/css" href="asset/css/larger.css" title="Larger"/>
    <link rel="alternate stylesheet" type="text/css" href="asset/css/largest.css" title="Largest"/>
    <link rel="stylesheet" type="text/css" href="asset/css/complex_table.css"/>

    <script language="JavaScript" type="text/javascript" src="js/cal.js"></script>
    <script language="JavaScript" type="text/javascript" src="js/sort.js"></script>
    <script language="JavaScript" type="text/javascript" src="js/jquery.js"></script>
    <script language="JavaScript" type="text/javascript" src="js/dg.js"></script>
    <script language="JavaScript" type="text/javascript" src="js/complextable.js"></script>
    <script language="JavaScript" type="text/javascript" src="js/print.js"></script>
    <script language="JavaScript" type="text/javascript" src="js/general.js"></script>
</head>

<body>
<a name="top" id="top"></a>
<div class="iewrapper">
    <div class="wrapper">
        <a href="#Heading" class="access">Go to main content</a>
        <div class="printlogo">
            <img src="images/printlogo.gif" alt="Justice UK logo"/>
        </div>
        <div class="innerwrapper">
            <div class="onecolfloat">
                <div class="onecolblock">
                    <div id="Section">
                        <div id="TextSize">
                            <a href="http://www.direct.gov.uk/en/Hl1/Help/Accessibility/index.htm" class="nojs">Resize
                                text help</a>
                        </div>
                        <div id="Heading">
                            <h1>Case Tracker for Civil Appeals</h1>
                        </div>
                    </div>
                    <div id="Content">
                        <div class="holder">
                            <!-- InstanceBeginEditable name="main" -->
                            <h2>Case Tracker Administration</h2>

                            <form action="simpleUpload.do" method="post" enctype="multipart/form-data" id="upform">
                                <p>Select a file to upload :</p>
                                <p><input type="file" name="uploadfile" size="50"/></p>
                                <p>
                                    <input type="hidden" name="todo" value="upload"/>&nbsp;
                                    <input type="submit" name="Submit" value="Upload Form"/>&nbsp;
                                    <input type="reset" name="Reset" value="Cancel"/>
                                </p>
                            </form>

                            <ul>
                                <%
                                    if (MultipartFormDataRequest.isMultipartFormData(request)) {
                                        // Uses MultipartFormDataRequest to parse the HTTP request.
                                        MultipartFormDataRequest mrequest = new MultipartFormDataRequest(request);
                                        String todo = null;
                                        if (mrequest != null) todo = mrequest.getParameter("todo");
                                        if ((todo != null) && (todo.equalsIgnoreCase("upload"))) {
                                            Hashtable files = mrequest.getFiles();
                                            if ((files != null) && (!files.isEmpty())) {
                                                UploadFile file = (UploadFile) files.get("uploadfile");
                                                file.setFileName(file.getFileName().toUpperCase());
                                                if (file != null)
                                                    out.println("<p><strong>Uploaded file :</strong> " + file.getFileName() + "</p>");
                                                // Uses the bean now to store specified by jsp:setProperty at the top.
                                                upBean.store(mrequest, "uploadfile");

                                            } else {
                                                out.println("<p>No uploaded files</p>");
                                            }
                                        } else out.println("<br /> todo=" + todo);
                                    }
                                %>
                            </ul>

                            <br/>
                            <%
                                session.setMaxInactiveInterval(3600);
                            %>

                            <p><html:link href="dumpData.do">Click here to add data in database</html:link></p>
                            <logic:present name="msg">
                                <%=request.getAttribute("msg") %>
                            </logic:present>

                            <p><a href="invalidate.do">Logout</a></p>
                            <!-- InstanceEndEditable -->
                        </div>

                    </div>
                </div>
            </div>

        </div>

        <div id="Footer">
            <div class="holder">
                <ul>
                    <li><a href="https://www.justice.gov.uk/privacy">Your privacy</a></li>
                    <li><a href="https://www.justice.gov.uk/copyright">&copy; Crown copyright</a></li>
                </ul>
            </div>
        </div>

        <!-- Include the Top Bar here -->
        <div id="TopBar">
            <div class="holder">
                <span class="tl"></span><span class="tr"><span></span></span>
                <a href="http://www.justice.gov.uk/index.htm" tabindex="1"><img src="images/logo.png"
                                                                                alt="Link to the Justice homepage"/></a>
            </div>
        </div>
        <!-- End of Top Bar -->

    </div>
</div>
<!--BEGIN_EXCLUDE-->
<script type="text/javascript" src="/dg_scripts/ProphetInsert.js"></script>
<noscript>
    <div>
        <img src='http://directgov.stcllctrs.com/OWCPZPDTRUV/noScript.bmp' alt="Scipt is not enabled"/>
    </div>
</noscript>
<!--END_EXCLUDE-->
</body>
<!-- InstanceEnd --></html>