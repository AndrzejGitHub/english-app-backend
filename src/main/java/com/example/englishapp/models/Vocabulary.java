package com.example.englishapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Vocabulary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;

    private String translation;

    private String imageURL;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "Vocabulary-area_pivot",
            joinColumns = @JoinColumn(name = "vocabulary_id"),
            inverseJoinColumns = @JoinColumn(name = "Vocabulary-area_id")
    )
    private List<VocabularyArea> vocabularyAreas;
}

// delete
//    @ManyToMany(mappedBy = "vocabularies")
//    private List<VocabularyArea> vocabularyAreas;

//    @OneToMany(mappedBy = "vocabulary", cascade = CascadeType.ALL)
//    private List<UsageExample> usageExamples;

//    @ManyToMany(cascade = CascadeType.ALL)
////    @JoinTable(
////            name = "lesson_vocabulary",
////            joinColumns = @JoinColumn(name = "vocabulary_id"),
////            inverseJoinColumns = @JoinColumn(name = "lesson_id")
////    )
//    private List<Lesson> lessons;
