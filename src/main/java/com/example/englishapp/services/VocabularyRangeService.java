package com.example.englishapp.services;

import com.example.englishapp.models.VocabularyRange;

public interface VocabularyRangeService {
    VocabularyRange insertVocabularyRange(VocabularyRange vocabularyRange);

    VocabularyRange getVocabularyRangeByTranslationId(Integer id);
}
