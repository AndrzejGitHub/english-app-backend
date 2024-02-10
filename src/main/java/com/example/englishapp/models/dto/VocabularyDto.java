package com.example.englishapp.models.dto;

import com.example.englishapp.models.VocabularyRange;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class VocabularyDto {

    private Integer id;

    @Size(min = 1, max = 255, message = "English word must be between 1 and 255 characters long")
    private String englishWord;

    private String imageURL;

}

