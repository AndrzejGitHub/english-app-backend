package com.example.englishapp.services.impl;

import com.example.englishapp.exeptions.BadRequestException;
import com.example.englishapp.exeptions.ConflictException;
import com.example.englishapp.exeptions.NotFoundException;
import com.example.englishapp.models.PartOfSpeech;
import com.example.englishapp.models.Translation;
import com.example.englishapp.models.TranslationWithVocabularyRange;
import com.example.englishapp.models.Vocabulary;
import com.example.englishapp.repositories.*;
import com.example.englishapp.services.PartOfSpeechService;
import com.example.englishapp.services.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
class TranslationServiceImpl implements TranslationService {

    private final TranslationRepository translationRepository;
    private final VocabularyRepository vocabularyRepository;
    private final VocabularyRangeRepository vocabularyRangeRepository;
    private final TranslationVariantRepository translationVariantRepository;
    private final PartOfSpeechRepository partOfSpeechRepository;
    private final PartOfSpeechService partOfSpeechService;

    @Override
    public List<Translation> getTranslations() {
        return translationRepository.findAll();
    }

    @Override
    public Translation getTranslation(Integer id) {
        return translationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Translation was not found"));
    }

    @Override
    public List<Translation> getTranslationsByVocabularyId(Integer id) {
        List<Translation> translations = translationRepository.findTranslationsByVocabularyId(id);
        if (translations.isEmpty()) {
            throw new NotFoundException("English word translation was not found");
        }
        return translations;
    }

    @Override
    public List<TranslationWithVocabularyRange> getTranslationsByVocabularyRangeIdWithVocabularyRange(Integer id) {
        List<Vocabulary> findVocabulariesByVocabularyRange = vocabularyRepository.findVocabulariesByVocabularyRange(id);
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
        Integer partOfSpeechId = translation.getPartOfSpeech().getId();
        partOfSpeechService.findById(partOfSpeechId)
                .ifPresentOrElse(translation::setPartOfSpeech,
                        () -> {
                            throw new NotFoundException("PartOfSpeech not found");
                        });
        return (Translation) Optional.ofNullable(translation.getId()).map(
                        (id) -> {
                            throw new ConflictException("Conflict. Translation exist.");
                        })
                .orElseGet(() -> translationRepository.save(translation));
    }

    @Override
    @Transactional
    public Translation updateTranslation(Translation translation, Integer id) {
        if ((Objects.nonNull(translation.getId()))
                && (translation.getId().equals(id))
                && (translationRepository.existsById(id))) {
            translation.setId(id);
            translation.setPartOfSpeech(partOfSpeechRepository.getReferenceById(translation.getPartOfSpeech().getId()));
            return translationRepository.save(translation);
        } else throw new ConflictException("Conflict. Translation can't be updated.");
    }

    @Override
    @Transactional
    public void removeTranslation(@PathVariable Integer id) {
        translationRepository.findById(id)
                .ifPresentOrElse((translation -> translationRepository.deleteById(translation.getId())),
                        () -> {
                            throw new NotFoundException("English word was not found");
                        });
    }

    @Override
    @Transactional
    public TranslationWithVocabularyRange insertTranslationWithVocabularyRange(TranslationWithVocabularyRange translationWithVocabularyRange) {
        validateTranslationWithVocabularyRangeDto(translationWithVocabularyRange);
        return Optional.of(translationWithVocabularyRange)
                .filter(t -> t.getVocabularyRange() == null || t.getVocabularyRange().getVocabulary_range() == null)
                .map(t -> new TranslationWithVocabularyRange(
                        insertTranslation(t.getTranslation()), null))
                .orElseGet(() -> translationWithVocabularyRangeSaved(translationWithVocabularyRange));
    }

    @Override
    @Transactional
    public TranslationWithVocabularyRange updateTranslationWithVocabularyRange(TranslationWithVocabularyRange translationWithVocabularyRange, Integer id) {
        validateTranslationWithVocabularyRangeDto(translationWithVocabularyRange);
        translationWithVocabularyRange.getTranslation().setId(id);
        return Optional.of(translationWithVocabularyRange)
                .filter(t -> t.getVocabularyRange() == null || t.getVocabularyRange().getVocabulary_range() == null)
                .map(t -> new TranslationWithVocabularyRange(
                        updateTranslation(t.getTranslation(), t.getTranslation().getId()), null))
                .orElseGet(() -> translationWithVocabularyRangeSaved(translationWithVocabularyRange));
    }

