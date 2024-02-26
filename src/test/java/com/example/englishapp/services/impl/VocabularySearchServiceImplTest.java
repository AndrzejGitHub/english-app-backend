package com.example.englishapp.services.impl;

import com.example.englishapp.exeptions.NotFoundException;
import com.example.englishapp.models.*;
import com.example.englishapp.repositories.TranslationRepository;
import com.example.englishapp.repositories.VocabularyRangeRepository;
import com.example.englishapp.repositories.VocabularyRepository;
import com.example.englishapp.services.VocabularyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VocabularySearchServiceImplTest {

    @Mock
    private VocabularyRepository vocabularyRepository;

    @Mock
    private TranslationRepository translationRepository;

    @Mock
    private VocabularyRangeRepository vocabularyRangeRepository;
    @Mock
    private VocabularyService vocabularyService;

    @InjectMocks
    private VocabularySearchServiceImpl vocabularySearchService;

    @Test
    void testSearchVocabularyAndGiveResponseTranslations_VocabularyExists() {
        // given
        String term = "apple";
        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setId(1);
        when(vocabularyRepository.findVocabularyByEnglishWord(term)).thenReturn(Optional.of(vocabulary));
        List<Translation> expectedTranslations = new ArrayList<>();
        when(translationRepository.findTranslationByVocabulary(vocabulary)).thenReturn(expectedTranslations);

        // when
        List<Translation> result = vocabularySearchService.searchVocabularyAndGiveResponseTranslations(term);

        // then
        assertEquals(expectedTranslations, result);
//        verify(vocabularyRepository).findVocabularyByEnglishWord(term);
        verify(translationRepository).findTranslationByVocabulary(vocabulary);
    }

    @Test
    void testSearchVocabularyAndGiveResponseTranslations_VocabularyNotFound() {
        // given
        String term = "apple";
        when(vocabularyRepository.findVocabularyByEnglishWord(term)).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> vocabularySearchService.searchVocabularyAndGiveResponseTranslations(term));
        verify(vocabularyRepository).findVocabularyByEnglishWord(term);
        verifyNoInteractions(translationRepository);
    }

    @Test
    void testSearchTranslationsByVocabularyOrderByPartOfSpeech_VocabularyExists() {
        // given
        String term = "apple";
        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setId(1);
        when(vocabularyRepository.findByEnglishWordIgnoreCase(term)).thenReturn(List.of(vocabulary));
        List<Translation> expectedTranslations = new ArrayList<>();
        when(translationRepository.findTranslationByVocabularyOrderByPartOfSpeech(vocabulary)).thenReturn(expectedTranslations);

        // when
        List<Translation> result = vocabularySearchService.searchTranslationsByVocabularyOrderByPartOfSpeech(term);

        // then
        assertEquals(expectedTranslations, result);
//        verify(vocabularyRepository).findByEnglishWordIgnoreCase(term);
        verify(translationRepository).findTranslationByVocabularyOrderByPartOfSpeech(vocabulary);
    }

    @Test
    void testSearchTranslationsByVocabularyOrderByPartOfSpeech_VocabularyNotFound() {
        // given
        String term = "apple";
        when(vocabularyRepository.findByEnglishWordIgnoreCase(term)).thenReturn(List.of());

        // then
        assertThrows(NotFoundException.class, () -> vocabularySearchService.searchTranslationsByVocabularyOrderByPartOfSpeech(term));
        verify(vocabularyRepository).findByEnglishWordIgnoreCase(term);
        verifyNoInteractions(translationRepository);
    }

    @Test
    void testSearchTranslationsByEnglishWord() {
        // given
        String term = "apple";
        List<Translation> expectedTranslations = new ArrayList<>();
        when(translationRepository.findTranslationByVocabularyEnglishWordContaining(term)).thenReturn(expectedTranslations);

        // when
        List<Translation> result = vocabularySearchService.searchTranslationsByEnglishWord(term);

        // then
        assertEquals(expectedTranslations, result);
        verify(translationRepository).findTranslationByVocabularyEnglishWordContaining(term);
    }

    @Test
    void testSearchTranslationsByRangeId() {
        // given
        Integer rangeId = 1;
        List<Translation> expectedTranslations = new ArrayList<>();
        when(translationRepository.findTranslationsByVocabularyContainingVocabularyRange(rangeId)).thenReturn(expectedTranslations);

        // when
        List<Translation> result = vocabularySearchService.searchTranslationsByRangeId(rangeId);

        // then
        assertEquals(expectedTranslations, result);
        verify(translationRepository).findTranslationsByVocabularyContainingVocabularyRange(rangeId);
    }

    @Test
    void testSearchTranslationsWithVocabularyRangeByEnglishWord_VocabularyNotFound() {
        // given
        String query = "apple";
        when(vocabularyService.getVocabulariesIgnoreCase(query)).thenReturn(new LinkedList<>());

        // then
        assertTrue(vocabularySearchService.searchTranslationsWithVocabularyRangeByEnglishWord(query).isEmpty());
        verify(vocabularyService).getVocabulariesIgnoreCase(query);
        verifyNoInteractions(vocabularyRangeRepository);
        verifyNoInteractions(translationRepository);
    }

    @Test
    void testSearchTranslationsWithVocabularyRangeByEnglishWord_VocabularyFound() {
        // given
        String query = "apple";

        // when
        List<TranslationWithVocabularyRange> translationsWithVocabularyRange = vocabularySearchService.searchTranslationsWithVocabularyRangeByEnglishWord(query);

        // then
        Set<String> uniquePolishMeanings = new HashSet<>();
        for (TranslationWithVocabularyRange translationWithVocabularyRange : translationsWithVocabularyRange) {
            String polishMeaning = translationWithVocabularyRange.getTranslation().getTranslationVariant().getPolishMeaning();
            assertFalse(uniquePolishMeanings.contains(polishMeaning), "Duplicate of translation: " + polishMeaning);
            uniquePolishMeanings.add(polishMeaning);
        }
    }

}