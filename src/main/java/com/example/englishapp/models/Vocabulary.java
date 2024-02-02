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
    @Column(name = "vocabulary_id")
    private Integer id;

    private String englishWord;

    private String imageURL;

//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "vocabulary_range_id")
//    private VocabularyRange vocabularyRange;

}
