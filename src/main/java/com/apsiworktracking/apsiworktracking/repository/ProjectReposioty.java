package com.apsiworktracking.apsiworktracking.repository;

import com.apsiworktracking.apsiworktracking.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectReposioty extends JpaRepository<Project, Long>
{


}