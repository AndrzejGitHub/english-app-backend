package com.example.englishapp.services.impl;

import com.example.englishapp.exeptions.ConflictException;
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
    void testInsertVocabularyRange_Success() {
        // given
        VocabularyRange vocabularyRange = new VocabularyRange();
        when(vocabularyRangeRepository.save(vocabularyRange)).thenReturn(vocabularyRange);

        // when
        VocabularyRange result = vocabularyRangeService.insertVocabularyRange(vocabularyRange);

        // then
        assertNotNull(result);
        assertEquals(vocabularyRange, result);
        verify(vocabularyRangeRepository).save(vocabularyRange);
    }

    @Test
    void testInsertVocabularyRange_Conflict() {
        // given
        VocabularyRange vocabularyRange = new VocabularyRange();
        vocabularyRange.setVocabulary_range(1);
        // then
        assertThrows(ConflictException.class, () -> vocabularyRangeService.insertVocabularyRange(vocabularyRange));
        verifyNoInteractions(vocabularyRangeRepository);
    }

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
        VocabularyRange result = vocabularyRangeService.getVocabularyRangeByTranslationId(translationId);

        // then
        assertNotNull(result);
        assertEquals(expectedVocabularyRange, result);
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
}
