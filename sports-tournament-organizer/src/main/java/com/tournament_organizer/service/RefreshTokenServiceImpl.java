package com.tournament_organizer.service;

import com.tournament_organizer.exception.TokenRefreshException;
import com.tournament_organizer.entity.RefreshToken;
import com.tournament_organizer.entity.User;
import com.tournament_organizer.repository.RefreshTokenRepository;
import com.tournament_organizer.service.contracts.RefreshTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken create(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(30));
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public User verify(String token) {
        RefreshToken rt = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenRefreshException("Invalid"));
        if (rt.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(rt);
            throw new TokenRefreshException("Expired");
        }
        return rt.getUser();
    }

    @Override
    @Transactional
    public void delete(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    @Override
    @Transactional
    public void delete(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }
}
