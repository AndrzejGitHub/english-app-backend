package com.example.englishapp.models.dto;

import com.example.englishapp.models.UserRole;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDtoAdd {

    private Integer id;

    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters long")
    private String firstName;

    @Size(min = 1, max = 255, message = "Surname must be between 1 and 255 characters long")
    private String lastName;

    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            message = "Please enter a valid email address.")
    private String email;

    @Pattern(regexp = "^.{8,}$", message = "Password must contain at least 8 characters.")
    @Pattern(regexp = ".*[A-Za-z].*", message = "Password must contain at least one letter (uppercase or lowercase).")
    @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one digit.")
    @Pattern(regexp = ".*[@$!%*#?&].*", message = "Password must contain at least one special character among @, $, !, %, *, #, ?, &.")
    private String password;

    @NotEmpty(message = "Role is required")
    private Set<UserRole> roles;
}
