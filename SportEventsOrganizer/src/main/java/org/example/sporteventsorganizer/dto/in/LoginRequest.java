package org.example.sporteventsorganizer.dto.in;

import jakarta.validation.constraints.Size;

public class LoginRequest {
    @Size(min = 2, max = 60, message = "Username should be between 2 and 60 characters long")
    private String username;

    @Size(min = 8, max = 60, message = "Password should be between 8 and 60 characters long")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
