package com.example.englishapp.repositories;

import com.example.englishapp.models.TranslationVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TranslationVariantRepository extends JpaRepository<TranslationVariant, Integer> {
    @Query("SELECT tv FROM TranslationVariant tv JOIN Translation t ON tv.id = t.translationVariant.id WHERE t.id = :translationId")
    Optional<TranslationVariant> findTranslationVariantByTranslationId(@Param("translationId") Integer translationId);

}
