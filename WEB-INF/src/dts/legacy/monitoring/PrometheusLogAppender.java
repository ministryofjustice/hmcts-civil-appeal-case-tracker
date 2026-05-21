package dts.legacy.monitoring;

import static dts.legacy.monitoring.AppExceptionMetrics.ERROR_COUNT;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

public class PrometheusLogAppender extends AppenderSkeleton {

    @Override
    protected void append(LoggingEvent loggingEvent) {
        try {
            if (loggingEvent.getLevel().isGreaterOrEqual(Level.ERROR)) {
                ERROR_COUNT
                        .labelValues(loggingEvent.getLoggerName(), "log_error")
                        .inc();
            }
        } catch (Throwable t) {
            // To catch any exception that get trapped in Log4j internals
            t.printStackTrace();
        }
    }

    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
