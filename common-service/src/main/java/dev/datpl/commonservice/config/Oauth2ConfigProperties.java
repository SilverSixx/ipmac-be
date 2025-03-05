package dev.datpl.commonservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "oauth2")
@Setter
@Getter
public class Oauth2ConfigProperties {
    @Value("${oauth2.auth-server-url}")
    private String serverUrl;

    @Value("${oauth2.realm}")
    private String realm;

    @Value("${oauth2.admin-client.client-id}")
    private String clientId;

    @Value("${oauth2.admin-client.client-secret}")
    private String clientSecret;

    private List<String> clients;

}
