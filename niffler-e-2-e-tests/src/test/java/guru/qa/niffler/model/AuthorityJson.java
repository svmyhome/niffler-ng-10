package guru.qa.niffler.model;

import guru.qa.niffler.data.entity.AuthUserEntity;
import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.data.entity.AuthorityEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AuthorityJson {

    private UUID id;
    private Authority authority;
    private UUID userId;

  public static AuthorityJson fromEntity(AuthorityEntity entity) {
    AuthorityJson au = new AuthorityJson();
    au.setId(entity.getId());
    au.setAuthority(entity.getAuthority());
    au.setUserId(entity.getUserId());
    return au;
  }
}
