package com.example.englishapp.services.impl;

import com.example.englishapp.exeptions.ConflictException;
import com.example.englishapp.exeptions.NotFoundException;
import com.example.englishapp.models.Vocabulary;
import com.example.englishapp.repositories.VocabularyRepository;
import com.example.englishapp.services.VocabularyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class VocabularyServiceImpl implements VocabularyService {

    final VocabularyRepository vocabularyRepository;

    @Override
    public List<Vocabulary> getVocabularies() {
        return vocabularyRepository.findAll();
    }

    @Override
    public Vocabulary getVocabulary(Integer id) {
        return vocabularyRepository.findVocabularyById(id)
                .orElseThrow(() -> new NotFoundException("English word was not found"));
    }

    @Override
    public List<Vocabulary> searchVocabulariesStartingWith(String term) {
        return Optional.ofNullable(term)
                .filter(t -> t.length() >= 2)
                .map(vocabularyRepository::findByEnglishWordIgnoreCaseStartingWith)
                .orElseThrow(() -> new NotFoundException("Please enter at least two letters."));
    }

    @Override
    public List<Vocabulary> searchVocabularies(String term) {
        return vocabularyRepository.findByEnglishWordIgnoreCase(term);
    }

    @Override
    public Vocabulary insertVocabulary(Vocabulary vocabulary) {
        return Optional.ofNullable(vocabulary)
                .filter(v -> Objects.isNull(v.getId()))
                .map(vocabularyRepository::save)
                .orElseThrow(() -> new ConflictException("Conflict. Vocabulary exist."));
    }

    @Override
    public void updateVocabulary(Vocabulary vocabulary, Integer id) {
        Optional.ofNullable(vocabulary)
                .filter(v -> Objects.nonNull(v.getId())
                        && v.getId().equals(id)
                        && vocabularyRepository.existsById(id))
                .ifPresentOrElse(
                        v -> {
                            v.setId(id);
                            vocabularyRepository.save(v);
                        },
                        () -> {
                            throw new ConflictException("Conflict. Vocabulary can't be updated.");
                        }
                );
    }


    @Override
    public void removeVocabulary(@PathVariable Integer id) {
        Optional<Vocabulary> vocabularyOptional = vocabularyRepository.findVocabularyById(id);
        if (vocabularyOptional.isPresent()) {
            vocabularyRepository.deleteById(id);
        } else {
            throw new NotFoundException("English word was not found");
        }
        // TODO
        //  @Test  void removeVocabulary_WordNotFound()
        //  test doesn't work with this functional notation
        //  it works only with above notation
//        Optional.ofNullable(vocabularyRepository.findVocabularyById(id))
//                .ifPresentOrElse(
//                        v -> vocabularyRepository.deleteById(id),
//                        () -> {
//                            throw new NotFoundException("English word was not found");
//                        }
//
//                );
    }

    @Override
    public List<Vocabulary> findVocabulariesByVocabularyRange(Integer rangeId) {
        return vocabularyRepository.findVocabulariesByVocabularyRange(rangeId);
    }
}
