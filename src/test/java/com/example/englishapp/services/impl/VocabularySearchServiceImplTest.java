package com.example.englishapp.services.impl;

import com.example.englishapp.exeptions.NotFoundException;
import com.example.englishapp.models.Translation;
import com.example.englishapp.models.TranslationWithVocabularyRange;
import com.example.englishapp.models.Vocabulary;
import com.example.englishapp.models.VocabularyRange;
import com.example.englishapp.repositories.PartOfSpeechRepository;
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
        when(vocabularyService.searchVocabularies(query)).thenReturn(new LinkedList<>());

        // then
        assertTrue(vocabularySearchService.searchTranslationsWithVocabularyRangeByEnglishWord(query).isEmpty());
        verify(vocabularyService).searchVocabularies(query);
        verifyNoInteractions(vocabularyRangeRepository);
        verifyNoInteractions(translationRepository);
    }

    @Test
    void testFindTranslationsByVocabularyEnglishWordContaining() {
        // given
        String query = "apple";
        Translation translation1 = new Translation();
        translation1.setId(1);
        Vocabulary vocabulary1 = new Vocabulary();
        vocabulary1.setId(10);
        translation1.setVocabulary(vocabulary1);
        Translation translation2 = new Translation();
        translation2.setId(2);
        translation2.setVocabulary(null);
        when(translationRepository.findTranslationByVocabularyEnglishWordContaining(query))
                .thenReturn(Arrays.asList(translation1, translation2));
        VocabularyRange vocabularyRange1 = new VocabularyRange();
        vocabularyRange1.setId(100);
        when(vocabularyRangeRepository.findVocabularyRangeByVocabularyId(10))
                .thenReturn(Optional.of(vocabularyRange1));
        // when
        List<TranslationWithVocabularyRange> result = vocabularySearchService.findTranslationsByVocabularyEnglishWordContaining(query);
        // then
        assertEquals(1, result.size());
        assertEquals(1, result.getFirst().getTranslation().getId());
        assertEquals(100, result.getFirst().getVocabularyRange().getId());
        verify(translationRepository).findTranslationByVocabularyEnglishWordContaining(query);
        verify(vocabularyRangeRepository).findVocabularyRangeByVocabularyId(10);
        verifyNoMoreInteractions(translationRepository, vocabularyRangeRepository);
    }

}