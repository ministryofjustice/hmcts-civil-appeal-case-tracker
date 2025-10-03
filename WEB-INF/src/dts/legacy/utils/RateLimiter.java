package dts.legacy.utils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RateLimiter implements Filter {
    private int maxRequests;
    private long windowMillis;

    // Map of IP → (window start timestamp + count)
    private final ConcurrentMap<String, SlidingWindow> windows = new ConcurrentHashMap<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Read init params
        String maxReqsParam = filterConfig.getInitParameter("maxRequests");
        String windowSecParam = filterConfig.getInitParameter("windowSeconds");
        maxRequests = (maxReqsParam != null) ? Integer.parseInt(maxReqsParam) : 10;
        long windowSeconds = (windowSecParam != null) ? Long.parseLong(windowSecParam) : 60;
        windowMillis = windowSeconds * 1000L;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getRequestURI();
        boolean shouldLimit = path.contains("/search.do") || path.contains("/getDetail.do");

        if (shouldLimit) {
            String ip = request.getRemoteAddr();
            SlidingWindow window = windows.computeIfAbsent(ip, (k) -> new SlidingWindow(windowMillis));
            int count = window.increment();

            System.out.println("TestLimit: path <" + path + "> ip <" + ip + "> Max <" + maxRequests + "> Current <" + count + ">");

            if (count > maxRequests) {
                response.setStatus(429);
                response.getWriter().write("Too Many Requests in last 60 seconds");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        windows.clear();
    }

    // Inner class for sliding window counting
    private static class SlidingWindow {
        private final long windowMillis;
        private long windowStart;
        private int count;

        public SlidingWindow(long windowMillis) {
            this.windowMillis = windowMillis;
            this.windowStart = System.currentTimeMillis();
            this.count = 0;
        }

        public synchronized int increment() {
            long now = System.currentTimeMillis();
            if (now - windowStart > windowMillis) {
                windowStart = now;
                count = 0;
            }
            count++;
            return count;
        }
    }
}

