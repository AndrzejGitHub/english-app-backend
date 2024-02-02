package com.example.englishapp.models.dto;

import com.example.englishapp.models.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDto {

    private Integer id;

    @NotBlank(message = "Name is required")
    private String firstName;

    @NotBlank(message = "Surname is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[\\w-]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Enter valid email")
    private String email;

    private Set<UserRole> roles;
}
