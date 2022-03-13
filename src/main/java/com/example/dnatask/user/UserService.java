package com.example.dnatask.user;

import com.example.dnatask.common.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final EventPublisher eventPublisher;

    private final Clock clock;

    UserService(UserRepository userRepository, EventPublisher eventPublisher, Clock clock) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    public long create(CreateUserRequest request) {
        log.info("Creating user {}", request);

        User user = userRepository.save(new User(request.name()));
        log.debug("Created user '{}'", user.getId());

        eventPublisher.publish(new UserCreated(user.getId(), user.getName(), Instant.now(clock)));

        return user.getId();
    }

    public void update(long userId, String newName) {
        log.info("Updating user (id: {}, newName: '{})", userId, newName);
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new UserNotFoundException("User %s not found".formatted(userId)));

        user.setName(newName);
        userRepository.save(user);
        eventPublisher.publish(new UserNameUpdated(userId, newName, Instant.now(clock)));
    }

    public void delete(long userId) {
        log.info("Deleting user '{}'", userId);

        userRepository.findById(userId)
                      .ifPresent(user -> {
                          userRepository.delete(user);
                          eventPublisher.publish(new UserDeleted(userId, Instant.now(clock)));
                      });

    }

    public List<User> findAll() {
        // TODO pagination (OOM)
        return userRepository.findAll();
    }
}
