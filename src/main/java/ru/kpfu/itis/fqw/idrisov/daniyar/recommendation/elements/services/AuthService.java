package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services;


import ru.kpfu.itis.fqw.idrisov.daniyar.auth.model.*;

public interface AuthService {

    AccountDto signUp(SignUpDto dto);

    JwtDto signIn(AuthDto dto);

    JwtDto refreshToken(RefreshDto refreshDto);
}
