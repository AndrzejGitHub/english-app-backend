package com.example.englishapp.services.impl;

import com.example.englishapp.exeptions.BadRequestException;
import com.example.englishapp.exeptions.ConflictException;
import com.example.englishapp.exeptions.NotFoundException;
import com.example.englishapp.models.PartOfSpeech;
import com.example.englishapp.models.Translation;
import com.example.englishapp.models.TranslationWithVocabularyRange;
import com.example.englishapp.models.VocabularyRange;
import com.example.englishapp.repositories.*;
import com.example.englishapp.services.PartOfSpeechService;
import com.example.englishapp.services.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
class TranslationServiceImpl implements TranslationService {

    final TranslationRepository translationRepository;
    final VocabularyRepository vocabularyRepository;
    final VocabularyRangeRepository vocabularyRangeRepository;
    final TranslationVariantRepository translationVariantRepository;
    final PartOfSpeechRepository partOfSpeechRepository;
    final PartOfSpeechService partOfSpeechService;

    @Override
    public List<Translation> getTranslations() {
        return translationRepository.findAll();
    }

    @Override
    public Translation getTranslation(Integer id) {
        var translation = translationRepository.findById(id);
        if (translation.isPresent())
            return translation.get();
        else
            throw new NotFoundException("Translation was not found");
    }

    @Override
    public List<Translation> getTranslationByVocabularyId(Integer id) {
        var translation = translationRepository.findTranslationsByVocabularyId(id);
        if (!translation.isEmpty())
            return translation;
        else
            throw new NotFoundException("English word translation was not found");
    }

    @Override
    public List<TranslationWithVocabularyRange> getTranslationsByVocabularyRangeIdWithVocabularyRange(Integer id) {
        var findVocabulariesByVocabularyRange = vocabularyRepository.findVocabulariesByVocabularyRange(id);
        if (findVocabulariesByVocabularyRange.isEmpty())
            throw new NotFoundException("Vocabulary Range was not found");
        return findVocabulariesByVocabularyRange.stream()
                .map(vocabulary -> {
                    var vocabularyById = vocabularyRepository.findVocabularyById(vocabulary.getId());
                    if (vocabularyById.isPresent()) {
                        var translations = translationRepository.findTranslationByVocabulary(vocabularyById.orElseThrow());
                        if (!translations.isEmpty()) {
                            var vocabularyRange = vocabularyRangeRepository.findVocabularyRangeByVocabularyId(vocabulary.getId())
                                    .orElseThrow(() -> new NotFoundException("VocabularyRange not found"));
                            return new TranslationWithVocabularyRange(translations.getFirst(), vocabularyRange);
                        }
                    }
                    throw new NotFoundException("Vocabulary was not found");
                })
                .toList();
    }

    @Override
    public List<TranslationWithVocabularyRange> getTranslationsWithVocabularyRange() {
        return translationRepository.findAll().stream().map(
                translation -> {
                    var findVocabularyRangeByVocabularyId = vocabularyRangeRepository.findVocabularyRangeByVocabularyId(translation.getVocabulary().getId());
                    return findVocabularyRangeByVocabularyId.map(
                                    vocabularyRange -> new TranslationWithVocabularyRange(translation, vocabularyRange))
                            .orElseGet(() -> new TranslationWithVocabularyRange(translation, null));
                }
        ).toList();
    }

    @Override
    public Translation insertTranslation(Translation translation) {
        if (Objects.isNull(translation.getId()))
            return translationRepository.save(translation);
        else throw new ConflictException("Conflict. Translation exist.");
    }

    @Override
    @Transactional
    public TranslationWithVocabularyRange insertTranslationWithVocabularyRange(TranslationWithVocabularyRange translationWithVocabularyRange) {
        Objects.requireNonNull(translationWithVocabularyRange, "TranslationWithVocabularyRangeDto not found");
        Integer partOfSpeechId = Optional.ofNullable(translationWithVocabularyRange.getTranslation())
                .map(Translation::getPartOfSpeech)
                .map(PartOfSpeech::getId)
                .orElseThrow(() -> new BadRequestException("Part of Speech is required"));
        PartOfSpeech partOfSpeech = partOfSpeechService.findById(partOfSpeechId)
                .orElseThrow(() -> new NotFoundException("PartOfSpeech not found"));
        translationWithVocabularyRange.getTranslation().setPartOfSpeech(partOfSpeech);
        if (translationWithVocabularyRange.getTranslation().getPartOfSpeech() == null) {
            throw new BadRequestException("Part of speech is required");
        }
        if (Objects.isNull(translationWithVocabularyRange.getTranslation().getId())
                && Objects.nonNull(translationWithVocabularyRange.getVocabularyRange())) {
            Translation savedTranslation = translationRepository.save(translationWithVocabularyRange.getTranslation());
            translationWithVocabularyRange.vocabularyRange.setVocabulary(savedTranslation.getVocabulary());
            VocabularyRange savedVocabularyRange = vocabularyRangeRepository.save(translationWithVocabularyRange.getVocabularyRange());
            return new TranslationWithVocabularyRange(savedTranslation, savedVocabularyRange);
        } else {
            throw new ConflictException("Conflict. TranslationWithVocabularyRange exist.");
        }
    }

    @Override
    @Transactional
    public void updateTranslation(Translation translation, Integer id) {
        if ((Objects.nonNull(translation.getId()))
                && (translation.getId().equals(id))
                && (translationRepository.existsById(id))) {
            translation.setId(id);
            translation.setPartOfSpeech(partOfSpeechRepository.getReferenceById(translation.getPartOfSpeech().getId()));
            translationRepository.save(translation);
        } else throw new ConflictException("Conflict. Translation can't be updated.");
    }

    @Override
    @Transactional
    public void removeTranslation(@PathVariable Integer id) {
        var translation = translationRepository.findById(id);
        if (translation.isPresent()) {
            translationRepository.deleteById(id);
        } else
            throw new NotFoundException("English word was not found");
    }

    @Override
    @Transactional
    public void removeTranslationAndVocabularyAndTranslationVariant(@PathVariable Integer id) {
        var translation = translationRepository.findById(id);
        if (translation.isPresent()) {
            var vocabulary = vocabularyRepository.findVocabularyByTranslationId(id);
            var translationVariant = translationVariantRepository.findTranslationVariantByTranslationId(id);
            vocabulary.ifPresent(value -> vocabularyRepository.deleteById(value.getId()));
            translationVariant.ifPresent(value -> translationVariantRepository.deleteById(value.getId()));
            translationRepository.deleteById(id);
        } else
            throw new NotFoundException("English word was not found");
    }

    @Override
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

    @Override
    public void removeTranslationsByPartOfSpeechId(Integer partOfSpeechId) {
        translationRepository.deleteByPartOfSpeech_Id(partOfSpeechId);
    }

    @Override
    public void removeTranslationWithVocabularyAndTranslationVariantAndVocabularyRange(Integer id) {
        if (translationRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Translation was not found");
        }
        translationRepository.findById(id).ifPresent(translation -> {
            vocabularyRepository.findVocabularyByTranslationId(id).ifPresent(vocabulary -> {
                translationVariantRepository.findTranslationVariantByTranslationId(id).ifPresent(translationVariant -> {
                    vocabularyRangeRepository.findVocabularyRangeByVocabularyId(vocabulary.getId()).ifPresent(vocabularyRangeRepository::delete);
                    translationRepository.delete(translation);
                    vocabularyRepository.delete(vocabulary);
                    translationVariantRepository.delete(translationVariant);
                });
            });
        });
    }

}
