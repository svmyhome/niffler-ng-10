package guru.qa.niffler.model;


import guru.qa.niffler.data.entity.UserEntity;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record UserJson(
    UUID id,
    String username,
    String firstname,
    String surname,
    String full_name,
    CurrencyValues currency,
    String photo,
    String photo_small) {

  public static UserJson fromEntity(UserEntity entity) {
    return new UserJson(
        entity.getId(),
        entity.getUsername(),
        entity.getFirstname(),
        entity.getSurname(),
        entity.getFull_name(),
        entity.getCurrency(),
        entity.getPhoto() != null && entity.getPhoto().length > 0 ? new String(entity.getPhoto(),
            StandardCharsets.UTF_8) : null,
        entity.getPhoto_small() != null && entity.getPhoto_small().length > 0 ? new String(
            entity.getPhoto_small(), StandardCharsets.UTF_8) : null
    );
  }

}
