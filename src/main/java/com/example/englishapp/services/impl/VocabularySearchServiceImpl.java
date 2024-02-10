package com.example.englishapp.services.impl;

import com.example.englishapp.exeptions.NotFoundException;
import com.example.englishapp.models.Translation;
import com.example.englishapp.models.TranslationWithVocabularyRange;
import com.example.englishapp.models.VocabularyRange;
import com.example.englishapp.repositories.PartOfSpeechRepository;
import com.example.englishapp.repositories.TranslationRepository;
import com.example.englishapp.repositories.VocabularyRangeRepository;
import com.example.englishapp.repositories.VocabularyRepository;
import com.example.englishapp.services.VocabularySearchService;
import com.example.englishapp.services.VocabularyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.toCollection;

@Service
@RequiredArgsConstructor
class VocabularySearchServiceImpl implements VocabularySearchService {

    final VocabularyRepository vocabularyRepository;
    final TranslationRepository translationRepository;
    final PartOfSpeechRepository partOfSpeechRepository;
    final VocabularyRangeRepository vocabularyRangeRepository;
    final VocabularyService vocabularyService;


    @Override
    public List<Translation> searchVocabularyAndGiveResponseTranslations(String term) {
        if (vocabularyRepository.findVocabularyByEnglishWord(term).isPresent()) {
            return translationRepository.findTranslationByVocabulary(
                    vocabularyRepository.findVocabularyByEnglishWord(term).get());
        } else throw new NotFoundException("Vocabulary was not found");
    }

    @Override
    public List<Translation> searchTranslationsByVocabularyOrderByPartOfSpeech(String term) {
        if (vocabularyRepository.findByEnglishWordIgnoreCase(term).isEmpty())
            throw new NotFoundException("Vocabulary was not found");
        else
            return translationRepository.findTranslationByVocabularyOrderByPartOfSpeech(
                    vocabularyRepository.findByEnglishWordIgnoreCase(term).getFirst());
    }

    @Override
    public List<Translation> searchTranslationsByEnglishWord(String term) {
        return translationRepository.findTranslationByVocabularyEnglishWordContaining(term);
    }

    @Override
    public List<Translation> searchTranslationsByRangeId(Integer rangeId) {
        return translationRepository.findTranslationsByVocabularyContainingVocabularyRange(rangeId);
    }

    @Override
    public List<TranslationWithVocabularyRange> searchTranslationsWithVocabularyRangeByEnglishWord(String query) {
        var vocabularies = vocabularyService.searchVocabularies(query);
        if (vocabularies.isEmpty())
            return List.of();
        var firstVocabulary = vocabularies.getFirst();
        var vocabularyRange = vocabularyRangeRepository.findVocabularyRangeByVocabularyId(firstVocabulary.getId());
        if (vocabularyRange.isEmpty())
            return findTranslationByVocabularyEnglishWordContaining(query);
        Set<TranslationWithVocabularyRange> combinedResultsSet = vocabularyRange.map(vRange -> {
            var initialWordTranslations = findTranslationByVocabularyEnglishWordContaining(firstVocabulary.getEnglishWord());
            var otherTranslations = vocabularyService.findVocabulariesByVocabularyRange(vRange.getVocabulary_range())
                    .stream()
                    .flatMap(vocabulary ->
                            findTranslationByVocabularyEnglishWordContaining(vocabulary.getEnglishWord()).stream())
                    .sorted(Comparator.comparing(translation -> translation.getTranslation().getVocabulary().getEnglishWord()))
                    .collect(toCollection(LinkedHashSet::new));
            Set<TranslationWithVocabularyRange> orderedResultsSet = new LinkedHashSet<>(initialWordTranslations);
            orderedResultsSet.addAll(otherTranslations);
            return orderedResultsSet;
        }).orElse(new LinkedHashSet<>());
        Set<String> uniquePolishMeanings = new HashSet<>();
        List<TranslationWithVocabularyRange> uniqueTranslations = new ArrayList<>();
        for (TranslationWithVocabularyRange translation : combinedResultsSet) {
            if (!uniquePolishMeanings.contains(translation.getTranslation().getTranslationVariant().getPolishMeaning())) {
                uniquePolishMeanings.add(translation.getTranslation().getTranslationVariant().getPolishMeaning());
                uniqueTranslations.add(translation);
            }
        }
        return uniqueTranslations;

    }

    List<TranslationWithVocabularyRange> findTranslationByVocabularyEnglishWordContaining(String query) {
        return translationRepository.findTranslationByVocabularyEnglishWordContaining(query)
                .stream()
                .map(translation -> {
                    VocabularyRange vr = vocabularyRangeRepository.findVocabularyRangeByVocabularyId(translation.getVocabulary().getId())
                            .orElse(null);
                    return new TranslationWithVocabularyRange(translation, vr);
                })
                .toList();
    }

}
