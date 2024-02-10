package com.example.englishapp.controllers;

import com.example.englishapp.models.TranslationWithVocabularyRange;
import com.example.englishapp.models.Vocabulary;
import com.example.englishapp.models.dto.TranslationWithVocabularyRangeDto;
import com.example.englishapp.models.dto.VocabularyDto;
import com.example.englishapp.services.VocabularyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vocabulary")
public class VocabularyRestController {

    final VocabularyService vocabularyService;
    final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<List<VocabularyDto>> getVocabularies() {
        return ResponseEntity.status(HttpStatus.OK).body(vocabularyService.getVocabularies()
                .stream().map(vocabulary -> modelMapper.map(vocabulary, VocabularyDto.class)).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VocabularyDto> getVocabulary(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                modelMapper.map(vocabularyService.getVocabulary(id), VocabularyDto.class));
    }

    @GetMapping("/range/{rangeId}")
    public ResponseEntity<List<VocabularyDto>> getVocabulariesByVocabularyRange(@PathVariable Integer rangeId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                vocabularyService.findVocabulariesByVocabularyRange(rangeId)
                        .stream().map(vocabulary -> modelMapper.map(vocabulary, VocabularyDto.class)).toList()
        );
    }

    @PostMapping()
    public ResponseEntity<VocabularyDto> addVocabulary(@Valid @RequestBody VocabularyDto vocabularyDto) {
        return Optional.ofNullable(vocabularyService.insertVocabulary(modelMapper.map(vocabularyDto, Vocabulary.class)))
                .map(vocabulary -> ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(vocabulary, VocabularyDto.class)))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Integer> editVocabulary(@RequestBody VocabularyDto vocabularyDto, @PathVariable Integer id) {
        vocabularyService.updateVocabulary(modelMapper.map(vocabularyDto, Vocabulary.class), id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Integer> deleteVocabulary(@PathVariable Integer id) {
        vocabularyService.removeVocabulary(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(id);
    }

}
