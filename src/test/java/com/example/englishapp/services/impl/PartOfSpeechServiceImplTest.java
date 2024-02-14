package com.example.englishapp.services.impl;

import com.example.englishapp.exeptions.NotFoundException;
import com.example.englishapp.models.PartOfSpeech;
import com.example.englishapp.repositories.PartOfSpeechRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartOfSpeechServiceImplTest {
    @Mock
    private PartOfSpeechRepository partOfSpeechRepository;

    @InjectMocks
    private PartOfSpeechServiceImpl partOfSpeechService;
    @Test
    void testGetPartOfSpeech() {
        // given
        List<PartOfSpeech> expectedPartOfSpeechList = new ArrayList<>();
        expectedPartOfSpeechList.add(new PartOfSpeech(1, "Noun"));
        expectedPartOfSpeechList.add(new PartOfSpeech(2, "Verb"));
        when(partOfSpeechRepository.findAll()).thenReturn(expectedPartOfSpeechList);

        // when
        List<PartOfSpeech> result = partOfSpeechService.getPartOfSpeech();

        // then
        assertEquals(expectedPartOfSpeechList, result);
        verify(partOfSpeechRepository).findAll();
    }

    @Test
    void testFindById_ExistingId() {
        // given
        int id = 1;
        PartOfSpeech expectedPartOfSpeech = new PartOfSpeech(id, "Noun");
        when(partOfSpeechRepository.findById(id)).thenReturn(Optional.of(expectedPartOfSpeech));

        // when
        Optional<PartOfSpeech> result = partOfSpeechService.findById(id);

        // then
        assertTrue(result.isPresent());
        assertEquals(expectedPartOfSpeech, result.get());
        verify(partOfSpeechRepository).findById(id);
    }

    @Test
    void testFindById_NonExistingId() {
        // given
        int id = 1;
        when(partOfSpeechRepository.findById(id)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class, () -> partOfSpeechService.findById(id));
        verify(partOfSpeechRepository).findById(id);
    }
}