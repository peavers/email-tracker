package space.forloop.tracking.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("application")
public record AppProperties(String applicationName, String tokenDirectory, String credentials) {}
