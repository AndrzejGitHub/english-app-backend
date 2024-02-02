package com.example.englishapp.repositories;

import com.example.englishapp.models.Vocabulary;
import com.example.englishapp.models.VocabularyRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Integer> {

    Optional<Vocabulary> findVocabularyById(Integer id);

    Optional<Vocabulary> findVocabularyByEnglishWord(String query);

    List<Vocabulary> findByEnglishWordIgnoreCaseStartingWith(String term);

    List<Vocabulary> findByEnglishWordIgnoreCase(String term);


    @Query("SELECT v FROM Vocabulary v JOIN Translation t ON v.id = t.vocabulary.id WHERE t.id = :translationId")
    Optional<Vocabulary> findVocabularyByTranslationId(@Param("translationId") Integer translationId);

//    @Query("SELECT v FROM Vocabulary v JOIN VocabularyRange vr ON vr.id = v.id WHERE vr.vocabulary_range = :vocabularyRange")
//    List<Vocabulary> findVocabularyByVocabularyRangeId(@Param("vocabularyRange") Integer vocabularyRange);

    @Query("SELECT v FROM Vocabulary v " +
            "JOIN VocabularyRange vr ON v.id = vr.vocabulary.id " +
            "WHERE vr.vocabulary_range = :vocabularyRangeId")
    List<Vocabulary> findVocabulariesByVocabularyRange(@Param("vocabularyRangeId") Integer vocabularyRangeId);

}
