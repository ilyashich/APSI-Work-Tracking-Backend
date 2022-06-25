package com.apsiworktracking.apsiworktracking.repository;

import com.apsiworktracking.apsiworktracking.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface ProjectDetailsRepository extends JpaRepository<ProjectDetail, ProjectDetailKey> {

    Set<ProjectDetail> findProjectDetailsByUserAndRole(User user, ProjectRoleEnum role);

    Set<ProjectDetail> findProjectDetailsByUser(User user);

}
