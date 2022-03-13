package com.example.dnatask.joboffer.listing;

import com.example.dnatask.joboffer.job.model.Category;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
public class JobListing {

    @Id
    private long offerId;

    private Category category;

    private String employer;

    public JobListing(long offerId, Category category, String employer) {
        this.offerId = offerId;
        this.category = category;
        this.employer = employer;
    }

    protected JobListing() {
        // JPA
    }

    public long getOfferId() {
        return offerId;
    }

    public Category getCategory() {
        return category;
    }

    public String getEmployer() {
        return employer;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", JobListing.class.getSimpleName() + "[", "]")
            .add("offerId=" + offerId)
            .add("category='" + category + "'")
            .add("employer='" + employer + "'")
            .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JobListing that = (JobListing) o;
        return offerId == that.offerId && category.equals(that.category) && employer.equals(that.employer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offerId, category, employer);
    }
}