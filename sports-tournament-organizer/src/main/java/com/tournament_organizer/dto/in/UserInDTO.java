package com.tournament_organizer.dto.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserInDTO {
    @Email(message = "Email should be in a valid format.")
    @Size(min = 10, max = 60, message = "Email should be between 10 and 60 characters long")
    private String email;
    @Size(min = 2, max = 60, message = "Username should be between 2 and 60 characters long")
    private String username;
    @Size(min = 8, max = 60, message = "Password should be between 8 and 60 characters long")
    private String password;

}
