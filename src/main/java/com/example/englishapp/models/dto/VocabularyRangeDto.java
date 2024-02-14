package com.example.englishapp.models.dto;

import lombok.*;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class VocabularyRangeDto {

    private Integer id;

    private Integer vocabulary_range;

    private VocabularyDto vocabulary;
}


