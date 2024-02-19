package com.example.englishapp.services.impl;

import com.example.englishapp.exeptions.ConflictException;
import com.example.englishapp.exeptions.NotFoundException;
import com.example.englishapp.models.*;
import com.example.englishapp.repositories.*;
import com.example.englishapp.services.PartOfSpeechService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TranslationServiceImplTest {

    @Mock
    private TranslationRepository translationRepository;

    @Mock
    private VocabularyRepository vocabularyRepository;

    @Mock
    private TranslationVariantRepository translationVariantRepository;

    @Mock
    private VocabularyRangeRepository vocabularyRangeRepository;

    @Mock
    private PartOfSpeechRepository partOfSpeechRepository;

    @Mock
    private PartOfSpeechService partOfSpeechService;
    @Mock
    private TranslationServiceImpl translationService;

    @BeforeEach
    public void setUp() {
        translationService = new TranslationServiceImpl(
                translationRepository,
                vocabularyRepository,
                vocabularyRangeRepository,
                translationVariantRepository,
                partOfSpeechRepository,
                partOfSpeechService
        );
    }

    @Test
    void testGetTranslations() {
        // given
        List<Translation> expectedTranslations = new ArrayList<>();
        expectedTranslations.add(new Translation());
        expectedTranslations.add(new Translation());

        when(translationRepository.findAll()).thenReturn(expectedTranslations);

        // when
        List<Translation> actualTranslations = translationService.getTranslations();

        // then
        assertEquals(expectedTranslations.size(), actualTranslations.size());
    }

    @Test
    void testGetTranslation_WhenTranslationExists() {
        // given
        Integer translationId = 1;
        Translation expectedTranslation = new Translation();
        expectedTranslation.setId(translationId);

        when(translationRepository.findById(translationId)).thenReturn(Optional.of(expectedTranslation));

        // when
        Translation actualTranslation = translationService.getTranslation(translationId);

        // then
        assertEquals(expectedTranslation, actualTranslation);
    }

    @Test
    void testGetTranslation_WhenTranslationDoesNotExist_NotFound() {
        // given
        Integer nonExistentTranslationId = 99;

        when(translationRepository.findById(nonExistentTranslationId)).thenReturn(Optional.empty());

        // when and then
        assertThrows(NotFoundException.class, () -> translationService.getTranslation(nonExistentTranslationId));
    }

    @Test
    void testGetTranslationsByVocabularyId() {
        // given
        Integer vocabularyId = 123;
        List<Translation> expectedTranslations = new ArrayList<>();
        expectedTranslations.add(new Translation());

        when(translationRepository.findTranslationsByVocabularyId(vocabularyId))
                .thenReturn(expectedTranslations);

        // when
        List<Translation> actualTranslations = translationService.getTranslationsByVocabularyId(vocabularyId);

        // then
        assertEquals(expectedTranslations, actualTranslations);
    }

    @Test
    void testGetTranslationsByVocabularyId_NotFound() {
        // given
        Integer vocabularyId = 456;

        when(translationRepository.findTranslationsByVocabularyId(vocabularyId))
                .thenReturn(new ArrayList<>());

        // when and then
        assertThrows(NotFoundException.class,
                () -> translationService.getTranslationsByVocabularyId(vocabularyId),
                "English word translation was not found");
    }

    @Test
    void testGetTranslationsByVocabularyRangeIdWithVocabularyRange() {
        // Given
        Integer vocabularyRangeId = 123;
        List<Vocabulary> expectedVocabularies = new ArrayList<>();
        expectedVocabularies.add(new Vocabulary(1, "apple", "apple.jpg"));
        expectedVocabularies.add(new Vocabulary(2, "banana", "banana.jpg"));

        when(vocabularyRepository.findVocabulariesByVocabularyRange(vocabularyRangeId))
                .thenReturn(expectedVocabularies);

        Vocabulary vocabulary1 = new Vocabulary(1, "apple", "apple.jpg");
        Vocabulary vocabulary2 = new Vocabulary(2, "banana", "banana.jpg");

        when(vocabularyRepository.findVocabularyById(1)).thenReturn(Optional.of(vocabulary1));
        when(vocabularyRepository.findVocabularyById(2)).thenReturn(Optional.of(vocabulary2));

        List<Translation> expectedTranslations = new ArrayList<>();
        expectedTranslations.add(new Translation(1, new PartOfSpeech(), vocabulary1, new TranslationVariant(1, "jabłko")));
        expectedTranslations.add(new Translation(2, new PartOfSpeech(), vocabulary2, new TranslationVariant(2, "banan")));

        when(translationRepository.findTranslationByVocabulary(vocabulary1)).thenReturn(expectedTranslations.subList(0, 1));
        when(translationRepository.findTranslationByVocabulary(vocabulary2)).thenReturn(expectedTranslations.subList(1, 2));

        VocabularyRange vocabularyRange = new VocabularyRange(1, 1, vocabulary1);
        when(vocabularyRangeRepository.findVocabularyRangeByVocabularyId(1)).thenReturn(Optional.of(vocabularyRange));
        when(vocabularyRangeRepository.findVocabularyRangeByVocabularyId(2)).thenReturn(Optional.of(vocabularyRange));

        // When
        List<TranslationWithVocabularyRange> actualTranslations = translationService.getTranslationsByVocabularyRangeIdWithVocabularyRange(vocabularyRangeId);

        // Then
        assertEquals(2, actualTranslations.size());
        assertEquals("jabłko", actualTranslations.get(0).getTranslation().getTranslationVariant().getPolishMeaning());
        assertEquals("banan", actualTranslations.get(1).getTranslation().getTranslationVariant().getPolishMeaning());
    }

    @Test
    void testGetTranslationsByVocabularyRangeIdWithVocabularyRange_NotFound() {
        // Given
        Integer vocabularyRangeId = 456;
        when(vocabularyRepository.findVocabulariesByVocabularyRange(vocabularyRangeId))
                .thenReturn(new ArrayList<>());

        // When and Then
        assertThrows(NotFoundException.class,
                () -> translationService.getTranslationsByVocabularyRangeIdWithVocabularyRange(vocabularyRangeId),
                "Vocabulary Range was not found");
    }


    @Test
    void testGetTranslationsWithVocabularyRange() {
        // Given
        List<Translation> expectedTranslations = new ArrayList<>();
        expectedTranslations.add(new Translation(1, new PartOfSpeech(), new Vocabulary(1, "apple", "apple.jpg"), new TranslationVariant()));
        expectedTranslations.add(new Translation(2, new PartOfSpeech(), new Vocabulary(2, "banana", "banana.jpg"), new TranslationVariant()));

        when(translationRepository.findAll()).thenReturn(expectedTranslations);
        VocabularyRange vocabularyRange = new VocabularyRange(1, 1, new Vocabulary(1, "apple", "apple.jpg"));

        when(vocabularyRangeRepository.findVocabularyRangeByVocabularyId(1)).thenReturn(Optional.of(vocabularyRange));
        when(vocabularyRangeRepository.findVocabularyRangeByVocabularyId(2)).thenReturn(Optional.empty());

        // When
        List<TranslationWithVocabularyRange> actualTranslations = translationService.getTranslationsWithVocabularyRange();

        // Then
        assertEquals(2, actualTranslations.size());
        assertEquals("apple", actualTranslations.get(0).getTranslation().getVocabulary().getEnglishWord());
        assertEquals(1, actualTranslations.get(0).getVocabularyRange().getVocabulary_range());
        assertEquals("banana", actualTranslations.get(1).getTranslation().getVocabulary().getEnglishWord());
        assertNull(actualTranslations.get(1).getVocabularyRange());
    }

    @Test
    void testInsertTranslation() {
        // Given
        Translation translation = new Translation();
        translation.setPartOfSpeech(new PartOfSpeech(1, "Noun"));
        when(partOfSpeechService.findById(1)).thenReturn(Optional.of(translation.getPartOfSpeech()));
        when(translationRepository.save(translation)).thenReturn(translation);

        // When
        Translation savedTranslation = translationService.insertTranslation(translation);

        // Then
        assertEquals(translation, savedTranslation);
    }

    @Test
    void testInsertTranslation_NotFound() {
        // Given
        Translation invalidTranslation = new Translation();
        invalidTranslation.setPartOfSpeech(new PartOfSpeech(99, "Invalid"));

        when(partOfSpeechService.findById(99)).thenReturn(Optional.empty());

        // When and Then
        assertThrows(NotFoundException.class,
                () -> translationService.insertTranslation(invalidTranslation),
                "PartOfSpeech not found");
    }

    @Test
    void testUpdateTranslation() {
        // Given
        Translation translation = new Translation();
        translation.setId(1);
        PartOfSpeech partOfSpeech = new PartOfSpeech();
        partOfSpeech.setId(1);
        translation.setPartOfSpeech(partOfSpeech);
        Integer id = 1;
        PartOfSpeechRepository partOfSpeechRepository = mock(PartOfSpeechRepository.class);

        when(partOfSpeechRepository.getReferenceById(any())).thenReturn(partOfSpeech);
        TranslationServiceImpl translationService = new TranslationServiceImpl(
                translationRepository, vocabularyRepository, vocabularyRangeRepository,
                translationVariantRepository, partOfSpeechRepository, partOfSpeechService
        );

        when(translationRepository.existsById(id)).thenReturn(true);
        when(translationRepository.save(translation)).thenReturn(translation);

        // When
        Translation result = translationService.updateTranslation(translation, id);

        // Then
        assertNotNull(result);
        assertEquals(translation, result);
    }

    @Test
    void testUpdateTranslation_ConflictException() {
        // Given
        Integer translationId = 2;
        Translation invalidTranslation = new Translation();
        invalidTranslation.setId(2);

        when(translationRepository.existsById(translationId)).thenReturn(false);

        // When and Then
        assertThrows(ConflictException.class,
                () -> translationService.updateTranslation(invalidTranslation, translationId),
                "Conflict. Translation can't be updated.");
    }

    @Test
    void testRemoveTranslation() {
        // Given
        Integer translationId = 1;
        Translation translation = new Translation();
        when(translationRepository.findById(translationId)).thenReturn(Optional.of(translation));

        // When and Then
        assertDoesNotThrow(() -> translationService.removeTranslation(translationId));
        verify(translationRepository, times(1)).deleteById(translation.getId());
    }

    @Test
    void testRemoveTranslation_NotFoundException() {
        // Given
        Integer translationId = 1;
        when(translationRepository.findById(translationId)).thenReturn(Optional.empty());

        // When and Then
        assertThrows(NotFoundException.class,
                () -> translationService.removeTranslation(translationId),
                "English word was not found");
    }

    @Test
    public void testInsertTranslationWithVocabularyRange_WithVocabularyRange() {
        // Given
        Vocabulary vocabulary = new Vocabulary(1, "apple", "apple.jpg");
        PartOfSpeech partOfSpeech = new PartOfSpeech(1, "idiom");
        Translation translation = new Translation(1, partOfSpeech, vocabulary,
                new TranslationVariant(1, "jabłko"));
        translation.setId(1);
        VocabularyRange vocabularyRange = new VocabularyRange(1, 1, vocabulary);
        vocabularyRange.setVocabulary_range(1);
        TranslationWithVocabularyRange translationWithVocabularyRange = new TranslationWithVocabularyRange(translation, vocabularyRange);

        when(partOfSpeechService.findById(1)).thenReturn(Optional.of(partOfSpeech));
        when(vocabularyRangeRepository.save(translationWithVocabularyRange.getVocabularyRange())).thenReturn(vocabularyRange);
        when(translationRepository.save(translationWithVocabularyRange.getTranslation())).thenReturn(translation);

        // When
        TranslationWithVocabularyRange result = translationService.insertTranslationWithVocabularyRange(translationWithVocabularyRange);

        // Then
        assertNotNull(result);
        assertEquals(translation, result.getTranslation());
        assertEquals(vocabularyRange, result.getVocabularyRange());
    }

    @Test
    public void testInsertTranslationWithVocabularyRange_VocabularyRangeNull() {
        // Given
        Vocabulary vocabulary = new Vocabulary(1, "apple", "apple.jpg");
        PartOfSpeech partOfSpeech = new PartOfSpeech(1, "idiom");
        Translation translation = new Translation(null, partOfSpeech, vocabulary,
                new TranslationVariant(1, "jabłko"));
        Translation translationExpected = new Translation(1, partOfSpeech, vocabulary,
                new TranslationVariant(1, "jabłko"));
        TranslationWithVocabularyRange translationWithVocabularyRange = new TranslationWithVocabularyRange(translation, null);

        when(partOfSpeechService.findById(1)).thenReturn(Optional.of(partOfSpeech));
        when(translationService.insertTranslation(translation)).thenReturn(translationExpected);

        // When
        TranslationWithVocabularyRange result = translationService.insertTranslationWithVocabularyRange(translationWithVocabularyRange);

        // Then
        assertNotNull(result);
        assertEquals(translationExpected.getId(), result.getTranslation().getId());
        assertEquals(translation.getPartOfSpeech(), result.getTranslation().getPartOfSpeech());
        assertEquals(translation.getVocabulary(), result.getTranslation().getVocabulary());
        assertEquals(translation.getTranslationVariant(), result.getTranslation().getTranslationVariant());
    }


    @Test
    void testRemoveTranslationAndVocabulary_WhenTranslationExists() {
        // given
        int translationId = 1;
        Translation translation = new Translation();
        translation.setId(translationId);

        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setId(42);
        translation.setVocabulary(vocabulary);

        when(translationRepository.findById(translationId)).thenReturn(Optional.of(translation));
        when(vocabularyRepository.findVocabularyByTranslationId(translationId)).thenReturn(Optional.of(vocabulary));

        // when
        translationService.removeTranslationAndVocabulary(translationId);

        // then
        verify(vocabularyRepository, times(1)).deleteById(vocabulary.getId());
        verify(translationRepository, times(1)).deleteById(translationId);
    }

    @Test
    void testRemoveTranslationAndVocabulary_WhenTranslationDoesNotExist() {
        // given
        int nonExistentTranslationId = 99;
        when(translationRepository.findById(nonExistentTranslationId)).thenReturn(Optional.empty());
        // when & then
        assertThrows(NotFoundException.class, () -> translationService.removeTranslationAndVocabulary(nonExistentTranslationId));
    }

    @Test
    void testRemoveTranslationsByPartOfSpeechId() {
        // Given
        int partOfSpeechId = 1;

        // When
        assertDoesNotThrow(() -> translationService.removeTranslationsByPartOfSpeechId(partOfSpeechId));

        // Then
        verify(translationRepository, times(1)).deleteByPartOfSpeech_Id(partOfSpeechId);
    }

    @Test
    void testRemoveTranslationWithVocabularyAndTranslationVariantAndVocabularyRange_WhenTranslationExists() {
        // given
        int translationId = 1;
        Translation translation = new Translation();
        translation.setId(translationId);

        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setId(42);
        translation.setVocabulary(vocabulary);

        TranslationVariant translationVariant = new TranslationVariant();
        translationVariant.setId(99);
        translation.setTranslationVariant(translationVariant);

        VocabularyRange vocabularyRange = new VocabularyRange();
        vocabularyRange.setId(123);
        vocabularyRange.setVocabulary(vocabulary);

        when(translationRepository.findById(translationId)).thenReturn(Optional.of(translation));
        when(vocabularyRepository.findVocabularyByTranslationId(translationId)).thenReturn(Optional.of(vocabulary));
        when(translationVariantRepository.findTranslationVariantByTranslationId(translationId)).thenReturn(Optional.of(translationVariant));
        when(vocabularyRangeRepository.findVocabularyRangeByVocabularyId(vocabulary.getId())).thenReturn(Optional.of(vocabularyRange));

        // when
        translationService.removeTranslationWithVocabularyAndTranslationVariantAndVocabularyRange(translationId);

        // then
        verify(vocabularyRangeRepository, times(1)).delete(vocabularyRange);
        verify(translationRepository, times(1)).delete(translation);
        verify(vocabularyRepository, times(1)).delete(vocabulary);
        verify(translationVariantRepository, times(1)).delete(translationVariant);
    }

    @Test
    void testRemoveTranslationWithVocabularyAndTranslationVariantAndVocabularyRange_WhenTranslationDoesNotExist_ShouldThrowNotFoundException() {
        // given
        int nonExistentTranslationId = 99;
        when(translationRepository.findById(nonExistentTranslationId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class, () -> translationService.removeTranslationWithVocabularyAndTranslationVariantAndVocabularyRange(nonExistentTranslationId));
    }

}