<%@ page errorPage="ExceptionHandler.jsp" %>
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
                            <%
                                String userid = request.getParameter("userid");
                                String password = request.getParameter("password");
                                String adminUser = System.getenv("ADMIN_USER");
                                String adminPass = System.getenv("ADMIN_PASS");

                                if ((userid.equals(adminUser)) && (password.equals(adminPass))) {
                                    session.setAttribute("UserName", userid);
                            %>
                            <jsp:forward page="SimpleUpload1.jsp">
                                <jsp:param name="id" value="<%= userid %>"/>
                            </jsp:forward>

                            <%
                            } else {
                            %>
                            <jsp:forward page="error.jsp"/>
                            <%
                                }
                            %>
                            <!-- InstanceEndEditable -->
                        </div>

                    </div>
                </div>
            </div>

        </div>

        <div id="Footer">
            <div class="holder">
                <ul>
                    <li><a href="http://www.justice.gov.uk/about/justice/index.htm" class="first">About Justice</a></li>
                    <li><a href="http://www.directgov.gov.uk">Directgov</a></li>
                    <li><a href="http://www.justice.gov.uk/global/privacy/index.htm">Your privacy</a></li>
                    <li><a href="http://www.justice.gov.uk/global/copyright/index.htm">&copy; Crown copyright</a></li>
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
<!-- InstanceEnd -->
</html>