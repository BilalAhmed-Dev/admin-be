package com.admin.panel.configuration.data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("api.authentication")
@Configuration
@Getter
@Setter
public class AuthenticationConfiguration {
    String jwtSecret;
    Long jwtExpiration;
    Long jwtRefreshExpiration;
}

