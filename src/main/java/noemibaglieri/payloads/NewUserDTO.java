package noemibaglieri.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import noemibaglieri.enums.Role;

public record NewUserDTO(

        @NotBlank(message = "Username is required")
        String username,

        @Email(message = "Email must be valid")
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password,

        Role role
) {}