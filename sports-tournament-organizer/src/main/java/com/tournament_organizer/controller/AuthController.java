package com.tournament_organizer.controller;

import com.tournament_organizer.dto.login.LoginRequest;
import com.tournament_organizer.dto.login.LoginResponse;
import com.tournament_organizer.dto.refresh.RefreshResponse;
import com.tournament_organizer.dto.user.UserInDTO;
import com.tournament_organizer.entity.RefreshToken;
import com.tournament_organizer.entity.User;
import com.tournament_organizer.security.JwtUtil;
import com.tournament_organizer.service.UserService;
import com.tournament_organizer.service.auth.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refService;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authManager, JwtUtil jwtUtil,
                          RefreshTokenService refService) {
        this.userService = userService;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.refService = refService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserInDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> handleLogin(@RequestBody LoginRequest loginRequest) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        User user = userService.findByUsername(loginRequest.getUsername());
        String access = jwtUtil.generateToken(user);
        RefreshToken rt = refService.create(user);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", rt.getToken())
                .httpOnly(true).secure(true).sameSite("None")
                .path("/api/v1/auth")
                .maxAge(Duration.ofDays(30)).build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginResponse(access));
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue("refreshToken") String token) {
        User user = refService.verify(token);
        refService.delete(token);
        RefreshToken newRt = refService.create(user);
        String access = jwtUtil.generateToken(user);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", newRt.getToken())
                .httpOnly(true).secure(true).sameSite("None")
                .path("/api/v1/auth")
                .maxAge(Duration.ofDays(30)).build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new RefreshResponse(access));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue("refreshToken") String token) {
        refService.delete(token);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true).secure(true).sameSite("None")
                .path("/api/v1/auth")
                .maxAge(0).build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
