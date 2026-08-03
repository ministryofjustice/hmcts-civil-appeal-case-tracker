package uk.gov.moj.cact.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.moj.cact.monitoring.IpRateLimitFilter;

@Configuration
@EnableConfigurationProperties(RateLimitProperties.class)
public class WebConfig {

    @Bean
    public FilterRegistrationBean<IpRateLimitFilter> ipRateLimitFilter(RateLimitProperties properties) {
        FilterRegistrationBean<IpRateLimitFilter> registration =
                new FilterRegistrationBean<>(new IpRateLimitFilter(properties));
        registration.addUrlPatterns("/search", "/case/*");
        return registration;
    }
}
