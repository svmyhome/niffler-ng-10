package guru.qa.niffler.data.entity;

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
}
