package com.example.englishapp.services.impl;

import com.example.englishapp.exeptions.NotFoundException;
import com.example.englishapp.models.Translation;
import com.example.englishapp.models.Vocabulary;
import com.example.englishapp.models.VocabularyRange;
import com.example.englishapp.repositories.TranslationRepository;
import com.example.englishapp.repositories.VocabularyRangeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VocabularyRangeServiceImplTest {

    @Mock
    private VocabularyRangeRepository vocabularyRangeRepository;

    @Mock
    private TranslationRepository translationRepository;

    @InjectMocks
    private VocabularyRangeServiceImpl vocabularyRangeService;

    @Test
    void testGetVocabularyRangeByTranslationId_ExistingTranslation() {
        // given
        Integer translationId = 1;
        Translation translation = new Translation();
        translation.setId(translationId);
        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setId(1);
        translation.setVocabulary(vocabulary);

        when(translationRepository.findById(translationId)).thenReturn(Optional.of(translation));
        VocabularyRange expectedVocabularyRange = new VocabularyRange();
        when(vocabularyRangeRepository.findVocabularyRangeByVocabularyId(vocabulary.getId())).thenReturn(Optional.of(expectedVocabularyRange));

        // when
        Optional<VocabularyRange> result = vocabularyRangeService.getVocabularyRangeByTranslationId(translationId);

        // then
        assertTrue(result.isPresent());
        assertEquals(expectedVocabularyRange, result.get());
        verify(translationRepository).findById(translationId);
        verify(vocabularyRangeRepository).findVocabularyRangeByVocabularyId(vocabulary.getId());
    }

    @Test
    void testGetVocabularyRangeByTranslationId_NonExistingTranslation() {
        // given
        Integer translationId = 1;
        when(translationRepository.findById(translationId)).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> vocabularyRangeService.getVocabularyRangeByTranslationId(translationId));
        verify(translationRepository).findById(translationId);
        verifyNoInteractions(vocabularyRangeRepository);
    }
    @Test
    void testInsertVocabularyRangeToExistingWordWithoutVocabularyRange() {
        // given
        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setEnglishWord("apple");
        Integer vr = 5;

        // when
        when(vocabularyRangeRepository.findVocabularyRangeByVocabulary_EnglishWord("apple")).thenReturn(Optional.empty());
        when(vocabularyRangeRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        VocabularyRange result = vocabularyRangeService.insertVocabularyRangeToExistingWordWithoutVocabularyRange(vocabulary, vr);

        // then
        assertNotNull(result);
        assertEquals(vr, result.getVocabulary_range());
        assertEquals(vocabulary, result.getVocabulary());
        verify(vocabularyRangeRepository, times(2)).save(any());
    }

    @Test
    void testInsertVocabularyRangeToExistingWordWithExistingVocabularyRange() {
        // given
        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setEnglishWord("apple");
        Integer vr = 10;

        VocabularyRange existingVocabularyRange = new VocabularyRange();
        existingVocabularyRange.setVocabulary_range(7);

        // when
        when(vocabularyRangeRepository.findVocabularyRangeByVocabulary_EnglishWord("apple")).thenReturn(Optional.of(existingVocabularyRange));
        when(vocabularyRangeRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        VocabularyRange result = vocabularyRangeService.insertVocabularyRangeToExistingWordWithoutVocabularyRange(vocabulary, vr);

        // then
        assertNotNull(result);
        assertEquals(vr, result.getVocabulary_range());
        verify(vocabularyRangeRepository, times(1)).save(any());
    }

}
