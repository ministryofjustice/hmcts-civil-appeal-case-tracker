package dts.legacy.monitoring;

import io.prometheus.metrics.instrumentation.jvm.JvmMetrics;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MetricsInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // Registers: jvm_memory_*, jvm_gc_*, jvm_threads_*, jvm_info, jvm_classes_*, etc.
        JvmMetrics.builder().register();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
