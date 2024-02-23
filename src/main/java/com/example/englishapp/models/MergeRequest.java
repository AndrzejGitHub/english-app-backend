package com.example.englishapp.models;

import lombok.*;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MergeRequest {
    private Vocabulary vocabulary;
    private Integer vocabularyRange;
}
