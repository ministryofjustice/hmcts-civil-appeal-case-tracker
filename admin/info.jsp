<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

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
      <div id="Heading">
        <h1>Case Tracker for Civil Appeals</h1>
      </div>
      <div class="holder">
        <!-- InstanceBeginEditable name="main" -->
        <h2>Case Tracker Administration</h2>
        <p>
          <logic:present name="msg">
            <%=request.getAttribute("msg") %>
          </logic:present>
        </p>

        <p><html:link href="simpleUpload.do">[Go Back]</html:link></p>
        <!-- InstanceEndEditable -->
      </div>
    </main>

    <jsp:include page="../includes/footer.jsp" />

    <div id="global-app-error" class="app-error hidden"></div>

    <script src="assets/javascripts/govuk-template.js" type="text/javascript"></script>

  </body>
</html>
