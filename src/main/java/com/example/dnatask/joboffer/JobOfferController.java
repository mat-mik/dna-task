package com.example.dnatask.joboffer;

import com.example.dnatask.joboffer.job.JobOfferService;
import com.example.dnatask.joboffer.job.model.Category;
import com.example.dnatask.joboffer.listing.JobListing;
import com.example.dnatask.joboffer.listing.JobListingService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/joboffer")
class JobOfferController {

    private final JobOfferService jobOfferService;

    private final JobListingService jobListingService;

    JobOfferController(JobOfferService jobOfferService,
                       JobListingService jobListingService) {
        this.jobOfferService = jobOfferService;
        this.jobListingService = jobListingService;
    }

    @PostMapping
    ResponseEntity<Long> addJobOffer(@Validated @RequestBody CreateJobOfferRequest createJobOfferRequest) {
        long jobOfferId = jobOfferService.addJobOffer(createJobOfferRequest);
        return ResponseEntity.ok(jobOfferId);
    }

    @GetMapping
    ResponseEntity<List<JobListing>> listing(@RequestParam(required = false) Category category,
                                             @RequestParam(required = false) String employer) {
        return ResponseEntity.ok(jobListingService.findBy(category, employer));
    }
}
