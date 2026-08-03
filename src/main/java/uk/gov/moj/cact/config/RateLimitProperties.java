package uk.gov.moj.cact.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

@ConfigurationProperties("app.rate-limit")
public record RateLimitProperties(
        @DefaultValue("20") int maxRequests,
        @DefaultValue("60s") Duration window,
        @DefaultValue("10000") long maxTrackedIps) {
}
