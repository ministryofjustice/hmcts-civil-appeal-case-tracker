package dts.legacy.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentMap;

public class IpRateLimitFilter implements Filter {
    private static class Window {
        long windowStartMillis;
        AtomicInteger count = new AtomicInteger(0);

        Window(long startMillis) {
            this.windowStartMillis = startMillis;
        }
    }

    private Cache<String, Window> ipCache;
    private int maxRequests;
    private long windowMillis;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String maxReq = filterConfig.getInitParameter("maxRequests");
        String windowSec = filterConfig.getInitParameter("windowSeconds");
        maxRequests = (maxReq != null) ? Integer.parseInt(maxReq) : 15;
        long wsec = (windowSec != null) ? Long.parseLong(windowSec) : 60;
        windowMillis = wsec * 1000L;

        // Build a Caffeine cache with eviction:
        ipCache = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(10))    // expire entries 10 min after last access
                .maximumSize(10000)                         // cap number of IP entries
                .build();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getRequestURI();

        /*
        The paths are currently explicitly specified by the filter in web.xml
        Should there be a requirement to add more intelligent filtering by path
        then the web.xml can be changed to filter "/*" and the logic can be applied
        according to the path in here

        boolean shouldLimit = path.contains("/search.do") || path.contains("/getDetail.do");
         */

        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0].trim();
        } else {
            ip = request.getRemoteAddr();
        }

        String referer = request.getHeader("Referer");
        System.out.println("IpRateLimit: Referer <" + referer + ">");

        long now = System.currentTimeMillis();
        Window window = ipCache.get(ip, k -> new Window(now));

        synchronized (window) {
            if (now - window.windowStartMillis > windowMillis) {
                // reset window
                window.windowStartMillis = now;
                window.count.set(0);
            }
            int newCount = window.count.incrementAndGet();

            if (newCount > maxRequests) {
                System.out.println("IpRateLimit: path <" + path + "> ip <" + ip + "> Max <" + maxRequests +
                        "> Current <" +newCount + ">");

                response.setStatus(429);
                response.getWriter().write("Too Many Requests in last 60 seconds");
                return;
            }
        }

        // #TODO - DEBUG only to verify removal of ip addresses after timeout
        //verifyIpCache();

        chain.doFilter(request, response);
    }

    private void verifyIpCache() {
        // Get a view of the cache as a ConcurrentMap
        ConcurrentMap<String, Window> mapView = ipCache.asMap();

// Iterate over keys
        for (String ip : mapView.keySet()) {
            Window w = mapView.get(ip);
            System.out.println("IP: " + ip + ", windowStart=" + w.windowStartMillis + ", count=" + w.count.get());
        }
    }

    @Override
    public void destroy() {
        ipCache.invalidateAll();
    }
}
