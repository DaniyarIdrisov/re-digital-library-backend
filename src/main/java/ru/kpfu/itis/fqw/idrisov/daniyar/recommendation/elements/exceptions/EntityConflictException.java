package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions;

import org.springframework.http.HttpStatus;

public class EntityConflictException extends ServiceException {

    public EntityConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public EntityConflictException(String message, Object... args) {
        super(message, HttpStatus.CONFLICT, args);
    }
}
