package com.example.dnatask.joboffer.job;

import com.example.dnatask.joboffer.CreateJobOfferRequest;
import com.example.dnatask.joboffer.job.model.Employer;
import com.example.dnatask.joboffer.job.model.JobOffer;
import com.example.dnatask.joboffer.listing.JobListingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.time.Clock;
import java.time.Instant;

public class JobOfferService {

    private static final Logger log = LoggerFactory.getLogger(JobOfferService.class);

    private final JobOfferRepository jobOfferRepository;

    private final EmployerRepository employerRepository;

    private final JobListingService jobListingService;

    private final Clock clock;

    public JobOfferService(JobOfferRepository jobOfferRepository,
                           EmployerRepository employerRepository,
                           JobListingService jobListingService,
                           Clock clock) {
        this.jobOfferRepository = jobOfferRepository;
        this.employerRepository = employerRepository;
        this.jobListingService = jobListingService;
        this.clock = clock;
    }

    @Transactional
    public long addJobOffer(CreateJobOfferRequest createJobOffer) {
        log.info("Adding job offer {}", createJobOffer);

        long employerId = createJobOffer.employerId();
        Employer employer = employerRepository.findById(employerId).orElseThrow();

        JobOffer jobOffer = jobOfferRepository.save(
            new JobOffer(createJobOffer.category(), createJobOffer.startDate(), createJobOffer.endDate(), employer));
        log.debug("JobOffer '{}' added", jobOffer.getId());

        if (jobOffer.isVisible(Instant.now(clock))) {
            log.info("JobOffer '{}' is visible, adding to listing", jobOffer.getId());
            jobListingService.addListing(jobOffer.toJobListing());
        }

        return jobOffer.getId();
    }

    public long addEmployer(long employerId, String name) {
        log.info("Creating Employer (employerId: '{}', name: '{}')", employerId, name);

        Employer employer = employerRepository.save(new Employer(employerId, name));
        log.debug("Employer '{}' added", employer.getId());

        return employer.getId();
    }

    public void deleteEmployer(long employerId) {
        log.info("Deleting Employer (employer: '{}')", employerId);

        // TODO should remove Employer
        // TODO should do something with JobOffer
        // TODO should remove JobListing
    }

    public void updateEmployer(long employerId, String newName) {
        log.info("Updating Employer (employerId: '{}', newName: '{}')", employerId, newName);
        Employer employer = employerRepository.findById(employerId).orElseThrow();
        employer.setName(employer.getName());
        employerRepository.save(employer);

        // TODO should update JobListings
    }
}
