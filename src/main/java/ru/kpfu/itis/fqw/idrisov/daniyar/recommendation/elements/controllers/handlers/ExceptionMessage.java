package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.controllers.handlers;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Сообщение описывающее возникшую исключительную ситуацию.
 */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExceptionMessage {

    /**
     * Описание пути, по которому возникла исключительная ситуация
     */
    String endpoint;

    /**
     * Сообщение исключения
     */
    String message;

    /**
     * Детальное сообщение исключения
     */
    String detailMessage;

    /**
     * Наименование исключения
     */
    String exceptionName;

    int code;
}
