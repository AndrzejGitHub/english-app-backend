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
public class TranslationVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "translation_variant_id")
    private Integer id;

    private String polishMeaning;

}
