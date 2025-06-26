package com.tournament_organizer.service.contracts;

import com.tournament_organizer.entity.RefreshToken;
import com.tournament_organizer.entity.User;

public interface RefreshTokenService {
    RefreshToken create(User user);
    User verify(String token);
    void delete(User user);
    void delete(String token);
}
