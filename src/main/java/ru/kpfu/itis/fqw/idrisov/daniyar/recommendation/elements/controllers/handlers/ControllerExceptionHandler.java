package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.controllers.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions.EntityConflictException;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions.EntityNotFoundException;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions.MinioException;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions.ServiceBadRequestException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<ExceptionMessage> handleEntityNotFoundException(
            EntityNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionMessage.builder()
                        .message(exception.getMessage())
                        .exceptionName(exception.getClass().getSimpleName())
                        .code(HttpStatus.NOT_FOUND.value())
                        .build());
    }

    @ExceptionHandler(EntityConflictException.class)
    public final ResponseEntity<ExceptionMessage> handleEntityConflictException(
            EntityConflictException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ExceptionMessage.builder()
                        .message(exception.getMessage())
                        .exceptionName(exception.getClass().getSimpleName())
                        .code(HttpStatus.CONFLICT.value())
                        .build());
    }

    @ExceptionHandler(ServiceBadRequestException.class)
    public final ResponseEntity<ExceptionMessage> handleServiceBadRequestException(
            ServiceBadRequestException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionMessage.builder()
                        .message(exception.getMessage())
                        .exceptionName(exception.getClass().getSimpleName())
                        .code(HttpStatus.BAD_REQUEST.value())
                        .build());
    }

    @ExceptionHandler(MinioException.class)
    public final ResponseEntity<ExceptionMessage> handleMinioExceptionException(
            MinioException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionMessage.builder()
                        .message(exception.getMessage())
                        .exceptionName(exception.getClass().getSimpleName())
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .build());
    }
}
