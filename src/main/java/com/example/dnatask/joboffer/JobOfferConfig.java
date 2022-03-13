package com.example.dnatask.joboffer;

import com.example.dnatask.joboffer.job.EmployerRepository;
import com.example.dnatask.joboffer.job.JobOfferRepository;
import com.example.dnatask.joboffer.job.JobOfferService;
import com.example.dnatask.joboffer.listing.JobListingRepository;
import com.example.dnatask.joboffer.listing.JobListingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class JobOfferConfig {

    @Bean
    JobOfferService jobOfferService(JobOfferRepository jobOfferRepository,
                                    EmployerRepository employerRepository,
                                    JobListingService jobListingService,
                                    Clock clock) {
        return new JobOfferService(jobOfferRepository, employerRepository, jobListingService, clock);
    }

    @Bean
    JobListingService jobListingService(JobListingRepository jobListingRepository) {
        return new JobListingService(jobListingRepository);
    }
}
