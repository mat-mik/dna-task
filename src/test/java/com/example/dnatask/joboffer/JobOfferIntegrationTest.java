package com.example.dnatask.joboffer;

import com.example.dnatask.joboffer.job.EmployerRepository;
import com.example.dnatask.joboffer.job.JobOfferRepository;
import com.example.dnatask.joboffer.job.JobOfferService;
import com.example.dnatask.joboffer.job.model.Category;
import com.example.dnatask.joboffer.job.model.Employer;
import com.example.dnatask.joboffer.job.model.JobOffer;
import com.example.dnatask.joboffer.listing.JobListing;
import com.example.dnatask.joboffer.listing.JobListingRepository;
import com.example.dnatask.joboffer.listing.JobListingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// TODO refactor test - extract listing
@SpringBootTest
class JobOfferIntegrationTest {

    @Autowired
    private JobListingRepository jobListingRepository;

    @Autowired
    private JobOfferRepository jobOfferRepository;

    @Autowired
    private EmployerRepository employerRepository;

    private Clock fixedClock;

    private JobListingService jobListingService;

    private JobOfferService jobOfferService;

    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(Instant.parse("2022-03-13T11:39:35.000Z"), ZoneId.systemDefault());
        jobListingService = new JobOfferConfig().jobListingService(jobListingRepository);
        jobOfferService = new JobOfferConfig().jobOfferService(jobOfferRepository, employerRepository,
                                                               jobListingService, fixedClock);
    }

    @AfterEach
    void tearDown() {
        jobOfferRepository.deleteAll();
        jobListingRepository.deleteAll();
        employerRepository.deleteAll();
    }

    @Test
    void shouldAddVisibleJobOffer() {
        // given
        Employer employer = aEmployer();
        CreateJobOfferRequest createJobOffer = new CreateJobOfferRequest(Category.IT, yesterday(), tomorrow(),
                                                                         employer.getId());

        // when
        long jobOfferId = jobOfferService.addJobOffer(createJobOffer);

        // then
        jobOfferWasCreated(jobOfferId, Category.IT, yesterday(), tomorrow(), employer);
        jobListeningIsVisible(jobOfferId, Category.IT, employer.getName());
    }

    @Test
    void shouldAddNonVisibleJobOffer() {
        // given
        Employer employer = aEmployer();
        CreateJobOfferRequest createJobOffer = new CreateJobOfferRequest(Category.IT, tomorrow(), nextWeek(),
                                                                         employer.getId());

        // when
        long jobOfferId = jobOfferService.addJobOffer(createJobOffer);

        // then
        jobOfferWasCreated(jobOfferId, Category.IT, tomorrow(), nextWeek(), employer);
        jobListeningDoesNotContain(jobOfferId);
    }

    @Test
    void shouldUpdateListeningWhenEmployerNameChanges() {
        // TODO implement me
    }

    @Test
    void shouldHandleEmployerRemoval() {
        // TODO implement me
    }

    @Test
    void shouldFindAllVisibleJobListing() {
        // given DNA employer with two visible offers
        Employer dnaEmployer = aEmployerWith(1, "DNA");
        long dnaVisibleITOfferId = visibleOfferFor(dnaEmployer, Category.IT);
        long dnaVisibleOfficeOfferId = visibleOfferFor(dnaEmployer, Category.OFFICE);

        // and Avenga employer with one visible and one not visible offer
        Employer avengaEmployer = aEmployerWith(2, "Avenga");
        long avengaVisibleShopOfferId = visibleOfferFor(avengaEmployer, Category.SHOP_ASSISTANT);
        long avengaNotVisibleCourierOfferId = notVisibleOfferFor(avengaEmployer, Category.COURIER);

        // expect
        jobListeningContains(dnaVisibleITOfferId, dnaVisibleOfficeOfferId, avengaVisibleShopOfferId);
        jobListeningDoesNotContain(avengaNotVisibleCourierOfferId);
    }

    @Test
    void shouldFindJobListingByEmployer() {
        // given two employer with visible offers
        Employer dnaEmployer = aEmployerWith(1, "DNA");
        long dnaVisibleITOfferId = visibleOfferFor(dnaEmployer, Category.IT);

        Employer avengaEmployer = aEmployerWith(2, "Avenga");
        long avengaVisibleShopOfferId = visibleOfferFor(avengaEmployer, Category.SHOP_ASSISTANT);

        // when
        List<JobListing> listings = jobListingService.findBy(null, "DNA");
        jobListeningContains(listings, dnaVisibleITOfferId);
        jobListeningDoesNotContain(listings, avengaVisibleShopOfferId);
    }

    @Test
    void shouldFindJobListingByCategory() {
        // given two employer with visible offers
        Employer dnaEmployer = aEmployerWith(1, "DNA");
        long dnaVisibleITOfferId = visibleOfferFor(dnaEmployer, Category.IT);

        Employer avengaEmployer = aEmployerWith(2, "Avenga");
        long avengaVisibleShopOfferId = visibleOfferFor(avengaEmployer, Category.SHOP_ASSISTANT);

        // when
        List<JobListing> listings = jobListingService.findBy(Category.IT, null);
        jobListeningContains(listings, dnaVisibleITOfferId);
        jobListeningDoesNotContain(listings, avengaVisibleShopOfferId);
    }

    @Test
    void shouldFindJobListingByCategoryAndEmployer() {
        // given two employer with visible offers
        Employer dnaEmployer = aEmployerWith(1, "DNA");
        long dnaVisibleITOfferId = visibleOfferFor(dnaEmployer, Category.IT);
        long dnaVisibleShopAssistantOfferId = visibleOfferFor(dnaEmployer, Category.SHOP_ASSISTANT);

        Employer avengaEmployer = aEmployerWith(2, "Avenga");
        long avengaVisibleShopOfferId = visibleOfferFor(avengaEmployer, Category.SHOP_ASSISTANT);

        // when
        List<JobListing> listings = jobListingService.findBy(Category.IT, "DNA");
        jobListeningContains(listings, dnaVisibleITOfferId);
        jobListeningDoesNotContain(listings, dnaVisibleShopAssistantOfferId, avengaVisibleShopOfferId);
    }

    private Employer aEmployer() {
        return aEmployerWith(1, "DNA");
    }

    private Employer aEmployerWith(long id, String name) {
        return employerRepository.save(new Employer(id, name));
    }

    private Instant yesterday() {
        return Instant.now(fixedClock).minus(1, ChronoUnit.DAYS);
    }

    private Instant tomorrow() {
        return Instant.now(fixedClock).plus(1, ChronoUnit.DAYS);
    }

    private Instant nextWeek() {
        return Instant.now(fixedClock).plus(7, ChronoUnit.DAYS);
    }

    private void jobOfferWasCreated(long jobOfferId, Category category, Instant startDate, Instant endDate,
                                    Employer employer) {

        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId).orElseThrow();
        assertThat(jobOffer.getCategory()).isEqualTo(category);
        assertThat(jobOffer.getStartDate()).isEqualTo(startDate);
        assertThat(jobOffer.getEndDate()).isEqualTo(endDate);
        assertThat(jobOffer.getEmployer()).isEqualTo(employer);

    }

    private void jobListeningIsVisible(long jobOfferId, Category category, String employerName) {
        List<JobListing> jobListings = jobListingService.findBy(null, null);
        assertThat(jobListings)
            .containsExactly(new JobListing(jobOfferId, category, employerName));
    }

    private void jobListeningContains(Long... jobOfferIds) {
        List<JobListing> jobListings = jobListingService.findBy(null, null);
        jobListeningContains(jobListings, jobOfferIds);
    }

    private void jobListeningContains(List<JobListing> jobListings, Long... jobOfferIds) {
        List<Long> jobListeningIds = jobListings.stream()
                                                .map(JobListing::getOfferId)
                                                .toList();
        assertThat(jobListeningIds).containsExactlyInAnyOrder(jobOfferIds);
    }

    private void jobListeningDoesNotContain(Long... jobOfferIds) {
        List<JobListing> jobListings = jobListingService.findBy(null, null);
        jobListeningDoesNotContain(jobListings, jobOfferIds);
    }

    private void jobListeningDoesNotContain(List<JobListing> jobListings, Long... jobOfferIds) {
        List<Long> jobListeningIds = jobListings.stream()
                                                .map(JobListing::getOfferId)
                                                .toList();
        assertThat(jobListeningIds).doesNotContain(jobOfferIds);
    }

    private long visibleOfferFor(Employer employer, Category category) {
        CreateJobOfferRequest createJobOffer = new CreateJobOfferRequest(category, yesterday(), tomorrow(),
                                                                         employer.getId());

        return jobOfferService.addJobOffer(createJobOffer);
    }

    private long notVisibleOfferFor(Employer employer, Category category) {
        CreateJobOfferRequest createJobOffer = new CreateJobOfferRequest(category, tomorrow(), nextWeek(),
                                                                         employer.getId());

        return jobOfferService.addJobOffer(createJobOffer);
    }
}