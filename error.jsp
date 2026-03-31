<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head><title>Error</title></head>
    <body>
        <h1>Case Tracker Error</h1>
        <p>An error occurred or an incorrect URL was specified.</p>
        <p>Resubmit the query or report to dts-legacy-apps-support-team@hmcts.net</p>

        <%
            Boolean invalidPage = (Boolean) request.getAttribute("invalidPage");
            if (Boolean.TRUE.equals(invalidPage)) {
                Long   totalResults = (Long)    request.getAttribute("totalResults");
                Integer totalPages  = (Integer) request.getAttribute("totalPages");
                Integer pageSize  = (Integer) request.getAttribute("pageSize");
        %>
            <div class="result">
                <span class="pagebanner">
                    Invalid page requested. There are only <%= totalPages %> page(s) for this search with page size <%= pageSize %>
                    (<%= totalResults %> total results).
                </span>
            </div>
        <%
            }
        %>

        <div>
            <div>
                <ul>
                    <li><a href="https://www.justice.gov.uk/privacy">Your privacy</a></li>
                    <li><a href="https://www.justice.gov.uk/copyright">&copy; Crown copyright</a></li>
                </ul>
            </div>
        </div>

        <div>
            <div>
                <span class="tl"></span><span class="tr"><span></span></span>
                <a href="http://www.justice.gov.uk/index.htm" tabindex="1"><img src="images/logo.png"
                        alt="Link to the Justice homepage" /></a>
            </div>
        </div>
    </body>
</html>