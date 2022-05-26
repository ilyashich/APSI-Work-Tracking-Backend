package com.apsiworktracking.apsiworktracking.repository;

import com.apsiworktracking.apsiworktracking.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
}
