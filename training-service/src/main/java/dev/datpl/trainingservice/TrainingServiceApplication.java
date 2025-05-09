package dev.datpl.trainingservice;

import dev.datpl.trainingservice.config.ConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TrainingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TrainingServiceApplication.class, args);
    }

    @Bean
    public ConfigProperties configProperties() {
        return new ConfigProperties();
    }
}
