package com.example.englishapp.services;

import com.example.englishapp.models.PartOfSpeech;
import com.example.englishapp.repositories.PartOfSpeechRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartOfSpeechService {

    final PartOfSpeechRepository partOfSpeechRepository;
    public List<PartOfSpeech> getPartOfSpeech() {
        return partOfSpeechRepository.findAll();
    }

    public Optional<PartOfSpeech> findById(Integer id) {
        return partOfSpeechRepository.findById(id);
    }
}
