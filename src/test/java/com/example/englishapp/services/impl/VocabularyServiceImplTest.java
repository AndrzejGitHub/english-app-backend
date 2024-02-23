package com.example.englishapp.services.impl;

import com.example.englishapp.exeptions.ConflictException;
import com.example.englishapp.exeptions.NotFoundException;
import com.example.englishapp.models.Vocabulary;
import com.example.englishapp.repositories.VocabularyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VocabularyServiceImplTest {

    private static final List<Vocabulary> expectedVocabularies;

    static {
        expectedVocabularies = Arrays.asList(
                Vocabulary.builder().id(1).englishWord("apple").build(),
                Vocabulary.builder().id(2).englishWord("approach").build()
        );

    }

    @InjectMocks
    VocabularyServiceImpl vocabularyService;
    @Mock
    VocabularyRepository vocabularyRepository;

    @Test
    void getVocabularies() {
        // given
        // when
        vocabularyService.getVocabularies();
        // then
        verify(vocabularyRepository).findAll();
    }

    @Test
    void getVocabulary_WordExists() {
        //given
        Integer id = 1;
        Vocabulary vocabulary = Vocabulary.builder().build();
        Optional<Vocabulary> optionalVocabulary = Optional.of(vocabulary);
        //when
        when(vocabularyRepository.findVocabularyById(id)).thenReturn(optionalVocabulary);
        Vocabulary response = vocabularyService.getVocabulary(id);
        //then
        assertEquals(vocabulary, response);
        verify(vocabularyRepository).findVocabularyById(id);
    }

    @Test
    void getVocabulary_NotFound() {
        //given
        Integer id = 1;
        //when
        when(vocabularyRepository.findVocabularyById(anyInt())).thenReturn(Optional.empty());
        //then
        assertThrows((NotFoundException.class), () -> vocabularyService.getVocabulary(id));
        verify(vocabularyRepository).findVocabularyById(id);
    }

    @Test
    void getVocabulariesStartingWith_TermIsNull() {
        // given
        String term = null;
        // then
        assertThrows(NotFoundException.class, () -> vocabularyService.getVocabulariesStartingWith(term));
    }

    @Test
    void getVocabulariesStartingWith_TermIsTooShort() {
        // given
        String term = "a";
        // then
        assertThrows(NotFoundException.class, () -> vocabularyService.getVocabulariesStartingWith(term));
    }

    @Test
    void getVocabulariesStartingWith_ValidTerm() {
        // given
        String term = "ap";
        when(vocabularyRepository.findByEnglishWordIgnoreCaseStartingWith(term)).thenReturn(expectedVocabularies);
        // when
        List<Vocabulary> response = vocabularyService.getVocabulariesStartingWith(term);
        // then
        assertEquals(expectedVocabularies, response);
        verify(vocabularyRepository).findByEnglishWordIgnoreCaseStartingWith(term);
    }

    @Test
    void getVocabulariesIgnoreCase() {
        // given
        String term = "aPple";
        when(vocabularyRepository.findByEnglishWordIgnoreCase(term)).thenReturn(expectedVocabularies);
        // when
        List<Vocabulary> response = vocabularyService.getVocabulariesIgnoreCase(term);
        // then
        assertEquals(expectedVocabularies, response);
        verify(vocabularyRepository).findByEnglishWordIgnoreCase(term);
    }

    @Test
    void insertVocabulary() {
        // given
        Vocabulary vocabulary = Vocabulary.builder().id(null).englishWord("apple").build();
        Vocabulary savedVocabulary = Vocabulary.builder().id(1).englishWord("apple").build();
        when(vocabularyRepository.save(vocabulary)).thenReturn(savedVocabulary);
        // when
        Vocabulary response = vocabularyService.insertVocabulary(vocabulary);
        // then
        assertEquals(savedVocabulary, response);
        verify(vocabularyRepository).save(vocabulary);
    }

    @Test
    void insertVocabulary_Conflict() {
        // given
        Vocabulary vocabulary = Vocabulary.builder().id(1).englishWord("apple").build();
        // then
        assertThrows(ConflictException.class, () -> vocabularyService.insertVocabulary(vocabulary));
    }

    @Test
    void updateVocabulary_SuccessfulUpdate() {
        // given
        Integer id = 1;
        Vocabulary vocabulary = Vocabulary.builder()
                .id(id)
                .englishWord("apple")
                .build();
        when(vocabularyRepository.existsById(id)).thenReturn(true);
        // when
        vocabularyService.updateVocabulary(vocabulary, id);
        // then
       verify(vocabularyRepository).save(vocabulary);
    }

    @Test
    void updateVocabulary_Conflict() {
        // given
        Integer id = 1;
        Vocabulary vocabulary = Vocabulary.builder().id(id).englishWord("banana").build();
        when(vocabularyRepository.existsById(id)).thenReturn(false);
        // then
        assertThrows(ConflictException.class, () -> vocabularyService.updateVocabulary(vocabulary, id));
    }

    @Test
    void removeVocabulary_WordExists() {
        // given
        Integer id = 1;
        Vocabulary vocabulary = Vocabulary.builder().id(id).englishWord("apple").build();
        when(vocabularyRepository.findVocabularyById(id)).thenReturn(Optional.of(vocabulary));
        // when
        vocabularyService.removeVocabulary(id);
        // then
        verify(vocabularyRepository).deleteById(id);
    }

    @Test
    void removeVocabulary_WordNotFound() {
        // given
        Integer id = 1;
        when(vocabularyRepository.findVocabularyById(id)).thenReturn(Optional.empty());
        // when & then
        assertThrows(NotFoundException.class, () -> vocabularyService.removeVocabulary(id));
    }

    @Test
    void findVocabulariesByVocabularyRange() {
        // given
        Integer rangeId = 1;
        when(vocabularyRepository.findVocabulariesByVocabularyRange(rangeId))
                .thenReturn(expectedVocabularies);
        // when
        List<Vocabulary> actualVocabularies = vocabularyService.findVocabulariesByVocabularyRange(rangeId);
        // then
        assertEquals(expectedVocabularies, actualVocabularies);
        verify(vocabularyRepository).findVocabulariesByVocabularyRange(rangeId);
    }

}