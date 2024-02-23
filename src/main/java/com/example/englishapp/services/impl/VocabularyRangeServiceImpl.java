package com.example.englishapp.services.impl;

import com.example.englishapp.exeptions.NotFoundException;
import com.example.englishapp.models.Translation;
import com.example.englishapp.models.Vocabulary;
import com.example.englishapp.models.VocabularyRange;
import com.example.englishapp.repositories.TranslationRepository;
import com.example.englishapp.repositories.VocabularyRangeRepository;
import com.example.englishapp.services.VocabularyRangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class VocabularyRangeServiceImpl implements VocabularyRangeService {

    private final VocabularyRangeRepository vocabularyRangeRepository;
    private final TranslationRepository translationRepository;

    @Override
    public Optional<VocabularyRange> getVocabularyRangeByTranslationId(Integer id) {
        return translationRepository.findById(id)
                .map(Translation::getVocabulary)
                .map(vocabulary -> vocabularyRangeRepository.findVocabularyRangeByVocabularyId(vocabulary.getId()))
                .orElseThrow(() -> new NotFoundException("Translation was not found"));
    }

    @Override
    public Optional<VocabularyRange> getVocabularyRangeByEnglishWord(String query) {
        return Optional.ofNullable(vocabularyRangeRepository.findVocabularyRangeByVocabulary_EnglishWord(query))
                .orElseThrow(() -> new NotFoundException("VocabularyRange was not found"));
    }

    @Override
    @Transactional
    public VocabularyRange insertVocabularyRangeToExistingWordWithoutVocabularyRange(Vocabulary vocabulary, Integer vr) {
        return vocabularyRangeRepository.findVocabularyRangeByVocabulary_EnglishWord(vocabulary.getEnglishWord())
                .map(
                        vocabularyRange -> {
                            vocabularyRange.setVocabulary_range(vr);
                            return vocabularyRangeRepository.save(vocabularyRange);
                        }
                ).orElseGet(
                        () -> {
                            VocabularyRange newVocabularyRangeValue = new VocabularyRange();
                            newVocabularyRangeValue.setVocabulary_range(vr);
                            vocabularyRangeRepository.save(newVocabularyRangeValue);
                            newVocabularyRangeValue.setVocabulary(vocabulary);
                            return vocabularyRangeRepository.save(newVocabularyRangeValue);
                        }
                );
    }

    @Override
    public Integer getMaxVocabularyRange() {
        Optional<Integer> optionalMaxVocabularyRange = vocabularyRangeRepository.findMaxVocabularyRange();
        return optionalMaxVocabularyRange.orElse(0) + 1;
    }

}

