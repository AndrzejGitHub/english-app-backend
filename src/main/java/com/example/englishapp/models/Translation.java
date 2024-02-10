package com.example.englishapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

//    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "part_of_speech_id")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private PartOfSpeech partOfSpeech;

//    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vocabulary_id")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Vocabulary vocabulary;

//    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "translation_variant_id")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private TranslationVariant translationVariant;


}