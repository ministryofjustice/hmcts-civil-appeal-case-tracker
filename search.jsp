<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ page import="java.sql.Date" %>

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
    <link href="../css/dgie.css" rel="stylesheet" type="text/css" media="screen"/>
    <![endif]-->
    <!--[if IE 6]>
    <link href="../css/dgie6.css" rel="stylesheet" type="text/css" media="screen"/>
    <![endif]-->

    <link rel="alternate stylesheet" type="text/css" href="asset/css/standard.css" title="Standard"/>
    <link rel="alternate stylesheet" type="text/css" href="asset/css/larger.css" title="Larger"/>
    <link rel="alternate stylesheet" type="text/css" href="asset/css/largest.css" title="Largest"/>
    <link rel="stylesheet" type="text/css" href="asset/css/complex_table.css"/>

    <link rel="Shortcut Icon" href="/favicon.ico" type="image/x-icon"/>

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
                            <div class="steps">
                                <h2>Ways to Search</h2>
                            </div>

                            <p>Click the &quot;Calendar&quot; icon below to enter a date or enter a "Case number" or
                                "Title", and click the search button</p>

                            <div class="formwrap">
                                <span class="tl"></span>
                                <span class="tr"><span></span></span>
                                <div class="formcon">

                                    <form name="frmSubmit" id="frmSubmit" action="search.do"><br/>
                                        <p>
                                            <input type="text" id="search" name="search" size="20" value=""/>
                                            <a href="Javascript:ShowCalendar('document.frmSubmit.search',2008,2030,'dd/mmm/yyyy');">
                                                <img src="images/cal.jpg" alt="calendar" width="65" height="16"
                                                     border="0"/>
                                            </a>
                                        </p>
                                        <p><input type="submit" value="Search"/></p>
                                    </form>
                                </div>
                            </div>

                            <logic:present name="results" scope="session">
                                <div class="formwrap">
                                    <span class="tl"></span>
                                    <span class="tr"><span></span></span>
                                    <div class="formcon">

                                        <h2>Search results</h2>
                                        <div class="result">
                                            <display:table id="result" name="sessionScope.results" requestURI=""
                                                           pagesize="15" sort="list">
                                                <display:setProperty
                                                        name="paging.banner.placement">top</display:setProperty>
                                                <display:column property="case_no" title="Case number" paramId="case_id"
                                                                paramProperty="case_no" href="getDetail.do"/>
                                                <display:column property="title1" title="Title"/>
                                            </display:table>
                                        </div>

                                    </div>
                                </div>
                            </logic:present>

                            <div class="submitc">
                                <div class="function previous">
                                    <span class="tl"></span>
                                    <span class="tr"><span></span></span>
                                    <a href="index.jsp">Back<span class="access"></span></a>
                                    <span class="bl"></span>
                                    <span class="br"></span>
                                </div>
                            </div>
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
                <a href="#" tabindex="1"><img src="images/logo.png" alt="Link to Justice UK homepage"/></a>
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
<script>
    (function (i, s, o, g, r, a, m) {
        i['GoogleAnalyticsObject'] = r;
        i[r] = i[r] || function () {
                (i[r].q = i[r].q || []).push(arguments)
            }, i[r].l = 1 * new Date();
        a = s.createElement(o),
            m = s.getElementsByTagName(o)[0];
        a.async = 1;
        a.src = g;
        m.parentNode.insertBefore(a, m)
    })(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');
    ga('create', 'UA-37377084-50', 'auto');
    ga('send', 'pageview');
</script>
<!-- InstanceEnd --></html>
