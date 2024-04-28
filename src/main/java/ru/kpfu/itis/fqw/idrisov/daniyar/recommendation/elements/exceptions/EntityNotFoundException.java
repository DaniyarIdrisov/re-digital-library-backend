package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends ServiceException {

    public EntityNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public EntityNotFoundException(String message, Object... args) {
        super(message, HttpStatus.NOT_FOUND, args);
    }
}
