package com.example.englishapp.controllers;

import com.example.englishapp.models.Vocabulary;
import com.example.englishapp.models.dto.VocabularyDto;
import com.example.englishapp.services.VocabularyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vocabulary")
public class VocabularyRestController {

    final VocabularyService vocabularyService;
    final ModelMapper modelMapper;
    @GetMapping()
    public ResponseEntity<List<VocabularyDto>> getVocabularies() {
       return ResponseEntity.status(HttpStatus.OK).body(vocabularyService.getVocabularies()
               .stream().map(
                       vocabulary -> modelMapper.map(vocabulary, VocabularyDto.class)).toList());
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<VocabularyDto> getVocabulary(@PathVariable Integer id) {
//        return ResponseEntity.status(HttpStatus.OK).body(
//                modelMapper.map(vocabularyService.getVocabulary(id), VocabularyDto.class));
//    }

    @GetMapping("/{rangeId}")
    public ResponseEntity<VocabularyDto> findVocabulariesByVocabularyRange(@PathVariable Integer rangeId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                modelMapper.map(vocabularyService.findVocabulariesByVocabularyRange(rangeId), VocabularyDto.class));
    }

    @PostMapping()
    public ResponseEntity<VocabularyDto> addVocabulary(@RequestBody VocabularyDto vocabularyDto) {
        var vocabulary = vocabularyService.insertVocabulary(modelMapper.map(vocabularyDto,Vocabulary.class));
        if (vocabulary.getId()==null) //error save
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        else
            return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(vocabulary,VocabularyDto.class));
    }

    @PutMapping("{id}")
    public ResponseEntity<Integer> editVocabulary(@RequestBody VocabularyDto vocabularyDto, @PathVariable Integer id) {
        vocabularyService.updateVocabulary(modelMapper.map(vocabularyDto,Vocabulary.class) ,id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Integer> deleteVocabulary(@PathVariable Integer id) {
        vocabularyService.removeVocabulary(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(id);
    }

}
