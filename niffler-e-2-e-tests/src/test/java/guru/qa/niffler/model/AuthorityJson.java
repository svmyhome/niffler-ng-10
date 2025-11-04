package guru.qa.niffler.model;

import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

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
