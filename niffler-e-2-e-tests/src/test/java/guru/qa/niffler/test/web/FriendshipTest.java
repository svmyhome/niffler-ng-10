package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.service.UserDbClient;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class FriendshipTest {

  private static final Config CFG = Config.getInstance();

  @Test
  void addFriendJdbcTest() {
    UserDbClient userDbClient = new UserDbClient();
    UserEntity requester = new UserEntity();
    requester.setId(UUID.fromString("726d9c7c-b8de-11f0-84ca-faa8bfae1c90"));
    UserEntity addresser = new UserEntity();
    addresser.setId(UUID.fromString("8016ff0c-b8df-11f0-bb7d-faa8bfae1c90"));
    userDbClient.addFriend(requester, addresser);
  }

  @Test
  void addIncomeInvitationJdbcTest() {
    UserDbClient userDbClient = new UserDbClient();
    UserEntity requester = new UserEntity();
    requester.setId(UUID.fromString("85a45678-b8c5-11f0-82bc-faa8bfae1c90"));
    UserEntity addresser = new UserEntity();
    addresser.setId(UUID.fromString("cde87b76-b8c5-11f0-9e12-faa8bfae1c90"));
    userDbClient.addIncomeInvitation(requester, addresser);
  }

  @Test
  void addOutcomeInvitationTest() {
    UserDbClient userDbClient = new UserDbClient();
    UserEntity requester = new UserEntity();
    requester.setId(UUID.fromString("cde87b76-b8c5-11f0-9e12-faa8bfae1c90"));
    UserEntity addresser = new UserEntity();
    addresser.setId(UUID.fromString("51358a50-b6f5-11f0-9e9d-ea06c42c5790"));
    userDbClient.addOutcomeInvitation(requester, addresser);
  }

  @Test
  void findFriendshipsByRequesterIdTest() {
    UserDbClient userDbClient = new UserDbClient();
    List<FriendshipEntity> friendshipEntities = userDbClient.findFriendshipsByRequesterId(
        UUID.fromString("b00e1bff-99da-46d0-81da-2394a412cd0d"));
    System.out.println(friendshipEntities);
  }

  @Test
  void findFriendshipsByAddresseeIdTest() {
    UserDbClient userDbClient = new UserDbClient();
    List<FriendshipEntity> friendshipEntities = userDbClient.findFriendshipsByRequesterId(
        UUID.fromString("b00e1bff-99da-46d0-81da-2394a412cd0d"));
    System.out.println(friendshipEntities);
  }

  @Test
  void deleteFriendshipTest() {
    UserDbClient userDbClient = new UserDbClient();
    FriendshipEntity friendship = new FriendshipEntity();
    UserEntity requester = new UserEntity();
    requester.setId(UUID.fromString("85a45678-b8c5-11f0-82bc-faa8bfae1c90"));
    UserEntity addresser = new UserEntity();
    addresser.setId(UUID.fromString("cde87b76-b8c5-11f0-9e12-faa8bfae1c90"));
    friendship.setRequester(requester);
    friendship.setAddressee(addresser);
    userDbClient.deleteFriendship(friendship);
  }

  @Test
  void findFridByIdTest() {
    UserDbClient userDbClient = new UserDbClient();
    userDbClient.findUserById(UUID.fromString("b00e1bff-99da-46d0-81da-2394a412cd0d"));
  }
}
