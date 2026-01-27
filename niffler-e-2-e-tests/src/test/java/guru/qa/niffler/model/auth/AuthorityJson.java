package guru.qa.niffler.model.auth;

import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import java.util.UUID;
import javax.annotation.Nonnull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityJson {

    private UUID id;
    private Authority authority;
    private UUID userId;

    @Nonnull
    public static AuthorityJson fromEntity(@Nonnull AuthorityEntity entity) {
        AuthorityJson au = new AuthorityJson();
        au.setId(entity.getId());
        au.setAuthority(entity.getAuthority());
        au.setUserId(entity.getId());
        return au;
    }
}
