package uk.gov.moj.cact.monitoring;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import uk.gov.moj.cact.config.RateLimitProperties;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class IpRateLimitFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(IpRateLimitFilter.class);

    private static final String FORWARDED_FOR = "X-Forwarded-For";

    private final int maxRequests;
    private final Cache<String, AtomicInteger> requestCounts;

    public IpRateLimitFilter(RateLimitProperties properties) {
        this(properties, Ticker.systemTicker());
    }

    public IpRateLimitFilter(RateLimitProperties properties, Ticker ticker) {
        this.maxRequests = properties.maxRequests();
        this.requestCounts = Caffeine.newBuilder()
                .expireAfterWrite(properties.window())
                .maximumSize(properties.maxTrackedIps())
                .ticker(ticker)
                .build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        if (UiRequestValidator.isUiRequest(request)) {
            chain.doFilter(request, response);
            return;
        }

        String ip = clientIp(request);
        int count = requestCounts.get(ip, key -> new AtomicInteger()).incrementAndGet();

        if (count > maxRequests) {
            LOGGER.info("IpRateLimit: path <{}> ip <{}> max <{}> current <{}>",
                    request.getRequestURI(), ip, maxRequests, count);
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value());
            return;
        }

        chain.doFilter(request, response);
    }

    /**
     * Resolves the caller using the rightmost X-Forwarded-For entry.
     *
     * That entry is the peer address the ingress observed and appended. Entries
     * to its left are supplied by the caller and can be forged, so keying the
     * limit on them would let a scraper look like a new IP on every request and
     * bypass the limit entirely.
     */
    private static String clientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader(FORWARDED_FOR);
        if (forwardedFor == null || forwardedFor.isBlank()) {
            return request.getRemoteAddr();
        }
        String[] hops = forwardedFor.split(",");
        String closestHop = hops[hops.length - 1].trim();
        return closestHop.isEmpty() ? request.getRemoteAddr() : closestHop;
    }
}
