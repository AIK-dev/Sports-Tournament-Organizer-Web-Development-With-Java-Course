package org.example.sporteventsorganizer.mapper;
import org.example.sporteventsorganizer.dto.in.UserInDTO;
import org.example.sporteventsorganizer.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class UserMapper {
    private final PasswordEncoder encoder;

    public UserMapper(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public User fromDto(UserInDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPasswordHash(encoder.encode(dto.getPassword()));
        return user;
    }
    public void updateDto(User user, UserInDTO dto){
        if(dto.getUsername() != null){
            user.setUsername(dto.getUsername());
        }
        if(dto.getEmail() != null){
            user.setEmail(dto.getEmail());
        }
        if(dto.getPassword() != null){
            user.setPasswordHash(encoder.encode(dto.getPassword()));
        }
    }
}
