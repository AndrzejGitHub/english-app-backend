package com.example.englishapp.services;

import com.example.englishapp.models.Vocabulary;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface VocabularyService {
    List<Vocabulary> getVocabularies();

    Vocabulary getVocabulary(Integer id);

    List<Vocabulary> getVocabulariesStartingWith(String term);

    List<Vocabulary> getVocabulariesIgnoreCase(String term);

    Vocabulary insertVocabulary(Vocabulary vocabulary);

    void updateVocabulary(Vocabulary vocabulary, Integer id);

    void removeVocabulary(@PathVariable Integer id);

    List<Vocabulary> findVocabulariesByVocabularyRange(Integer rangeId);
}
