package com.example.dnatask.config;

import com.example.dnatask.common.EventPublisher;
import com.example.dnatask.common.SpringEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class Config {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    EventPublisher springEventPublisher(ApplicationEventPublisher eventPublisher) {
        return new SpringEventPublisher(eventPublisher);
    }

}
