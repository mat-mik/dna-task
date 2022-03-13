package com.example.dnatask.user;

import com.example.dnatask.common.StoreLastEventPublisher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    private Clock fixedClock;

    private StoreLastEventPublisher eventPublisher;

    private UserService userService;

    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(Instant.parse("2022-03-13T09:08:28.000Z"), ZoneId.systemDefault());
        eventPublisher = new StoreLastEventPublisher();
        userService = new UserConfig()
            .userService(userRepository, eventPublisher, fixedClock);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUser() {
        // given
        String name = "DNA";

        // when
        long createdUserId = userService.create(new CreateUserRequest(name));

        // then
        userWasCreated(createdUserId, name);
        userCreatedEventWasPublished(name, createdUserId);
    }

    @Test
    void shouldFindAllUsers() {
        // given
        aExistingUserWith("DNA");
        aExistingUserWith("Avenga");

        // when
        List<User> result = userService.findAll();

        assertThat(result)
            .hasSize(2);

        // TODO flaky tests - list order (e.g. AssertObject pattern)
        assertThat(result.get(0).getName())
            .isEqualTo("DNA");

        assertThat(result.get(1).getName())
            .isEqualTo("Avenga");

    }

    @Test
    void shouldUpdateUser() {
        // given
        long existingUserId = aExistingUser();
        String newName = "New Name";

        // when
        userService.update(existingUserId, newName);

        // then
        userWasUpdated(existingUserId, newName);
        userNameUpdatedEventWasPublished(existingUserId, newName);
    }

    @Test
    void shouldDeleteUser() {
        // given
        long existingUserId = aExistingUser();

        // when
        userService.delete(existingUserId);

        // then
        userWasDeleted();
        userDeletedEventWasPublished(existingUserId);
    }

    private void userWasCreated(long createdUserId, String name) {
        assertThat(userService.findAll())
            .hasSize(1);

        User createdUser = userRepository.findById(createdUserId).orElseThrow();
        assertThat(createdUser.getName())
            .isEqualTo(name);
    }

    private void userCreatedEventWasPublished(String name, long createdUserId) {
        assertThat(eventPublisher.getLastEvent())
            .isExactlyInstanceOf(UserCreated.class);

        UserCreated userCreated = (UserCreated) eventPublisher.getLastEvent();
        assertThat(userCreated.getAggregateId())
            .isEqualTo(createdUserId);
        assertThat(userCreated.name())
            .isEqualTo(name);
        assertThat(userCreated.getWhen())
            .isEqualTo(Instant.now(fixedClock));
    }

    private void userWasUpdated(long userId, String newName) {
        User updatedUser = userRepository.findById(userId).orElseThrow();
        assertThat(updatedUser.getName())
            .isEqualTo(newName);
    }

    private void userNameUpdatedEventWasPublished(long userId, String newName) {
        assertThat(eventPublisher.getLastEvent())
            .isEqualTo(new UserNameUpdated(userId, newName, Instant.now(fixedClock)));

    }

    private void userWasDeleted() {
        assertThat(userService.findAll())
            .isEmpty();
    }

    private void userDeletedEventWasPublished(long userId) {
        assertThat(eventPublisher.getLastEvent())
            .isEqualTo(new UserDeleted(userId, Instant.now(fixedClock)));
    }

    private long aExistingUser() {
        return aExistingUserWith("DNA");
    }

    private long aExistingUserWith(String name) {
        return userRepository.save(new User(name)).getId();
    }
}
