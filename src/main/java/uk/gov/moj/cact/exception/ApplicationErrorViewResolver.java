package uk.gov.moj.cact.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.autoconfigure.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Component
public class ApplicationErrorViewResolver implements ErrorViewResolver {
    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
        String viewName = status == HttpStatus.TOO_MANY_REQUESTS
                ? "error-limit"
                : "error";

        return new ModelAndView(viewName, model);
    }
}
