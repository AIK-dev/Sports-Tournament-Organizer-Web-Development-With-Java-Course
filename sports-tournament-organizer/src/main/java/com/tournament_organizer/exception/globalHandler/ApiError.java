package com.tournament_organizer.exception.globalHandler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ApiError {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private OffsetDateTime timestamp;
    private int status;
    private HttpStatus error;
    private String message;
    private String path;
    public static ApiError of(HttpStatus status, String message, String path){
        return new ApiError(OffsetDateTime.now(), status.value(), status, message, path);
    }
}
