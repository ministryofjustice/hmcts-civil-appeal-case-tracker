package dts.legacy.utils;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicInteger;

public class SessionTrackingListener implements HttpSessionListener {
    private static final AtomicInteger activeSessions = new AtomicInteger();

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        int count = activeSessions.incrementAndGet();
        System.out.println("Session created: " + event.getSession().getId()
                + " | Active sessions: " + count);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        int count = activeSessions.decrementAndGet();
        System.out.println("Session destroyed: " + event.getSession().getId()
                + " | Active sessions: " + count);
    }

    /** Optional helper method for external metrics collection */
    public static int getActiveSessionCount() {
        return activeSessions.get();
    }
}

