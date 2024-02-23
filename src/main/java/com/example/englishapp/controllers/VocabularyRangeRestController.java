package com.example.englishapp.controllers;

import com.example.englishapp.models.MergeRequest;
import com.example.englishapp.models.dto.VocabularyRangeDto;
import com.example.englishapp.services.VocabularyRangeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vocabulary-range")
public class VocabularyRangeRestController {

    private final VocabularyRangeService vocabularyRangeService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<VocabularyRangeDto> getVocabularyRangeByTranslationId(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(modelMapper.map(vocabularyRangeService.getVocabularyRangeByTranslationId(id), VocabularyRangeDto.class));
    }

    @GetMapping
    public ResponseEntity<VocabularyRangeDto> getVocabularyRangeByEnglishWord(@RequestParam(name = "query") String query) {
        return vocabularyRangeService.getVocabularyRangeByEnglishWord(query)
                .map(vocabularyRange -> ResponseEntity.status(HttpStatus.OK)
                        .body(modelMapper.map(vocabularyRange, VocabularyRangeDto.class)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/get-max-vocabulary-range")
    public ResponseEntity<Integer> getMaxVocabularyRange() {
        return ResponseEntity.ok().body(vocabularyRangeService.getMaxVocabularyRange());
    }

    @PostMapping
    public ResponseEntity<VocabularyRangeDto> insertVocabularyRangeToExistingWordWithoutVocabularyRange(@RequestBody MergeRequest mergeRequest) {
        return ResponseEntity.ok().body(
                modelMapper.map(vocabularyRangeService.insertVocabularyRangeToExistingWordWithoutVocabularyRange(
                        mergeRequest.getVocabulary(), mergeRequest.getVocabularyRange()), VocabularyRangeDto.class));
    }

}



