package com.example.englishapp.repositories;

import com.example.englishapp.models.VocabularyRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VocabularyRangeRepository extends JpaRepository<VocabularyRange, Integer> {
    Optional<VocabularyRange> findVocabularyRangeByVocabularyId(Integer id);

    Optional<VocabularyRange> findVocabularyRangeByVocabulary_EnglishWord(String vocabulary);

    @Query("SELECT MAX(vr.vocabulary_range) FROM VocabularyRange vr")
    Optional<Integer> findMaxVocabularyRange();

}
