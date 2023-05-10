package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResourceException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ResourceException(HttpStatus httpStatus, String s) {
        super(s);
        this.httpStatus = httpStatus;
    }
}