package com.example.englishapp.controllers;

import com.example.englishapp.exeptions.NotFoundException;
import com.example.englishapp.models.Translation;
import com.example.englishapp.models.TranslationWithVocabularyRange;
import com.example.englishapp.models.dto.PartOfSpeechDto;
import com.example.englishapp.models.dto.TranslationDto;
import com.example.englishapp.models.dto.TranslationWithVocabularyRangeDto;
import com.example.englishapp.services.PartOfSpeechService;
import com.example.englishapp.services.TranslationService;
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
@RequestMapping("/api/translation")
public class TranslationRestController {

    final TranslationService translationService;
    final VocabularyService vocabularyService;
    final PartOfSpeechService partOfSpeechService;
    final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<List<TranslationDto>> getTranslations() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(translationService.getTranslations().stream()
                        .map((translation) -> modelMapper.map(translation, TranslationDto.class)).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TranslationDto> getTranslationById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(modelMapper.map(translationService.getTranslation(id), TranslationDto.class));
    }

    @GetMapping("/vocabulary/{id}")
    public ResponseEntity<List<TranslationDto>> getTranslationsByVocabularyId(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(translationService.getTranslationsByVocabularyId(id).stream()
                        .map((translation) -> modelMapper.map(translation, TranslationDto.class)).toList());
    }

    @GetMapping("/range")
    public ResponseEntity<List<TranslationWithVocabularyRangeDto>> getTranslationsWithVocabularyRange() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(translationService.getTranslationsWithVocabularyRange().stream()
                        .map((translationWithVocabularyRange) -> modelMapper.map(translationWithVocabularyRange, TranslationWithVocabularyRangeDto.class)).toList());
    }

    @GetMapping("/range/{id}")
    public ResponseEntity<List<TranslationWithVocabularyRangeDto>> getTranslationsByVocabularyRangeIdWithVocabularyRange(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(translationService.getTranslationsByVocabularyRangeIdWithVocabularyRange(id).stream()
                        .map((translationWithVocabularyRange) -> modelMapper.map(translationWithVocabularyRange, TranslationWithVocabularyRangeDto.class)).toList());
    }

    @PostMapping()
    public ResponseEntity<TranslationDto> addTranslation(@Valid @RequestBody TranslationDto translationDto) {
        return Optional.ofNullable(translationService.insertTranslation(
                        modelMapper.map(translationDto, Translation.class)))
                .map(translationSaved ->
                        ResponseEntity.status(HttpStatus.OK)
                                .body(modelMapper.map(translationSaved, TranslationDto.class)))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Integer> editTranslation(@RequestBody TranslationDto translationDto, @PathVariable Integer id) {
        translationService.updateTranslation(modelMapper.map(translationDto, Translation.class), id);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

    @PostMapping("/range")
    public ResponseEntity<TranslationWithVocabularyRangeDto> addTranslationWithVocabularyRange(@Valid @RequestBody TranslationWithVocabularyRangeDto translationWithVocabularyRangeDto) {
        return Optional.ofNullable(translationService.insertTranslationWithVocabularyRange(
                        modelMapper.map(translationWithVocabularyRangeDto, TranslationWithVocabularyRange.class)))
                .map(translationSaved -> ResponseEntity.status(HttpStatus.OK)
                        .body(modelMapper.map(translationSaved, TranslationWithVocabularyRangeDto.class)))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PutMapping("range/{id}")
    public ResponseEntity<Integer> editTranslationWithVocabularyRange(@Valid @RequestBody TranslationWithVocabularyRangeDto translationWithVocabularyRangeDto,
                                                                      @PathVariable Integer id) {
        return Optional.ofNullable(translationService.updateTranslationWithVocabularyRange(
                        modelMapper.map(translationWithVocabularyRangeDto, TranslationWithVocabularyRange.class), id))
                .map(translationUpdated -> ResponseEntity.status(HttpStatus.OK).body(id))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Integer> deleteTranslationWithVocabularyRange(@PathVariable Integer id) {
        translationService.removeTranslationWithVocabularyAndTranslationVariantAndVocabularyRange(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(id);
    }
}
