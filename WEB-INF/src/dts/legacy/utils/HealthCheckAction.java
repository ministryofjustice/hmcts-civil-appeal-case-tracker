package dts.legacy.utils;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HealthCheckAction extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
        // Prevent session creation
        request.getSession(false);

        // Set response content type
        response.setContentType("text/plain");
        response.setStatus(HttpServletResponse.SC_OK);

        try {
            // Write a simple health check message
            response.getWriter().write("OK");
        } catch (IOException e) {
            System.out.println("Failed to write response to health check request");
        }

        // Return null to indicate no further processing is needed
        return null;
    }
}
