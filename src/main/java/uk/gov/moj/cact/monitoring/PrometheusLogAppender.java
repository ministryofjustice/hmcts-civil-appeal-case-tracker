package uk.gov.moj.cact.monitoring;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import io.micrometer.core.instrument.Metrics;

public class PrometheusLogAppender  extends AppenderBase<ILoggingEvent> {
    @Override
    protected void append(ILoggingEvent event) {
        try {
            if (event.getLevel().isGreaterOrEqual(Level.ERROR)) {
                Metrics.counter("app.error.logs",
                                "source", event.getLoggerName(),
                                "type", "log_error")
                        .increment();
            }
        } catch (Throwable t) {
            // Never let metrics collection break logging
            t.printStackTrace();
        }
    }
}
