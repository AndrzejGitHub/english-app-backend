package com.example.englishapp.models;

import jakarta.persistence.*;
import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@ToString
public class Translation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "translation_id")
    private Integer id;

    @JoinColumn(name = "part_of_speech_id")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private PartOfSpeech partOfSpeech;

    @JoinColumn(name = "vocabulary_id")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Vocabulary vocabulary;

    @JoinColumn(name = "translation_variant_id")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private TranslationVariant translationVariant;

}