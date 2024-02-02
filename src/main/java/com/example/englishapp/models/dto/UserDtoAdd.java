package com.example.englishapp.models.dto;

import com.example.englishapp.models.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.Set;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDtoAdd {

    private Integer id;

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 255, message = "Firstname of user have to contain 1 - 255 chars")
    private String firstName;

    @NotBlank(message = "Surname is required")
    @Size(min = 1, max = 255, message = "Surname of user have to contain 1 - 255 chars")
    private String lastName;

//    @Column
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "Enter valid password")
    private String password;

//    @NotBlank(message = "Confirm Password is mandatory")
//    private String confirmPassword;

    @NotBlank(message = "Email is required")
//    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-zA-z]{2,3}$\n", message = "Enter valid email")
    private String email;

    private Set<UserRole> roles;
}
