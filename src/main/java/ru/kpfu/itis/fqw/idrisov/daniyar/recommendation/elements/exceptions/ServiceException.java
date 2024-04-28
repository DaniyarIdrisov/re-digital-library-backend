package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.apache.commons.lang3.ArrayUtils.isEmpty;

@Getter
public class ServiceException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ServiceException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public ServiceException(String message, HttpStatus httpStatus, Object... args) {
        super(getErrorMessage(message, args));
        this.httpStatus = httpStatus;
    }

    public ServiceException(String message, Throwable cause, HttpStatus httpStatus, Object... args) {
        super(getErrorMessage(message, args), cause);
        this.httpStatus = httpStatus;
    }

    private static String getErrorMessage(String message, Object... args) {
        return isEmpty(args) ? message : String.format(message, args);
    }
}
