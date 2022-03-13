package com.example.dnatask.user;

import com.example.dnatask.common.EventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class UserConfig {

    @Bean
    UserService userService(UserRepository userRepository,
                                   EventPublisher eventPublisher,
                                   Clock clock) {
        return new UserService(userRepository,
                               eventPublisher, clock);
    }
}
