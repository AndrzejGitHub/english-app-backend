package com.example.englishapp.models.dto;

import com.example.englishapp.models.VocabularyRange;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class VocabularyDto {

    private Integer id;

    @NotBlank
    @Size(min = 1, max = 255, message = "English word have to contain 1 - 255 chars")
    private String englishWord;

    private String imageURL;

//    private VocabularyRange vocabularyRange;
}

