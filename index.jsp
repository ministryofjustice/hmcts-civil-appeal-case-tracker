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
    <p>The Case Tracker allows users to search for information on applications or appeals in the Court of Appeal, Civil
        Division. Users are also able to search for information on applications or appeals heard in the last 31 days.
        You can search for a case in the following three ways:</p>

    <ul class="listFix">
        <li><strong>By Case Number</strong> &ndash; The case number for the case needs to be 8 digits long and must be
            entered in the following format, without any spaces or oblique strokes: 20051234;
        </li>
        <li><strong>By Title</strong> &ndash; The title can be entered using the names of either party. For example, in
            the case of Smith &amp; Co v Jones, either &quot;Smith&quot; or &quot;Jones&quot; can be entered to search;
            and
        </li>
        <li><strong>By Date</strong> &ndash; A date of hearing can be entered in the following format DD-MMM-YY eg: 15&ndash;Jan&ndash;09,
            or you can click the calendar icon and choose a date from the pop&ndash;up calendar.
        </li>
    </ul>

    <p>For confirmation of the judges hearing your case, the time and location of the hearing, please check the <a
            href="http://www.justice.gov.uk/guidance/courts-and-tribunals/courts/daily-court-hearings.htm"
            target="_blank">Daily List</a> from 14:30 the working day before your case is due to be heard, or call the
        Listing Office.</p>
    <p>Information is provided in good faith for the convenience of court users and others. Whilst we endeavour to
        ensure that the information is correct, listing dates and constitutions in particular are subject to change.</p>

    <div class="submitc">
        <div class="function next right">
            <span class="tl"></span>
            <span class="tr"><span></span></span>
            <a href="search.jsp">Next <span class="access">page</span></a>
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
