package com.example.englishapp.services;

import com.example.englishapp.models.Vocabulary;
import com.example.englishapp.models.VocabularyRange;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface VocabularyRangeService {

    Optional<VocabularyRange> getVocabularyRangeByTranslationId(Integer id);

    Optional<VocabularyRange> getVocabularyRangeByEnglishWord(String query);

    @Transactional
    VocabularyRange insertVocabularyRangeToExistingWordWithoutVocabularyRange(Vocabulary vocabulary, Integer vocabularyRange);

    Integer getMaxVocabularyRange();

}
