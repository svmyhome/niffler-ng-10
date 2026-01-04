package guru.qa.niffler.test.rest;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.service.UserApiClient;
import guru.qa.niffler.service.UserClient;
import guru.qa.niffler.service.UserDbClient;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import utils.RandomDataUtils;

public class UserApiTest {
  private final UserClient userClient = new UserApiClient();
  private final UserDbClient userDbClient = new UserDbClient();

  @Test
  public void testGetUserFromApi() {
    UserJson user = userClient.createUser(RandomDataUtils.randomUsername(), "12345");
  }

  @Test
  public void createIncomeInvitationFromApi() {
    UserJson user1 = null;
    Optional<UserEntity> user = userDbClient.findUserById(
        UUID.fromString("8b8996ed-f701-49fd-a421-31183916d818"));
    if (user.isPresent()) {
      user1 = UserJson.fromEntity(user.get());
    }
    userClient.createIncomeInvitations(user1, 1);
  }

  @Test
  public void createOutocomeInvitationFromApi() {
    UserJson user1 = null;
    Optional<UserEntity> user = userDbClient.findUserById(
        UUID.fromString("8b8996ed-f701-49fd-a421-31183916d818"));
    if (user.isPresent()) {
      user1 = UserJson.fromEntity(user.get());
    }
    userClient.createOutcomeInvitations(user1, 1);
  }

  @Test
  public void createFriendsFromApi() {
    UserJson user1 = null;
    Optional<UserEntity> user = userDbClient.findUserById(
        UUID.fromString("8b8996ed-f701-49fd-a421-31183916d818"));
    if (user.isPresent()) {
      user1 = UserJson.fromEntity(user.get());
    }
    userClient.createFriends(user1, 1);
  }
}
