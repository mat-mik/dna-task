package com.example.dnatask.joboffer.job;

import com.example.dnatask.joboffer.job.model.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long> {

    Optional<Employer> findByName(String name);
}
