package dts.legacy.monitoring;

import io.prometheus.metrics.core.metrics.Counter;

public class AppExceptionMetrics {

    private AppExceptionMetrics() {}

    public static final Counter EXCEPTIONS_COUNT = Counter.builder()
            .name("app_exceptions_total")
            .help("Total uncaught application exceptions thrown")
            .labelNames("exception_class", "location")
            .register();

    public static final Counter ERROR_COUNT = Counter.builder()
            .name("app_error_logs_total")
            .help("Total ERROR logs that have been written")
            .labelNames("source", "type")
            .register();
}
