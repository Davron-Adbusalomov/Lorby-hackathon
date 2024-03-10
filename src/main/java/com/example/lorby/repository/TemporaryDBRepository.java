package com.example.lorby.repository;

import com.example.lorby.model.TemporaryUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemporaryDBRepository extends JpaRepository<TemporaryUser, Long> {
    Optional<TemporaryUser> findByUsername(String username);
}
