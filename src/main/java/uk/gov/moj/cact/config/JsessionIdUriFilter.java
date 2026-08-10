package uk.gov.moj.cact.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsessionIdUriFilter extends OncePerRequestFilter {

    private static final Pattern JSESSIONID =
            Pattern.compile(";jsessionid=[^;/?]*", Pattern.CASE_INSENSITIVE);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String uri = request.getRequestURI();
        Matcher matcher = JSESSIONID.matcher(uri);

        if (!matcher.find()) {
            chain.doFilter(request, response);
            return;
        }

        chain.doFilter(new CleanUriRequest(request, matcher.replaceAll("")), response);
    }

    /** Presents the request with the jsessionid removed from its URI. */
    private static final class CleanUriRequest extends HttpServletRequestWrapper {

        private final String cleanUri;

        CleanUriRequest(HttpServletRequest request, String cleanUri) {
            super(request);
            this.cleanUri = cleanUri;
        }

        @Override
        public String getRequestURI() {
            return cleanUri;
        }

        @Override
        public StringBuffer getRequestURL() {
            String scheme = getScheme();
            int port = getServerPort();
            StringBuffer url = new StringBuffer(scheme).append("://").append(getServerName());
            boolean defaultPort = ("http".equals(scheme) && port == 80)
                    || ("https".equals(scheme) && port == 443);
            if (!defaultPort) {
                url.append(':').append(port);
            }
            return url.append(cleanUri);
        }
    }
}
