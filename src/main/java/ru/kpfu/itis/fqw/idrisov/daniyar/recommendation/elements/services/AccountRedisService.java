package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services;


import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Account;

public interface AccountRedisService {

    void addRefreshTokenToUser(Account account, String refreshToken);

    void deleteOldAndAddNewRefreshToken(Account account, String refreshToken);

    boolean checkForRefreshTokenExisting(String email, String refreshToken);
}
