package com.example.englishapp.services;

import com.example.englishapp.models.Translation;
import com.example.englishapp.models.Vocabulary;
import com.example.englishapp.repositories.PartOfSpeechRepository;
import com.example.englishapp.repositories.TranslationRepository;
import com.example.englishapp.repositories.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VocabularySearchService {

    final VocabularyRepository vocabularyRepository;
    final TranslationRepository translationRepository;
    final PartOfSpeechRepository partOfSpeechRepository;

    public List<Translation> searchVocabularyAndGiveResponseTranslations(String term) {
        var vocabulary = vocabularyRepository.findVocabularyByEnglishWord(term);
        if (vocabulary.isPresent()) {
            return translationRepository.findTranslationByVocabulary(vocabulary.get());
        } else return List.of();
    }

    public List<Translation> searchTranslationsByVocabularyOrderByPartOfSpeech(String term) {
        var vocabulary = vocabularyRepository.findByEnglishWordIgnoreCase(term).getFirst();
        return translationRepository.findTranslationByVocabularyOrderByPartOfSpeech(vocabulary);
    }

//    public List<Vocabulary> searchVocabulariesByRangeId(Integer rangeId) {
//        return vocabularyRepository.findAllByVocabularyRangeId(rangeId);
//    }

    public List<Translation> searchTranslationsByEnglishWord(String term) {
        return translationRepository.findTranslationByVocabularyEnglishWordContaining(term);
    }

//    public List<Translation> searchTranslationsByRangeId(Integer rangeId) {
//        return translationRepository.findTranslationsByVocabularyContainingVocabularyRange(rangeId);
//    }
}
