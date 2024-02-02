package com.example.englishapp.services;

import com.example.englishapp.exeptions.ConflictException;
import com.example.englishapp.exeptions.NotFoundException;
import com.example.englishapp.models.Vocabulary;
import com.example.englishapp.repositories.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class VocabularyService {

    final VocabularyRepository vocabularyRepository;

    public List<Vocabulary> getVocabularies() {
        return vocabularyRepository.findAll();
    }

    public Vocabulary getVocabulary(Integer id) {
        var vocabulary = vocabularyRepository.findVocabularyById(id);
        if (vocabulary.isPresent())
            return vocabulary.get();
        else
            throw new NotFoundException("English word was not found");
    }

    public List<Vocabulary> searchVocabulariesStartingWith(String term) {
        if (term != null && term.length() >= 2) {
            return vocabularyRepository.findByEnglishWordIgnoreCaseStartingWith(term);
        } else throw new NotFoundException("Please enter at least two letters.");
    }

    public List<Vocabulary> searchVocabularies(String term) {
        return vocabularyRepository.findByEnglishWordIgnoreCase(term);
    }

    public Vocabulary insertVocabulary(Vocabulary vocabulary) {
        if (Objects.isNull(vocabulary.getId()))
            return vocabularyRepository.save(vocabulary);
        else throw new ConflictException();
    }

    public void updateVocabulary(Vocabulary vocabulary, Integer id) {
        if (
                (Objects.nonNull(vocabulary.getId()))
                        && (vocabulary.getId().equals(id))
                        && (vocabularyRepository.existsById(id))
        ) {
            vocabulary.setId(id);
            vocabularyRepository.save(vocabulary);
        } else throw new ConflictException();
    }


    public void removeVocabulary(@PathVariable Integer id) {
        if (vocabularyRepository.findVocabularyById(id).isPresent()) {
            vocabularyRepository.deleteById(id);
        } else
            throw new NotFoundException("English word was not found");
    }
}
