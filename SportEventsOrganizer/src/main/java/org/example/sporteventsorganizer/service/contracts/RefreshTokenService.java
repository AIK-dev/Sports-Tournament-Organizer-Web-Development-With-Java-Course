package org.example.sporteventsorganizer.service.contracts;

import org.example.sporteventsorganizer.models.RefreshToken;
import org.example.sporteventsorganizer.models.User;

public interface RefreshTokenService {
    RefreshToken create(User user);
    User verify(String token);
    void delete(User user);
    void delete(String token);
}
