package com.tournament_organizer.mapper;
import com.tournament_organizer.dto.in.UserInDTO;
import com.tournament_organizer.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
