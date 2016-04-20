<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>

<!DOCTYPE html>
<!--[if lt IE 9]><html class="lte-ie8" lang="en"><![endif]-->
<!--[if gt IE 8]><!--><html lang="en"><!--<![endif]-->
  <head>
    <jsp:include page="includes/head.jsp" />
  </head>

  <body>
    <script type="text/javascript">document.body.className = ((document.body.className) ? document.body.className + ' js-enabled' : 'js-enabled');</script>

    <jsp:include page="includes/cookie.jsp" />

    <jsp:include page="includes/banner-public.jsp" />

    <!-- Page content goes here -->
    <main id="content" role="main">
      <div class="steps">
        <h2>Summary Case Details for <logic:present name="case" scope="request"><bean:write name="case"/></logic:present></h2>
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
                <p><strong>Reference:</strong>&nbsp;<bean:write name="detail" property="case_ref" /></p>
                <p><strong>Title:</strong>&nbsp;<bean:write name="detail" property="title1" /></p>
                <p><strong>Type:</strong>&nbsp;<bean:write name="detail" property="type" /></p>
                <p>
                  <strong>Appeal / Application:</strong><br />
                  <bean:write name="detail" property="lc_judge" /><br />
                  <bean:write name="detail" property="lcourt" /><br />
                  <bean:write name="detail" property="nature" />
                </p>
                <p><strong>Hearing Status:</strong>&nbsp;<bean:write name="detail" property="heading_status" /></p>
                <p><strong>Venue:</strong>&nbsp;<bean:write name="detail" property="venue" /></p>
                <p>
                  <strong>Constitution:</strong><br />
                  <bean:write name="detail" property="judge1" /><br />
                  <bean:write name="detail" property="judge2" /><br />
                  <bean:write name="detail" property="judge3" />
                </p>
              </div>
            </div>

            <div class="formblock">
              <h3>Case results:</h3>
              <p><bean:write name="detail" property="result" /></p>
            </div>

            <div class="formblock">
              <h3>Track Your Case:</h3>
              <p><strong>Current Status:</strong>&nbsp;<bean:write name="detail" property="status" /></p>
            </div>

            <div class="formblock underline">
              <h3>Tracking Information:</h3>
              <p>
                <bean:write name="detail" property="track_line1" /><br />
                <bean:write name="detail" property="track_line2" /><br />
                <bean:write name="detail" property="track_line3" /><br />
                <bean:write name="detail" property="track_line4" /><br />
                <bean:write name="detail" property="track_line5" /><br />
                <bean:write name="detail" property="track_line6" /><br />
                <bean:write name="detail" property="track_line7" /><br />
                <bean:write name="detail" property="track_line8" />
              </p>
            </div>

            <p><strong>Last Updated:</strong>&nbsp;<bean:write name="detail" property="last_updated" /></p>

          </logic:present>
        </div>
      </div>

      <div class="submitc">
        <div class="function next right">
          <span class="tl"></span>
          <span class="tr"><span></span></span>
          <a href="search.jsp">Search again<span class="access"></span></a>
          <span class="bl"></span>
          <span class="br"></span>
        </div>
      </div>


    </main>

    <jsp:include page="includes/footer.jsp" />

    <div id="global-app-error" class="app-error hidden"></div>

    <script src="assets/javascripts/govuk-template.js" type="text/javascript"></script>

  </body>
</html>
