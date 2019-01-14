package com.hassan.auth.repository;

import com.hassan.auth.model.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {
    public List<EmailAuth> findByEmail(String email);
}
