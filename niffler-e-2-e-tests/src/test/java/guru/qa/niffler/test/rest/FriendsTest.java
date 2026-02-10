package guru.qa.niffler.test.rest;


import static guru.qa.niffler.model.FriendshipStatus.FRIEND;
import static guru.qa.niffler.model.FriendshipStatus.INVITE_RECEIVED;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.service.GatewayApiClient;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

@RestTest
@Epic("API")
@Feature("User management")
@Story("Friends")
public class FriendsTest {


    @RegisterExtension
    private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.rest();

    private final GatewayApiClient gatewayApiClient = new GatewayApiClient();

    @User(
            incomeInvitations = 1,
            friends = 2
    )
    @ApiLogin
    @Test
    @DisplayName("API: Should return all friends and income invitations")
    void allFriendsAndIncomeInvitationsShouldBeReturned(UserJson user, @Token String token) {
        UserJson testFriend = user.testData().friends().getFirst();
        UserJson incomeInvitation = user.testData().incomeInvitations().getFirst();
        final List<UserJson> friends = gatewayApiClient.allFriends(token, null);
        step("Check that response not null", () ->
                assertNotNull(friends)
        );
        step("Check that response contains expected users", () ->
                assertEquals(3, friends.size())
        );
        step("Check sorting by status", () ->
                assertEquals(INVITE_RECEIVED, friends.getFirst().friendshipStatus())
        );
        final var foundedInvitation = friends.getFirst();
        final var foundedFriend = friends.getLast();
        step("Check friend in response", () -> {
            assertSame(FRIEND, foundedFriend.friendshipStatus());
            assertEquals(testFriend.id(), foundedFriend.id());
            assertEquals(testFriend.username(), foundedFriend.username());
        });
        step("Check income invitation in response", () -> {
            assertSame(INVITE_RECEIVED, foundedInvitation.friendshipStatus());
            assertEquals(incomeInvitation.id(), foundedInvitation.id());
            assertEquals(incomeInvitation.username(), foundedInvitation.username());
        });
    }

    @User(
            friends = 1
    )
    @ApiLogin
    @Test
    @DisplayName("API: Should delete friend")
    void friendShouldBeDelete(UserJson user, @Token String token) {
        String username = user.testData().friends().getFirst().username();
        gatewayApiClient.removeFriend(token, username);
        final List<UserJson> friends = gatewayApiClient.allFriends(token, null);
        System.out.println();
        step("Current user should not have friend after removing", () ->
                assertTrue(friends.isEmpty())
        );
    }

    @User(
            incomeInvitations = 2
    )
    @ApiLogin
    @Test
    @DisplayName("API: Income invitation should be accepted")
    void incomeInvitationShouldBeAccepted(UserJson user, @Token String token) {
        UserJson testFriend = user.testData().incomeInvitations().getFirst();
        FriendJson friendJson = new FriendJson(testFriend.username());
        UserJson result = gatewayApiClient.acceptInvitation(token, friendJson);
        final List<UserJson> friends = gatewayApiClient.allFriends(token, null);
        step("Check income invitation in response", () -> {
            assertSame(FRIEND, result.friendshipStatus());
            assertEquals(testFriend.id(), friends.getLast().id());
            assertEquals(testFriend.username(), friends.getLast().username());
        });
        System.out.println();
    }

    @User(
            incomeInvitations = 2
    )
    @ApiLogin
    @Test
    @DisplayName("API: Income invitation should be decline")
    void incomeInvitationShouldBeDeclined(UserJson user, @Token String token) {
        UserJson testFriend = user.testData().incomeInvitations().getFirst();
        FriendJson friendJson = new FriendJson(testFriend.username());
        UserJson result = gatewayApiClient.declineInvitation(token, friendJson);
        final List<UserJson> friends = gatewayApiClient.allFriends(token, null);

        step("Check that response contains expected users", () ->
                assertEquals(1, friends.size())
        );
    }
}
