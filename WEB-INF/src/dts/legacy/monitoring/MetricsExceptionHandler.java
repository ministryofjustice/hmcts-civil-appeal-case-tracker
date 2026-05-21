package dts.legacy.monitoring;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

public class MetricsExceptionHandler extends ExceptionHandler {

    @Override
    public ActionForward execute(Exception ex, ExceptionConfig config,
                                 ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        AppExceptionMetrics.EXCEPTIONS_COUNT
                .labelValues(ex.getClass().getSimpleName(), mapping.getPath())
                .inc();

        return super.execute(ex, config, mapping, form, request, response);
    }
}
