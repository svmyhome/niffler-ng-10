package guru.qa.niffler.test.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.FriendshipStatus;
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
import org.junit.jupiter.api.Test;


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
            user1 = UserJson.fromEntity(user.get(), FriendshipStatus.INVITE_RECEIVED);
        }
        userClient.addIncomeInvitation(user1, 1);
    }

    @Test
    @DisplayName("API: Should creat new outcome invitation")
    public void shouldCreateOutcomeInvitationFromApi() {
        UserJson user1 = null;
        Optional<UserEntity> user = userDbClient.findUserById(
                UUID.fromString("8b8996ed-f701-49fd-a421-31183916d818"));
        if (user.isPresent()) {
            user1 = UserJson.fromEntity(user.get(), FriendshipStatus.INVITE_SENT);
        }
        userClient.addOutcomeInvitation(user1, 1);
        System.out.println(user1);
    }

    @Test
    @DisplayName("API: Should creat friend")
    public void shouldCreateFriendsFromApi() {
        UserJson user1 = null;
        Optional<UserEntity> user = userDbClient.findUserById(
                UUID.fromString("8b8996ed-f701-49fd-a421-31183916d818"));
        if (user.isPresent()) {
            user1 = UserJson.fromEntity(user.get(), FriendshipStatus.FRIEND);
        }
        userClient.addFriend(user1, 1);
    }

    @User
    @Test
    @DisplayName("API: Should returns empty list when search query doesn't match any user")
    public void shouldReturnEmptyUserListWhenSearchQueryUnmatchedFromApi(UserJson user) {
        List<UserJson> allUsers = userApiClient.getAllUsers(user.username(), "8gwygcydg");
        assertThat(allUsers, empty());
    }

    @User
    @Test
    @DisplayName("API: finds user when searching by 'mouse'")
    public void shouldReturnUserWhenQueryIsMouseFromApi(UserJson user) {
        String searchQuery = "mouse";
        List<UserJson> allUsers = userApiClient.getAllUsers(user.username(), searchQuery);
        assertThat(allUsers, hasSize(1));
    }

    @Test
    @ApiLogin(username = "mouse", password = "12345")
    @DisplayName("API: get all friends by User")
    public void shouldReturnAllFriendsByUserFromApi(UserJson user) {
        List<UserJson> allFriends = userApiClient.getAllFriends(user.username());
        assertThat(allFriends.size(), greaterThanOrEqualTo(1));
        List<UserJson>  asdf = allFriends.stream().filter(f -> f.friendshipStatus() != null).toList();
        System.out.println(asdf);
        System.out.println(allFriends);
    }

}
