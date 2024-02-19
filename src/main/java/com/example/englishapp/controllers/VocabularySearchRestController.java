package com.example.englishapp.controllers;

import com.example.englishapp.models.dto.TranslationDto;
import com.example.englishapp.models.dto.TranslationWithVocabularyRangeDto;
import com.example.englishapp.models.dto.VocabularyDto;
import com.example.englishapp.services.VocabularySearchService;
import com.example.englishapp.services.VocabularyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vocabulary/search")
public class VocabularySearchRestController {

    private final VocabularyService vocabularyService;
    private final VocabularySearchService vocabularySearchService;
    private final ModelMapper modelMapper;

    @GetMapping("/startingWith")
    public ResponseEntity<List<VocabularyDto>> searchVocabularyStartingWith(@RequestParam(name = "query") String query) {
        var vocabularies = vocabularyService.searchVocabulariesStartingWith(query).stream().map(
                vocabulary -> modelMapper.map(vocabulary, VocabularyDto.class)).toList();
        return ResponseEntity.status(HttpStatus.OK).body(vocabularies);
    }

    @GetMapping()
    public ResponseEntity<List<VocabularyDto>> searchVocabulary(@RequestParam(name = "query") String query) {
        return ResponseEntity.status(HttpStatus.OK).body(
                vocabularyService.searchVocabularies(query).stream().map(
                        vocabulary -> modelMapper.map(vocabulary, VocabularyDto.class)).toList()
        );
    }

    @GetMapping("/translation")
    public ResponseEntity<List<TranslationDto>> searchTranslationByVocabularyOrderByPartOfSpeech(@RequestParam(name = "query") String query) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(vocabularySearchService.searchTranslationsByVocabularyOrderByPartOfSpeech(query).stream()
                        .map(translation -> modelMapper.map(translation, TranslationDto.class)).toList());
    }

    @GetMapping("/findByRangeWithVocabularyRange")
    public ResponseEntity<List<TranslationWithVocabularyRangeDto>> searchVocabulariesByRangeWithVocabularyRange(@RequestParam(name = "query") String query) {
        var translationWithVocabularyRangeDtoList = vocabularySearchService.searchTranslationsWithVocabularyRangeByEnglishWord(query).stream()
                .map(translationWithVocabularyRange -> modelMapper.map(translationWithVocabularyRange, TranslationWithVocabularyRangeDto.class)).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(translationWithVocabularyRangeDtoList);
    }

}



