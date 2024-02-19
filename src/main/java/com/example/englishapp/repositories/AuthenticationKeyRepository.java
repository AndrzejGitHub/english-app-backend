package com.example.englishapp.repositories;

import com.example.englishapp.models.AuthenticationKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationKeyRepository extends JpaRepository<AuthenticationKey, Integer> {
}