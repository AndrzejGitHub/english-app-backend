package com.example.englishapp.models.dto;

import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Polish translation is required")
    @Size(min = 1, max = 255, message = "Polish translation must be between 1 and 255 characters long")
    private String polishMeaning;

}
