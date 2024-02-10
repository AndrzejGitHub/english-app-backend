package com.example.englishapp.services;

import com.example.englishapp.models.PartOfSpeech;

import java.util.List;
import java.util.Optional;

public interface PartOfSpeechService {
    List<PartOfSpeech> getPartOfSpeech();

    Optional<PartOfSpeech> findById(Integer id);
}
