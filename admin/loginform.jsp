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
      <div class="holder">
        <!-- InstanceBeginEditable name="main" -->
        <h2>Login Form</h2>
        <p>Please enter your username and details below. </p>

        <form method="post" action="validateLogin.do">
          <p>User ID: </p>
          <p><input size="20" name="userid" type="text" /></p>
          <p>Password:</p>
          <p><input size="20" name="password" type="password" /></p>
          <p><input type="submit" value="Submit" name="SubmitBtn" />&nbsp;<input type="reset" value="Reset" name="B2" /></p>
        </form>
        <!-- InstanceEndEditable -->
      </div>

    </main>

    <jsp:include page="../includes/footer.jsp" />

    <div id="global-app-error" class="app-error hidden"></div>

    <script src="assets/javascripts/govuk-template.js" type="text/javascript"></script>

  </body>
</html>
