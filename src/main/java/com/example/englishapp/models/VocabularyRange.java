package com.example.englishapp.models;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class VocabularyRange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vocabulary_range_id")
    private Integer id;

    private Integer vocabulary_range;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vocabulary_id")
    private Vocabulary vocabulary;
}

