package com.example.dnatask.joboffer.listing;

import com.example.dnatask.joboffer.job.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static java.util.Objects.nonNull;

public class JobListingService {

    private static final Logger log = LoggerFactory.getLogger(JobListingService.class);

    private final JobListingRepository jobListingRepository;

    public JobListingService(
        JobListingRepository jobListingRepository) {
        this.jobListingRepository = jobListingRepository;
    }

    public void addListing(JobListing listing) {
        log.info("Adding listing: {}", listing);
        jobListingRepository.save(listing);
    }

    public void deleteListing(long offerId) {
        this.deleteListings(Set.of(offerId));
    }

    public void deleteListings(Collection<Long> offerIds) {
        log.info("Deleting listings (offerIds: {})", offerIds);
        jobListingRepository.deleteAllById(offerIds);
    }

    // TODO refactor to more sophisticated solution, e.g. Specification
    public List<JobListing> findBy(@Nullable Category category, @Nullable String employerName) {
        log.info("Listing job offers by (category: '{}', employerName: '{}')", category, employerName);
        if (nonNull(category) && nonNull(employerName)) {
            return jobListingRepository.findAllByCategoryAndEmployer(category, employerName);
        } else if (nonNull(category)) {
            return jobListingRepository.findAllByCategory(category);
        } else if (nonNull(employerName)) {
            return jobListingRepository.findAllByEmployer(employerName);
        } else {
            return jobListingRepository.findAll();
        }
    }
}
