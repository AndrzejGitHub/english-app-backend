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
    public class VocabularyArea {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;
    }

// deleted
//        @ManyToMany(mappedBy = "vocabularyAreas")
//        private List<Vocabulary> vocabularies;