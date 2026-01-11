package guru.qa.niffler.test.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.service.UserApiClient;
import guru.qa.niffler.service.UserClient;
import guru.qa.niffler.service.UserDbClient;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.Isolated;

@Isolated
@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(OrderAnnotation.class)
@Epic("API")
@Feature("User management")
@Story("Friends")
public class UserApiTest {

  private final UserClient userClient = new UserApiClient();
  private final UserApiClient userApiClient = new UserApiClient();
  private final UserDbClient userDbClient = new UserDbClient();

  @User
  @Test
  @DisplayName("API: Should creat new user")
  public void shouldCreateNewUserFromApi(UserJson user) {
    userClient.createUser(user.username(), user.testData().password());
  }

  @Test
  @DisplayName("API: Should creat new income invitation")
  public void shouldCreateIncomeInvitationFromApi() {
    UserJson user1 = null;
    Optional<UserEntity> user = userDbClient.findUserById(
        UUID.fromString("8b8996ed-f701-49fd-a421-31183916d818"));
    if (user.isPresent()) {
      user1 = UserJson.fromEntity(user.get());
    }
    userClient.createIncomeInvitations(user1, 1);
  }

  @Test
  @DisplayName("API: Should creat new outcome invitation")
  public void shouldCreateOutcomeInvitationFromApi() {
    UserJson user1 = null;
    Optional<UserEntity> user = userDbClient.findUserById(
        UUID.fromString("8b8996ed-f701-49fd-a421-31183916d818"));
    if (user.isPresent()) {
      user1 = UserJson.fromEntity(user.get());
    }
    userClient.createOutcomeInvitations(user1, 1);
  }

  @Test
  @DisplayName("API: Should creat friend")
  public void shouldCreateFriendsFromApi() {
    UserJson user1 = null;
    Optional<UserEntity> user = userDbClient.findUserById(
        UUID.fromString("8b8996ed-f701-49fd-a421-31183916d818"));
    if (user.isPresent()) {
      user1 = UserJson.fromEntity(user.get());
    }
    userClient.createFriends(user1, 1);
  }

  @Order(1)
  @User
  @Test
  @DisplayName("API: Should returns empty list when search query doesn't match any user")
  public void shouldReturnEmptyUserListWhenSearchQueryUnmatchedFromApi(UserJson user) {
    List<UserJson> allUsers = userApiClient.getAllUsers(user.username(), "8gwygcydg");
    assertThat(allUsers, empty());
  }

  @Order(2)
  @User
  @Test
  @DisplayName("API: finds user when searching by 'mouse'")
  public void shouldReturnUserWhenQueryIsMouseFromApi(UserJson user) {
    String searchQuery = "mouse";
    List<UserJson> allUsers = userApiClient.getAllUsers(user.username(), searchQuery);
    assertThat(allUsers, hasSize(1));
  }

  @Order(Integer.MAX_VALUE)
  @User
  @Test
  @DisplayName("API: Should return all users")
  public void shouldReturnAllExistingUsersFromApi(UserJson user) {
    List<UserJson> allUsers = userApiClient.getAllUsers(user.username());
    assertThat(allUsers.size(), greaterThan(1));
  }
}
