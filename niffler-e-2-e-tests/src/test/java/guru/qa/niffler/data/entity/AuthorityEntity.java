package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.AuthorityJson;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityEntity implements Serializable {

  private UUID id;
  private Authority authority;
  private UUID userId;

  public static AuthorityEntity fromJson(AuthorityJson json) {
    AuthorityEntity aue = new AuthorityEntity();
    aue.setId(json.getId());
    aue.setAuthority(json.getAuthority());
    aue.setUserId(json.getUserId());
    return aue;
  }
}
