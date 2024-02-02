package com.example.englishapp.controllers;


import com.example.englishapp.models.PartOfSpeech;
import com.example.englishapp.models.Translation;
import com.example.englishapp.services.PartOfSpeechService;
import com.example.englishapp.services.TranslationService;
import com.example.englishapp.services.VocabularyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/part-of-speech")
public class PartOfSpeechController {
    final PartOfSpeechService partOfSpeechService;


    @GetMapping()
    public ResponseEntity<List<PartOfSpeech>> getPartOfSpeech() {
        return ResponseEntity.status(HttpStatus.OK).body(partOfSpeechService.getPartOfSpeech());
    }
}
