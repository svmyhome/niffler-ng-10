package guru.qa.niffler.model.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.spend.CurrencyValues;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record UserJson(
    UUID id,
    String username,
    String firstname,
    String surname,
    String fullname,
    CurrencyValues currency,
    String photo,
    String photoSmall,
    @JsonIgnore
    FriendshipStatus friendshipStatus,
    @JsonIgnore
    TestData testData) {

  public static UserJson fromEntity(UserEntity entity) {
    return new UserJson(
        entity.getId(),
        entity.getUsername(),
        entity.getFirstname(),
        entity.getSurname(),
        entity.getFullname(),
        entity.getCurrency(),
        entity.getPhoto() != null && entity.getPhoto().length > 0 ? new String(entity.getPhoto(),
            StandardCharsets.UTF_8) : null,
        entity.getPhotoSmall() != null && entity.getPhotoSmall().length > 0 ? new String(
            entity.getPhotoSmall(), StandardCharsets.UTF_8) : null,
        null,
        null
    );
  }

  public UserJson addTestData(TestData testData) {
    return new UserJson(
        id,
        username,
        firstname,
        surname,
        fullname,
        currency,
        photo,
        photoSmall,
        friendshipStatus,
        testData);
  }
}
