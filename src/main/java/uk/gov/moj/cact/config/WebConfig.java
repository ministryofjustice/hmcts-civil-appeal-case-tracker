package uk.gov.moj.cact.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import uk.gov.moj.cact.monitoring.IpRateLimitFilter;

@Configuration
@EnableConfigurationProperties(RateLimitProperties.class)
public class WebConfig {

    @Bean
    public FilterRegistrationBean<JsessionIdUriFilter> jsessionIdUriFilter() {
        FilterRegistrationBean<JsessionIdUriFilter> registration = new FilterRegistrationBean<>(new JsessionIdUriFilter());
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean<IpRateLimitFilter> ipRateLimitFilter(RateLimitProperties properties) {
        FilterRegistrationBean<IpRateLimitFilter> registration =
                new FilterRegistrationBean<>(new IpRateLimitFilter(properties));
        registration.addUrlPatterns("/search", "/case/*");
        return registration;
    }
}
