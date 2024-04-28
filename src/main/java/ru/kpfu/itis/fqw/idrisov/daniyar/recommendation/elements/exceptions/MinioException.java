package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions;

import org.springframework.http.HttpStatus;

public class MinioException extends ServiceException {
    public MinioException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
