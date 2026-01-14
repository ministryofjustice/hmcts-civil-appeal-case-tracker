package dts.legacy.utils;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

public class ExceptionHandlerRequestProcessor extends RequestProcessor {
    private static final Logger LOGGER = Logger.getLogger(ExceptionHandlerRequestProcessor.class);

    @Override
    protected ActionForward processException(
            HttpServletRequest request,
            HttpServletResponse response,
            Exception exception,
            ActionForm form,
            ActionMapping mapping)
            throws IOException, ServletException {

        LOGGER.error("===== STRUTS UNHANDLED EXCEPTION =====");
        LOGGER.error("URI: " + request.getRequestURI());
        LOGGER.error("Query: " + request.getQueryString());
        LOGGER.error("Stack trace: ", exception);

        return super.processException(request, response, exception, form, mapping);
    }
}
