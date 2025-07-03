package com.tournament_organizer.service.auth;
import com.tournament_organizer.entity.RefreshToken;
import com.tournament_organizer.entity.User;
import com.tournament_organizer.exception.TokenRefreshException;
import com.tournament_organizer.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }
    public RefreshToken create(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(30));
        return refreshTokenRepository.save(refreshToken);
    }

    public User verify(String token) {
        RefreshToken rt = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenRefreshException("Invalid"));
        if (rt.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(rt);
            throw new TokenRefreshException("Expired");
        }
        return rt.getUser();
    }

    @Transactional
    public void delete(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
    @Transactional
    public void delete(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }
}