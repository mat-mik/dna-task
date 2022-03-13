package com.example.dnatask.joboffer.job;

import com.example.dnatask.joboffer.job.model.Employer;
import com.example.dnatask.joboffer.job.model.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {

    // TODO pagination - possible OOM
    List<JobOffer> findByEmployer(Employer employer);
}
