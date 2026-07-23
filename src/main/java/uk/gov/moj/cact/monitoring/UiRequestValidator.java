package uk.gov.moj.cact.monitoring;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UiRequestValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(UiRequestValidator.class);

    private UiRequestValidator() {
    }

    public static boolean isUiRequest(HttpServletRequest request) {

        String accept = request.getHeader("Accept");
        String referer = request.getHeader("Referer");
        String uiParam = request.getParameter("ui");
        String userAgent = request.getHeader("User-Agent");

        // Explicit override parameter - most reliable signal
        if ("true".equalsIgnoreCase(uiParam)) {
            return true;
        }
        if ("false".equalsIgnoreCase(uiParam)) {
            return false;
        }

        if (accept != null && accept.contains("application/json")) {
            return false;
        }

        // curl and common API tools identify themselves clearly
        if (userAgent != null && (
                userAgent.startsWith("curl/")
                        || userAgent.startsWith("wget/")
                        || userAgent.startsWith("Python")
                        || userAgent.startsWith("Java/")
                        || userAgent.startsWith("Apache-HttpClient"))) {
            return false;
        }

        // Referer present and matches known domain - definite UI
        if (referer != null && (
                referer.contains("casetracker.justice.gov.uk")
                        || referer.contains("localhost"))) {
            return true;
        }

        if (accept != null && accept.contains("text/html")) {
            return true;
        }

        // No Referer but Accept looks like a browser (bookmarked URL case)
        if (accept != null && accept.contains("*/*") && userAgent != null && (
                userAgent.contains("Mozilla")
                        || userAgent.contains("Chrome")
                        || userAgent.contains("Safari")
                        || userAgent.contains("Firefox")
                        || userAgent.contains("Edge"))) {
            return true;
        }

        // Default to non-UI - safer for memory/rate-limit purposes
        LOGGER.debug("isUiRequest: no signals matched - defaulting to non-UI");
        return false;
    }
}
