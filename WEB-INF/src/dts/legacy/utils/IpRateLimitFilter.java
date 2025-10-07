package dts.legacy.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class IpRateLimitFilter implements Filter {
    private static class Window {
        long windowStartMillis;
        AtomicInteger count = new AtomicInteger(0);

        Window(long startMillis) {
            this.windowStartMillis = startMillis;
        }
    }

    private Cache<String, Window> windows;
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
        windows = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(10))    // expire entries 10 min after last access
                .maximumSize(10_000)                         // cap number of IP entries
                .build();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getRequestURI();
        boolean shouldLimit = path.contains("/search.do") || path.contains("/getDetail.do");

        if (shouldLimit) {
            String ip = request.getHeader("X-Forwarded-For");
            if (ip != null && !ip.isEmpty()) {
                ip = ip.split(",")[0].trim();
            } else {
                ip = request.getRemoteAddr();
            }

            long now = System.currentTimeMillis();
            Window window = windows.get(ip, k -> new Window(now));

            synchronized (window) {
                if (now - window.windowStartMillis > windowMillis) {
                    // reset window
                    window.windowStartMillis = now;
                    window.count.set(0);
                }
                int newCount = window.count.incrementAndGet();

                System.out.println("TestLimit: path <" + path + "> ip <" + ip + "> Max <" + maxRequests +
                        "> Current <" +newCount + ">");

                if (newCount > maxRequests) {
                    response.setStatus(429);
                    response.getWriter().write("Too Many Requests in last 60 seconds");
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        windows.invalidateAll();
    }
}
