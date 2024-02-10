package com.example.englishapp.services.impl;

import com.example.englishapp.exeptions.ConflictException;
import com.example.englishapp.exeptions.NotFoundException;
import com.example.englishapp.models.Vocabulary;
import com.example.englishapp.models.VocabularyRange;
import com.example.englishapp.repositories.TranslationRepository;
import com.example.englishapp.repositories.VocabularyRangeRepository;
import com.example.englishapp.services.VocabularyRangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
class VocabularyRangeServiceImpl implements VocabularyRangeService {

    final VocabularyRangeRepository vocabularyRangeRepository;
    final TranslationRepository translationRepository;

    @Override
    public VocabularyRange insertVocabularyRange(VocabularyRange vocabularyRange) {
        if (Objects.isNull(vocabularyRange.getVocabulary_range()))
            return vocabularyRangeRepository.save(vocabularyRange);
        else throw new ConflictException("Conflict. VocabularyRange exist.");
    }

    @Override
    public VocabularyRange getVocabularyRangeByTranslationId(Integer id) {
        var translation = translationRepository.findById(id);
        if (translation.isPresent()) {
            Vocabulary vocabulary = translation.get().getVocabulary();
            return vocabularyRangeRepository.findVocabularyRangeByVocabularyId(vocabulary.getId())
                    .orElseGet(VocabularyRange::new);
        } else throw new NotFoundException("Translation was not found");
    }
}

