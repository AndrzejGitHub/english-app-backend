package com.example.englishapp.services.impl;

import com.example.englishapp.exeptions.NotFoundException;
import com.example.englishapp.models.PartOfSpeech;
import com.example.englishapp.repositories.PartOfSpeechRepository;
import com.example.englishapp.services.PartOfSpeechService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class PartOfSpeechServiceImpl implements PartOfSpeechService {

    private final PartOfSpeechRepository partOfSpeechRepository;
    @Override
    public List<PartOfSpeech> getPartOfSpeech() {
        return partOfSpeechRepository.findAll();
    }

    @Override
    public Optional<PartOfSpeech> findById(Integer id) {
        return Optional.ofNullable(partOfSpeechRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User was not found")));
    }
}
