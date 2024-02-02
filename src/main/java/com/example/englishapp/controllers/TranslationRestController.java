package com.example.englishapp.controllers;

import com.example.englishapp.models.PartOfSpeech;
import com.example.englishapp.models.Translation;
import com.example.englishapp.models.VocabularyRange;
import com.example.englishapp.repositories.PartOfSpeechRepository;
import com.example.englishapp.services.PartOfSpeechService;
import com.example.englishapp.services.TranslationService;
import com.example.englishapp.services.VocabularyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/translation")
public class TranslationRestController {

    final TranslationService translationService;
    final VocabularyService vocabularyService;
    final ModelMapper modelMapper;
    final PartOfSpeechService partOfSpeechService;

    @GetMapping()
    public ResponseEntity<List<Translation>> getTranslations() {
        return ResponseEntity.status(HttpStatus.OK).body(translationService.getTranslations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Translation>> getTranslation(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(translationService.getTranslation(id));
    }

    @PostMapping()
    public ResponseEntity<Translation> addTranslation(@RequestBody Translation translation) {
        Integer partOfSpeechId = translation.getPartOfSpeech().getId();
        PartOfSpeech managedPartOfSpeech = partOfSpeechService.findById(partOfSpeechId)
                .orElseThrow(() -> new RuntimeException("PartOfSpeech not found"));
        translation.setPartOfSpeech(managedPartOfSpeech);
//        VocabularyRange managedVocabularyRange = vocabularyService.findById(partOfSpeechId)
//                .orElseThrow(() -> new RuntimeException("PartOfSpeech not found"));
        var translationSaved = translationService.insertTranslation(translation);
        if (translationSaved.getId() == null) //error save
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        else
            return ResponseEntity.status(HttpStatus.OK).body(translationSaved);
    }

    @PutMapping("{id}")
    public ResponseEntity<Integer> editTranslation(@RequestBody Translation translation, @PathVariable Integer id) {
        translationService.updateTranslation(translation, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Integer> deleteTranslation(@PathVariable Integer id) {
        translationService.removeTranslationAndVocabularyAndTranslationVariant(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(id);
    }
}
