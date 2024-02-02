package com.example.englishapp.controllers;

import com.example.englishapp.models.Translation;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vocabulary/search")
public class VocabularySearchRestController {

    final VocabularyService vocabularyService;
    final VocabularySearchService vocabularySearchService;
    final ModelMapper modelMapper;

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
    public ResponseEntity<List<Translation>> searchTranslationByVocabularyOrderByPartOfSpeech(@RequestParam(name = "query") String query) {
        return ResponseEntity.status(HttpStatus.OK).body(
                vocabularySearchService.searchTranslationsByVocabularyOrderByPartOfSpeech(query));
    }

    @GetMapping("/findByRange")
    public ResponseEntity<List<Translation>> searchVocabulariesByRange(@RequestParam(name = "query") String query) {



        var vocabularies = vocabularyService.searchVocabularies(query);
        if (vocabularies.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var firstVocabulary = vocabularies.getFirst();
        List<Translation> combinedResults;

        if (firstVocabulary.getVocabularyRange() == null) {
            combinedResults = vocabularySearchService.searchVocabularyAndGiveResponseTranslations(query);
        } else {
            var translationsByWord = vocabularySearchService.searchTranslationsByEnglishWord(query);
            var translationsByRangeId = vocabularySearchService.searchTranslationsByRangeId(firstVocabulary.getVocabularyRange().getId());
            combinedResults = new ArrayList<>(translationsByWord);
            translationsByRangeId.stream()
                    .filter(translation -> combinedResults.stream().noneMatch(t -> t.getId().equals(translation.getId())))
                    .forEach(combinedResults::add);
        }
        return ResponseEntity.ok().body(combinedResults);
    }

}



