package com.example.englishapp.repositories;

import com.example.englishapp.models.VocabularyRange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VocabularyRangeRepository extends JpaRepository<VocabularyRange, Integer> {
    Optional<VocabularyRange> findVocabularyRangeByVocabularyId(Integer id);
}
