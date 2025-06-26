package com.tournament_organizer.repository;

import com.tournament_organizer.entity.RefreshToken;
import com.tournament_organizer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
    Optional<RefreshToken> findByIdAndExpiryDateAfter(Integer id, LocalDateTime date);
}
