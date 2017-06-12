<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ page import="java.sql.Date" %>


<!DOCTYPE html>
<!--[if lt IE 9]><html class="lte-ie8" lang="en"><![endif]-->
<!--[if gt IE 8]><!--><html lang="en"><!--<![endif]-->
<head>
    <jsp:include page="includes/head.jsp"/>
</head>

<body>
<script type="text/javascript">document.body.className = ((document.body.className) ? document.body.className + ' js-enabled' : 'js-enabled');</script>

<jsp:include page="includes/cookie.jsp"/>

<jsp:include page="includes/banner-public.jsp"/>

<!-- Page content goes here -->
<main id="content" role="main">
    <div class="steps">
        <h2>Ways to Search</h2>
    </div>

    <p>Click the &quot;Calendar&quot; icon below to enter a date or enter a "Case number" or "Title", and click the
        search button</p>

    <div class="formwrap">
        <span class="tl"></span>
        <span class="tr"><span></span></span>
        <div class="formcon">

            <form name="frmSubmit" id="frmSubmit" action="search.do"><br/>
                <p>
                    <input type="text" id="search" name="search" size="20" value=""/>
                    <a href="Javascript:ShowCalendar('document.frmSubmit.search',2008,2030,'dd/mmm/yy');">
                        <img src="images/cal.jpg" alt="calendar" width="65" height="16" border="0"/>
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
                    <display:table id="result" name="sessionScope.results" requestURI="" pagesize="15" sort="list">
                        <display:setProperty name="paging.banner.placement">top</display:setProperty>
                        <display:column property="case_no" title="Case number" paramId="case_id" paramProperty="case_no"
                                        href="getDetail.do"/>
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

</main>

<jsp:include page="includes/footer.jsp"/>

<div id="global-app-error" class="app-error hidden"></div>

<script src="assets/javascripts/govuk-template.js" type="text/javascript"></script>

</body>
</html>
