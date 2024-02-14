package com.example.englishapp.controllers;

import com.example.englishapp.models.dto.PartOfSpeechDto;
import com.example.englishapp.services.PartOfSpeechService;
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
    final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<List<PartOfSpeechDto>> getPartOfSpeech() {
        return ResponseEntity.status(HttpStatus.OK).body(
                partOfSpeechService.getPartOfSpeech().stream()
                        .map((partOfSpeech) -> modelMapper.map(partOfSpeech, PartOfSpeechDto.class)).toList());
    }
}
