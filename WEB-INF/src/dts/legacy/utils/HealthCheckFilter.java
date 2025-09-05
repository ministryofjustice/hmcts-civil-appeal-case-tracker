package dts.legacy.utils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HealthCheckFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Check if the request URI matches the health check endpoint
        if (httpRequest.getRequestURI().endsWith("/health")) {
            // Prevent session creation
            httpRequest.getSession(false);

            // Set response content type and status
            httpResponse.setContentType("text/plain");
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            httpResponse.getWriter().write("OK");
            return; // Skip further processing
        }

        // Continue with the filter chain for other requests
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup code if needed
    }
}
