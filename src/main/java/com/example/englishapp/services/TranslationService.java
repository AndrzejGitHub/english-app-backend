package com.example.englishapp.services;

import com.example.englishapp.exeptions.ConflictException;
import com.example.englishapp.exeptions.NotFoundException;
import com.example.englishapp.models.Translation;
import com.example.englishapp.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TranslationService {

    final TranslationRepository translationRepository;
    final VocabularyRepository vocabularyRepository;
    final TranslationVariantRepository translationVariantRepository;
    final PartOfSpeechRepository partOfSpeechRepository;

    public List<Translation> getTranslations() {
        return translationRepository.findAll();
    }

    public List<Translation> getTranslation(Integer id) {
        var translation = translationRepository.findTranslationsByVocabularyId(id);
        if (!translation.isEmpty())
            return translation;
        else
            throw new NotFoundException("English word translation was not found");
    }

    //    public List<Vocabulary> searchVocabulariesStartingWith(String term) {
//        if (term != null && term.length() >= 2) {
//            return vocabularyRepository.findByEnglishWordIgnoreCaseStartingWith(term);
//        } else throw new NotFoundException("Please enter at least two letters.");
//    }
//
//    public List<Vocabulary> searchVocabularies(String term) {
//        return vocabularyRepository.findByEnglishWordIgnoreCase(term);
//    }
//
    public Translation insertTranslation(Translation translation) {
        System.out.println("insertTranslation " + translation);
        if (Objects.isNull(translation.getId()))
            return translationRepository.save(translation);
        else throw new ConflictException();
    }

    public void updateTranslation(Translation translation, Integer id) {
        if ((Objects.nonNull(translation.getId()))
                        && (translation.getId().equals(id))
                        && (translationRepository.existsById(id))) {
            translation.setId(id);
            translationRepository.save(translation);
        } else throw new ConflictException();
    }

    @Transactional
    public void removeTranslation(@PathVariable Integer id) {
        var translation = translationRepository.findById(id);
        if (translation.isPresent()) {
            translationRepository.deleteById(id);
        } else
            throw new NotFoundException("English word was not found");
    }


    @Transactional
    public void removeTranslationAndVocabularyAndTranslationVariant(@PathVariable Integer id) {
        var translation = translationRepository.findById(id);
        if (translation.isPresent()) {
            var vocabulary = vocabularyRepository.findVocabularyByTranslationId(id);
            var translationVariant = translationVariantRepository.findTranslationVariantByTranslationId(id);
            System.out.println(vocabulary);
            System.out.println(translationVariant);
            vocabulary.ifPresent(value -> vocabularyRepository.deleteById(value.getId()));
            translationVariant.ifPresent(value -> translationVariantRepository.deleteById(value.getId()));
            translationRepository.deleteById(id);
        } else
            throw new NotFoundException("English word was not found");
    }
    @Transactional
    public void removeTranslationAndVocabulary(@PathVariable Integer id) {
        var translation = translationRepository.findById(id);
        if (translation.isPresent()) {
            var vocabulary = vocabularyRepository.findVocabularyByTranslationId(id);
            vocabulary.ifPresent(value -> vocabularyRepository.deleteById(value.getId()));
            translationRepository.deleteById(id);
        } else
            throw new NotFoundException("English word was not found");
    }

    public void removeTranslationsByPartOfSpeechId(Integer partOfSpeechId) {
        translationRepository.deleteByPartOfSpeech_Id(partOfSpeechId);
    }

}
