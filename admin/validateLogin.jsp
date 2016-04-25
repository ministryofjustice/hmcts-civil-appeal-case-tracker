<%@ page errorPage="ExceptionHandler.jsp" %>
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
      <h1>Case Tracker for Civil Appeals</h1>

      <%
      String userid = request.getParameter("userid");
      String password = request.getParameter("password");

      if ((userid.equals("admin")) && (password.equals("diary"))) {
      session.setAttribute("UserName", userid);

      %>
      <jsp:forward page="SimpleUpload1.jsp">
      <jsp:param name="id" value="<%= userid %>" />
      </jsp:forward>

      <% } else { %>
        <jsp:forward page="error.jsp"/>
      <% } %>
      <!-- InstanceEndEditable -->

    </main>

    <jsp:include page="../includes/footer.jsp" />

    <div id="global-app-error" class="app-error hidden"></div>

    <script src="assets/javascripts/govuk-template.js" type="text/javascript"></script>

  </body>
</html>
