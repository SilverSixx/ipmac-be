package dev.datpl.commonservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class Beans {

    @Bean
    public ConfigProperties configProperties() {
        return new ConfigProperties();
    }

}
