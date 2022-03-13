package com.example.dnatask.joboffer.listing;

import com.example.dnatask.joboffer.job.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobListingRepository extends JpaRepository<JobListing, Long> {

    List<JobListing> findAllByCategoryAndEmployer(Category category, String employer);

    List<JobListing> findAllByCategory(Category category);

    List<JobListing> findAllByEmployer(String employer);
}
