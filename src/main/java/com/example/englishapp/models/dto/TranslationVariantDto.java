package com.example.englishapp.models.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class TranslationVariantDto {

    private Integer id;

    @Size(min = 1, max = 255, message = "Polish translation must be between 1 and 255 characters long")
    private String polishMeaning;

}
