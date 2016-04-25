<%@ page isErrorPage="true" import="java.io.*" %>

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
        <h3>An error has occurred!</h3>
        <p>Please report the error message below to Publications Branch with a brief description of what action you were carrying out.</p>
        <p><strong><%= exception.toString() %></strong></p>
        <p>
          <%
            out.println("<!--");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exception.printStackTrace(pw);
            out.print(sw);
            sw.close();
            pw.close();
            out.println("-->");
          %>
        </p>
      </div>
    </main>

    <jsp:include page="../includes/footer.jsp" />

    <div id="global-app-error" class="app-error hidden"></div>

    <script src="assets/javascripts/govuk-template.js" type="text/javascript"></script>

  </body>
</html>
