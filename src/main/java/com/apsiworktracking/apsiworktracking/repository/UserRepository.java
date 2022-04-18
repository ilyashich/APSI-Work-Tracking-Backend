package com.apsiworktracking.apsiworktracking.repository;

import com.apsiworktracking.apsiworktracking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    User findByUsername(String username);

}
