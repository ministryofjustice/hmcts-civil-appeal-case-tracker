<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
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

    <title>Bot Activity Dashboard</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f4f4f4;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
        }
        .dashboard-header {
            text-align: center;
            margin-bottom: 20px;
        }
        .iframe-container {
            position: relative;
            overflow: hidden;
            width: 100%;
            padding-top: 56.25%; /* 16:9 Aspect Ratio */
            border: 1px solid #ddd;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .iframe-container iframe {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            border: none;
        }
        .error-message {
            color: red;
            text-align: center;
            display: none;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="dashboard-header">
            <h1>Case Tracker Activity Dashboard</h1>
            <p>OpenSearch Dashboard for monitoring bot-related log activity</p>
        </div>
        <div class="iframe-container">
            <iframe src="https://app-logs.cloud-platform.service.justice.gov.uk/_dashboards/app/dashboards?security_tenant=global#/view/d1f29980-51ab-11f0-9e1d-5f86861f42b7?embed=true&_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:now-7d,to:now))&_a=(description:'',filters:!(),fullScreenMode:!f,options:(hidePanelTitles:!f,useMargins:!t),query:(language:kuery,query:''),timeRestore:!t,title:Case-Tracker-Prod,viewMode:view)&show-time-filter=true&hide-filter-bar=true" height="600" width="800"></iframe>
        </div>
        <div class="error-message" id="errorMessage">
            Failed to load the dashboard. Please check the URL or try again later.
        </div>
    </div>

    <script>
        // Basic error handling for iframe loading
        window.addEventListener('load', function() {
            var iframe = document.querySelector('iframe');
            var errorMessage = document.getElementById('errorMessage');
            iframe.onerror = function() {
                errorMessage.style.display = 'block';
            };
            iframe.onload = function() {
                errorMessage.style.display = 'none';
            };
        });
    </script>
</body>
</html>