    public void validateTranslationWithVocabularyRangeDto(TranslationWithVocabularyRange translationWithVocabularyRange) {
        List<String> errorMessages = new ArrayList<>();
        if (translationWithVocabularyRange.getTranslation().getPartOfSpeech() == null
                || translationWithVocabularyRange.getTranslation().getPartOfSpeech().getId() == null) {
            errorMessages.add("Part of speech is required");
        }
        if (translationWithVocabularyRange.getTranslation().getVocabulary().getEnglishWord() == null
                || translationWithVocabularyRange.getTranslation().getVocabulary().getEnglishWord().isEmpty()) {
            errorMessages.add("English word is required");
        }

        if (translationWithVocabularyRange.getTranslation().getTranslationVariant().getPolishMeaning() == null
                || translationWithVocabularyRange.getTranslation().getTranslationVariant().getPolishMeaning().isEmpty()) {
            errorMessages.add("Polish translation is required");
        }
        if (!errorMessages.isEmpty()) {
            String errorMessage = String.join("; ", errorMessages);
            throw new BadRequestException(errorMessage);
        }
    }

    TranslationWithVocabularyRange translationWithVocabularyRangeSaved(TranslationWithVocabularyRange translationWithVocabularyRange) {
        Integer partOfSpeechId = translationWithVocabularyRange.getTranslation().getPartOfSpeech().getId();
        PartOfSpeech partOfSpeech = partOfSpeechService.findById(partOfSpeechId)
                .orElseThrow(() -> new NotFoundException("PartOfSpeech not found"));
        translationWithVocabularyRange.getTranslation().setPartOfSpeech(partOfSpeech);
        Translation savedTranslation = translationRepository.save(translationWithVocabularyRange.getTranslation());
        return Optional.ofNullable(translationWithVocabularyRange.getVocabularyRange())
                .map(vocabularyRange -> {
                    translationWithVocabularyRange.vocabularyRange.setVocabulary(savedTranslation.getVocabulary());
                    return new TranslationWithVocabularyRange(savedTranslation,
                            vocabularyRangeRepository.save(translationWithVocabularyRange.getVocabularyRange()));
                })
                .orElse(new TranslationWithVocabularyRange(savedTranslation, null));
    }

    @Override
    @Transactional
    public void removeTranslationAndVocabularyAndTranslationVariant(@PathVariable Integer id) {
        translationRepository.findById(id).ifPresentOrElse(
                (translation) -> {
                    vocabularyRepository.findVocabularyByTranslationId(id).ifPresent(
                            vocabulary -> vocabularyRepository.deleteById(vocabulary.getId()));
                    translationVariantRepository.findTranslationVariantByTranslationId(id).ifPresent(
                            translationVariant -> translationVariantRepository.deleteById(translationVariant.getId()));
                    translationRepository.deleteById(id);
                },
                () -> {
                    throw new NotFoundException("English word was not found");
                }
        );
    }

    @Override
    @Transactional
    public void removeTranslationAndVocabulary(@PathVariable Integer id) {
        translationRepository.findById(id)
                .ifPresentOrElse(
                        translation -> {
                            vocabularyRepository.findVocabularyByTranslationId(id)
                                    .ifPresentOrElse(
                                            vocabulary -> vocabularyRepository.deleteById(vocabulary.getId()),
                                            () -> {
                                                throw new NotFoundException("English word was not found");
                                            }
                                    );
                            translationRepository.deleteById(id);
                        },
                        () -> {
                            throw new NotFoundException("Translation was not found");
                        }
                );
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
        translationRepository.findById(id)
                .ifPresent(translation -> vocabularyRepository.findVocabularyByTranslationId(id)
                        .ifPresent(vocabulary -> translationVariantRepository.findTranslationVariantByTranslationId(id)
                                .ifPresent(translationVariant -> {
                                    vocabularyRangeRepository.findVocabularyRangeByVocabularyId(vocabulary.getId()).ifPresent(vocabularyRangeRepository::delete);
                                    translationRepository.delete(translation);
                                    vocabularyRepository.delete(vocabulary);
                                    translationVariantRepository.delete(translationVariant);
                                })));
    }

}
