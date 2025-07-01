package com.tournament_organizer.exception;

public class TokenRefreshException extends RuntimeException{
    public TokenRefreshException(String message) {
        super(message);
    }
}