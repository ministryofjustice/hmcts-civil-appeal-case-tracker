package uk.gov.moj.cact.config;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsessionIdUriFilterTest {

    private final JsessionIdUriFilter filter = new JsessionIdUriFilter();

    /** The URI the application code (and Spring Security's firewall) will see. */
    private String uriSeenDownstream(String requestUri) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", requestUri);
        request.setRequestURI(requestUri);
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, new MockHttpServletResponse(), chain);

        return ((HttpServletRequest) chain.getRequest()).getRequestURI();
    }

    @Test
    void shouldStripJsessionIdFromLegacyDetailUrl() throws Exception {
        assertEquals("/getDetail.do",
                uriSeenDownstream("/getDetail.do;jsessionid=0E3FBB1014815D2F27D5E83607FD44D1"));
    }

    @Test
    void shouldStripJsessionIdRegardlessOfCase() throws Exception {
        assertEquals("/search.do", uriSeenDownstream("/search.do;JSESSIONID=ABC123"));
        assertEquals("/search.do", uriSeenDownstream("/search.do;JSessionId=ABC123"));
    }

    @Test
    void shouldLeaveOrdinaryUrlsUntouched() throws Exception {
        assertEquals("/case/CA-2025-001011", uriSeenDownstream("/case/CA-2025-001011"));
        assertEquals("/search", uriSeenDownstream("/search"));
    }


    // Only the session id is removed. Any other path parameter stays
    @Test
    void shouldNotStripOtherPathParameters() throws Exception {
        assertEquals("/search.do;foo=bar", uriSeenDownstream("/search.do;foo=bar"));
        assertEquals("/search.do;evil=1",
                uriSeenDownstream("/search.do;jsessionid=ABC123;evil=1"));
    }

    @Test
    void shouldStripJsessionIdMidPath() throws Exception {
        assertEquals("/listing_calendar/search.jsp",
                uriSeenDownstream("/listing_calendar;jsessionid=ABC123/search.jsp"));
    }

    @Test
    void shouldRemoveJsessionIdFromRequestUrlToo() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/getDetail.do");
        request.setRequestURI("/getDetail.do;jsessionid=ABC123");
        request.setServerName("casetracker.justice.gov.uk");
        request.setScheme("https");
        request.setServerPort(443);
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, new MockHttpServletResponse(), chain);

        assertEquals("https://casetracker.justice.gov.uk/getDetail.do",
                ((HttpServletRequest) chain.getRequest()).getRequestURL().toString());
    }
}
