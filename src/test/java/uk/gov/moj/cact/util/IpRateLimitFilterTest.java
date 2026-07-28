package uk.gov.moj.cact.util;

import com.github.benmanes.caffeine.cache.Ticker;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import uk.gov.moj.cact.config.RateLimitProperties;
import uk.gov.moj.cact.monitoring.IpRateLimitFilter;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IpRateLimitFilterTest {

    private static final int MAX_REQUESTS = 3;
    private static final Duration WINDOW = Duration.ofSeconds(60);

    private final MutableTicker ticker = new MutableTicker();

    private final FilterChain chain = mock(FilterChain.class);

    private final IpRateLimitFilter filter = new IpRateLimitFilter(
            new RateLimitProperties(MAX_REQUESTS, WINDOW, 1000), ticker);

    @Test
    void shouldAllowRequestsUpToTheLimit() throws Exception {
        for (int i = 0; i < MAX_REQUESTS; i++) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            filter.doFilter(scraperRequest("10.0.0.1"), response, chain);
            assertEquals(200, response.getStatus());
        }
        verify(chain, times(MAX_REQUESTS)).doFilter(any(), any());
    }

    @Test
    void shouldRejectWithTooManyRequestsOnceOverTheLimit() throws Exception {
        for (int i = 0; i < MAX_REQUESTS; i++) {
            filter.doFilter(scraperRequest("10.0.0.1"), new MockHttpServletResponse(), chain);
        }

        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(scraperRequest("10.0.0.1"), response, chain);

        assertEquals(429, response.getStatus());
        verify(chain, times(MAX_REQUESTS)).doFilter(any(), any());
    }

    @Test
    void shouldLimitEachIpIndependently() throws Exception {
        for (int i = 0; i < MAX_REQUESTS; i++) {
            filter.doFilter(scraperRequest("10.0.0.1"), new MockHttpServletResponse(), chain);
        }

        MockHttpServletResponse otherIp = new MockHttpServletResponse();
        filter.doFilter(scraperRequest("10.0.0.2"), otherIp, chain);

        assertEquals(200, otherIp.getStatus());
    }

    @Test
    void shouldNeverLimitBrowserTraffic() throws Exception {
        for (int i = 0; i < MAX_REQUESTS * 3; i++) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            filter.doFilter(browserRequest("10.0.0.1"), response, chain);
            assertEquals(200, response.getStatus());
        }
    }

    @Test
    void shouldAllowRequestsAgainOnceTheWindowHasPassed() throws Exception {
        for (int i = 0; i < MAX_REQUESTS + 1; i++) {
            filter.doFilter(scraperRequest("10.0.0.1"), new MockHttpServletResponse(), chain);
        }

        ticker.advance(WINDOW.plusSeconds(1));

        MockHttpServletResponse afterWindow = new MockHttpServletResponse();
        filter.doFilter(scraperRequest("10.0.0.1"), afterWindow, chain);

        assertEquals(200, afterWindow.getStatus());
    }

    @Test
    void shouldNotExtendTheWindowWhileRequestsKeepArrivingSafely() throws Exception {
        filter.doFilter(scraperRequest("10.0.0.1"), new MockHttpServletResponse(), chain);
        ticker.advance(Duration.ofSeconds(30));
        filter.doFilter(scraperRequest("10.0.0.1"), new MockHttpServletResponse(), chain);
        ticker.advance(Duration.ofSeconds(31));

        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(scraperRequest("10.0.0.1"), response, chain);

        assertEquals(200, response.getStatus());
    }

    @Test
    void shouldExpectSpoofedForwardedForToNotResetTheLimit() throws Exception {
        for (int i = 0; i < MAX_REQUESTS; i++) {
            MockHttpServletRequest request = scraperRequest("10.0.0.1");
            request.addHeader("X-Forwarded-For", "1.2.3." + i + ", 10.0.0.1");
            filter.doFilter(request, new MockHttpServletResponse(), chain);
        }

        MockHttpServletRequest request = scraperRequest("10.0.0.1");
        request.addHeader("X-Forwarded-For", "9.9.9.9, 10.0.0.1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, chain);

        assertEquals(429, response.getStatus());
    }

    @Test
    void shouldFallBackToRemoteAddressWhenNotForwarded() throws Exception {
        for (int i = 0; i < MAX_REQUESTS; i++) {
            filter.doFilter(scraperRequest("10.0.0.9"), new MockHttpServletResponse(), chain);
        }

        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(scraperRequest("10.0.0.9"), response, chain);

        assertEquals(429, response.getStatus());
    }

    //non-ui
    private static MockHttpServletRequest scraperRequest(String remoteAddr) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/search");
        request.setRemoteAddr(remoteAddr);
        request.addHeader("User-Agent", "curl/8.4.0");
        return request;
    }

    private static MockHttpServletRequest browserRequest(String remoteAddr) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/search");
        request.setRemoteAddr(remoteAddr);
        request.addHeader("User-Agent", "Mozilla/5.0 (Macintosh) Chrome/126.0 Safari/537.36");
        request.addHeader("Accept", "text/html,application/xhtml+xml");
        return request;
    }

    /** Lets the tests move time without sleeping. */
    private static final class MutableTicker implements Ticker {
        private long nanos;

        @Override
        public long read() {
            return nanos;
        }

        void advance(Duration duration) {
            nanos += duration.toNanos();
        }
    }
}
