package com.example.dnatask.joboffer.job.model;

import com.example.dnatask.joboffer.listing.JobListing;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.Instant;

import static java.util.Objects.requireNonNull;

@Entity
public class JobOffer {

    @Id
    @GeneratedValue
    private long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    private Instant startDate;

    private Instant endDate;

    @OneToOne
    @JoinColumn(name = "employee_id",
                referencedColumnName = "id")
    private Employer employer;

    protected JobOffer() {
        // JPA
    }

    public JobOffer(Category category, Instant startDate, Instant endDate, Employer employer) {
        this.category = requireNonNull(category, "category must be set");
        this.startDate = requireNonNull(startDate, "startDate must be set");
        this.endDate = requireNonNull(endDate, "endDate must be set");
        this.employer = requireNonNull(employer, "employer must be set");
    }

    public long getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public Employer getEmployer() {
        return employer;
    }

    public boolean isVisible(Instant now) {
        return now.compareTo(startDate) >= 0 && now.compareTo(endDate) <= 0;
    }

    public JobListing toJobListing() {
        return new JobListing(id, category, employer.getName());
    }

}
