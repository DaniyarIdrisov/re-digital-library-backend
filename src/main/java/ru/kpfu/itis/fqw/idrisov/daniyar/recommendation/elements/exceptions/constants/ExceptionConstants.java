package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionConstants {

    public static final String ACCOUNT_NOT_FOUND_MESSAGE = "Account not found";
    public static final String ACCOUNT_WITH_THIS_EMAIL_NOT_FOUND_MESSAGE = "Account with this email [%s] is not found";
    public static final String ACCOUNT_WITH_THIS_EMAIL_EXISTS_MESSAGE = "Account with this email [%s] already exists";
    public static final String INVALID_LOGIN_OR_PASSWORD_MESSAGE = "Invalid login or password";
    public static final String INVALID_REFRESH_TOKEN_MESSAGE = "Invalid refresh token";
    public static final String EMPTY_FIELD_MESSAGE = "Field [%s] is empty";
    public static final String AUTHOR_NOT_FOUND_MESSAGE = "Author not found";
    public static final String REQUEST_NOT_FOUND_MESSAGE = "Request not found";
    public static final String PUBLICATION_NOT_FOUND_MESSAGE = "Publication not found";
    public static final String KEYWORD_NOT_FOUND_MESSAGE = "Keyword not found";
    public static final String INCORRECT_ACCESS_TOKEN = "Incorrect access token";
}
