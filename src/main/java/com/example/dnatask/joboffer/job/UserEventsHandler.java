package com.example.dnatask.joboffer.job;

import com.example.dnatask.user.UserCreated;
import com.example.dnatask.user.UserDeleted;
import com.example.dnatask.user.UserNameUpdated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventsHandler {

    private static final Logger log = LoggerFactory.getLogger(UserEventsHandler.class);

    private final JobOfferService jobOfferService;

    public UserEventsHandler(JobOfferService jobOfferService) {
        this.jobOfferService = jobOfferService;
    }

    @EventListener
    public void handle(UserCreated event) {
        log.debug("{} event caught", event);
        jobOfferService.addEmployer(event.getAggregateId(), event.name());
    }

    @EventListener
    public void handle(UserDeleted event) {
        log.debug("{} event caught", event);
        jobOfferService.deleteEmployer(event.getAggregateId());
    }

    @EventListener
    public void handle(UserNameUpdated event) {
        log.debug("{} event caught", event);
        jobOfferService.updateEmployer(event.getAggregateId(), event.newName());
    }
}
