package com.example.englishapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        TranslationVariant other = (TranslationVariant) obj;
        return Objects.equals(id, other.id) && Objects.equals(polishMeaning, other.polishMeaning);
    }
}
