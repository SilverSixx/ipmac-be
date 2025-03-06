package dev.datpl.commonservice.config;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaConfigProperties {
    private List<String> topics;

    // Add a method to get user creation topic
    public String getUserCreationTopic() {
        return topics != null && !topics.isEmpty() ? topics.get(0) : "user-creation";
    }
}