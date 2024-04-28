package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {

    SUPER_ADMIN("SUPER_ADMIN"),
    ADMIN("ADMIN"),
    USER("USER");

    public final String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}
