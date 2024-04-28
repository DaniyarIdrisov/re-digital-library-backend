package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.security.providers;

import io.jsonwebtoken.Claims;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Account;
;

public interface JwtProvider {

    String generateAccessToken(Account account);

    String generateRefreshToken(Account account);

    boolean validateAccessToken(String accessToken);

    boolean validateRefreshToken(String refreshToken);

    Claims getAccessClaims(String token);

    Claims getRefreshClaims(String token);
}
