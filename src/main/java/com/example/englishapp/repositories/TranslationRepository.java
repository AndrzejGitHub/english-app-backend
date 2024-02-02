package com.example.englishapp.repositories;

import com.example.englishapp.models.Translation;
import com.example.englishapp.models.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TranslationRepository extends JpaRepository<Translation, Integer> {

    List<Translation> findTranslationsByVocabularyId(Integer id);

    List<Translation> findTranslationByVocabulary(Vocabulary vocabulary);

    List<Translation> findTranslationByVocabularyOrderByPartOfSpeech(Vocabulary vocabulary);

    List<Translation> findTranslationByVocabularyEnglishWordContaining(String term);

//    @Query("SELECT t FROM Translation t WHERE t.vocabulary.vocabularyRange.id = :rangeId")
//    List<Translation> findTranslationsByVocabularyContainingVocabularyRange(@Param("rangeId") Integer rangeId);

//
//    @Query("SELECT t FROM Translation t JOIN t.vocabulary v JOIN v.vocabularyRange vr WHERE vr.id = :rangeId")
//    List<Translation> findTranslationsByVocabularyContainingVocabularyRange(@Param("rangeId") Integer rangeId);

//    @Query("SELECT vr FROM VocabularyRange vr " +
//            "JOIN vr.vocabulary v " +
//            "WHERE vr.vocabulary_range = :vocabularyRangeId")
//    List<Vocabulary> findVocabulariesByVocabularyRange(@Param("vocabularyId") Integer vocabularyRangeId);






    void deleteByPartOfSpeech_Id(Integer part_of_speech_id);
}
