package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.AuthUserEntity;
import guru.qa.niffler.data.entity.CategoryEntity;

import java.util.UUID;

public record AuthUserJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("enabled")
        boolean enabled,
        @JsonProperty("accountNonExpired")
        boolean accountNonExpired,
        @JsonProperty("accountNonLocked")
        boolean accountNonLocked,
        @JsonProperty("credentialsNonExpired")
        boolean credentialsNonExpired) {

    public static AuthUserJson fromEntity(AuthUserEntity entity) {
        return new AuthUserJson(
                entity.getId(),
                entity.getUsername(),
                entity.getEnabled(),
                entity.getAccountNonExpired(),
                entity.getAccountNonLocked(),
                entity.getCredentialsNonExpired()
        );
    }
}
