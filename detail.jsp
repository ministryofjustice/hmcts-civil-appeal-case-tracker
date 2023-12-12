<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>

<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<!-- InstanceBegin template="/Templates/default.dwt" codeOutsideHTMLIsLocked="false" -->
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
                                <h2>Summary Case Details for <logic:present name="case" scope="request"><bean:write
                                        name="case"/></logic:present></h2>
                            </div>

                            <div class="formwrap">
                                <span class="tl"></span>
                                <span class="tr"><span></span></span>
                                <div class="formcon">

                                    <logic:present name="detail" scope="request">
                                    <div class="formblock">
                                        <h3><span><strong>Case</strong></span></h3>
                                        <div class="block">
                                            <div class="row">
                                                <p><strong>Reference:</strong>&nbsp;<bean:write name="detail"
                                                                                                property="case_ref"/>
                                                </p>
                                                <p><strong>Title:</strong>&nbsp;<bean:write name="detail"
                                                                                            property="title1"/></p>
                                                <p><strong>Type:</strong>&nbsp;<bean:write name="detail"
                                                                                           property="type"/></p>
                                                <p>
                                                    <strong>Appeal / Application:</strong><br/>
                                                    <bean:write name="detail" property="lc_judge"/><br/>
                                                    <bean:write name="detail" property="lcourt"/><br/>
                                                    <bean:write name="detail" property="nature"/>
                                                </p>
                                                <p><strong>Hearing Status:</strong>&nbsp;<bean:write name="detail"
                                                                                                     property="heading_status"/>
                                                </p>
                                                <p><strong>Venue:</strong>&nbsp;<bean:write name="detail"
                                                                                            property="venue"/></p>
                                                <p>
                                                    <strong>Constitution:</strong><br/>
                                                    <bean:write name="detail" property="judge1"/><br/>
                                                    <bean:write name="detail" property="judge2"/><br/>
                                                    <bean:write name="detail" property="judge3"/>
                                                </p>
                                            </div>
                                        </div>

                                        <div class="formblock">
                                            <h3>Case results:</h3>
                                            <p><bean:write name="detail" property="result"/></p>
                                        </div>

                                        <div class="formblock">
                                            <h3>Track Your Case:</h3>
                                            <p><strong>Current Status:</strong>&nbsp;<bean:write name="detail"
                                                                                                 property="status"/></p>
                                        </div>

                                        <div class="formblock underline">
                                            <h3>Tracking Information:</h3>
                                            <p>
                                                <bean:write name="detail" property="track_line1"/><br/>
                                                <bean:write name="detail" property="track_line2"/><br/>
                                                <bean:write name="detail" property="track_line3"/><br/>
                                                <bean:write name="detail" property="track_line4"/><br/>
                                                <bean:write name="detail" property="track_line5"/><br/>
                                                <bean:write name="detail" property="track_line6"/><br/>
                                                <bean:write name="detail" property="track_line7"/><br/>
                                                <bean:write name="detail" property="track_line8"/>
                                            </p>
                                        </div>

                                        <p><strong>Last Updated:</strong>&nbsp;<bean:write name="detail"
                                                                                           property="last_updated"/></p>

                                        </logic:present>
                                    </div>
                                </div>

                                <div class="submitc">
                                    <div class="function next right">
                                        <span class="tl"></span>
                                        <span class="tr"><span></span></span>
                                        <a href="#" onclick="goBack()">Search results<span class="access"></span></a>
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
                    <li><a href="https://www.justice.gov.uk/privacy">Your privacy</a></li>
                    <li><a href="https://www.justice.gov.uk/copyright">&copy; Crown copyright</a></li>
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
<script>
    function goBack() {
        window.history.back();
    }
</script>
<noscript>
    <div>
        <img src='http://directgov.stcllctrs.com/OWCPZPDTRUV/noScript.bmp' alt="Scipt is not enabled"/>
    </div>
</noscript>
<!--END_EXCLUDE-->
</body>
<!-- InstanceEnd --></html>