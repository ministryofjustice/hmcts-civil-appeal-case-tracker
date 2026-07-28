package uk.gov.moj.cact.config;

import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers Micrometer's CountedAspect so that @Counted can work.
 * Requires org.aspectj:aspectjweaver on the classpath
 */
@Configuration
public class CountedConfig {

    @Bean
    public CountedAspect countedAspect(MeterRegistry registry) {
        return new CountedAspect(registry);
    }
}
