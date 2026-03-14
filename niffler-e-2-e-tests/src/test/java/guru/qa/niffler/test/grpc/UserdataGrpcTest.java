package guru.qa.niffler.test.grpc;

import static guru.qa.niffler.model.FriendshipStatus.FRIEND;
import static guru.qa.niffler.model.FriendshipStatus.INVITE_SENT;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import guru.qa.niffler.grpc.FriendRequest;
import guru.qa.niffler.grpc.FriendshipRequest;
import guru.qa.niffler.grpc.StreamUserRequest;
import guru.qa.niffler.grpc.UserPageResponse;
import guru.qa.niffler.grpc.UserRequest;
import guru.qa.niffler.grpc.UserResponse;
import guru.qa.niffler.grpc.UsernameRequest;
import guru.qa.niffler.grpc.UsersResponse;
import guru.qa.niffler.jupiter.annotation.meta.GrpcTest;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.user.UserJson;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@GrpcTest
public class UserdataGrpcTest extends BaseGrpcTest {

    @Test
    @DisplayName("GRPC: Should return info about user")
    public void shouldReturnUserInfo() {
        final UsernameRequest usernameRequest = UsernameRequest.newBuilder()
                .setUsername("Ptaha5")
                .build();
        final UserResponse user = userdataBlockingStub.getUser(usernameRequest);
        Assertions.assertAll("User response validation",
                () -> assertNotNull(user, "User response should not be null"),
                () -> assertEquals("Ptaha5", user.getUsername(), "Username mismatch"),
                () -> assertEquals("First", user.getFirstname(), "Firstname mismatch"),
                () -> assertEquals("Sure", user.getSurname(), "Surname mismatch"),
                () -> assertEquals("413bef2a-c7b9-11f0-bd21-fa8c6dc05d6e", user.getId(), "User ID mismatch"));
    }

    @Test
    @DisplayName("GRPC: Should return list users")
    public void shouldReturnUsersList() {
        final UserRequest usernameRequest = UserRequest.newBuilder()
                .setUsername("duck")
                .build();
        final UsersResponse users = userdataBlockingStub.listAllUser(usernameRequest);
        step("List users not null", () -> assertNotNull(users));
    }

    @Test
    @DisplayName("GRPC: Should return page users")
    public void shouldReturnUsersPage() {
        final StreamUserRequest usernameRequest = StreamUserRequest.newBuilder()
                .setUsername("duck")
                .setPage(0)
                .setSize(2)
                .build();

        UserPageResponse users = userdataBlockingStub.getAllPage(usernameRequest);

        step("totalElements not null", () -> Assertions.assertEquals(3406, users.getTotalElements()));
        step("totalPages not null", () -> Assertions.assertEquals(1703, users.getTotalPages()));
        step("first is true", () -> Assertions.assertTrue(users.getFirst()));
        step("size is 2", () -> Assertions.assertEquals(2, users.getSize()));
        step("edges is not null", () -> Assertions.assertEquals("артём.селиверстов", users.getUsers(1).getUsername()));
    }

    @Test
    @DisplayName("GRPC: Should return list friends")
    void getUserFriends() {
        final FriendRequest friendRequest = FriendRequest.newBuilder()
                .setUsername("duck")
                .build();
        final UsersResponse users = userdataBlockingStub.listAllFriends(friendRequest);
        step("List friends not null", () -> assertNotNull(users));
    }

    @Test
    @DisplayName("GRPC: Should return list of friends filtered by search query")
    void getUserFriendsWithSearchQuery() {
        final FriendRequest friendRequest = FriendRequest.newBuilder()
                .setUsername("duck")
                .setSearchQuery("ele")
                .build();
        List<UserResponse> users = userdataBlockingStub.listAllFriends(friendRequest).getUserList();
        UserResponse user = users.getFirst();

        Assertions.assertAll("User response validation",
                () -> assertNotNull(user.getUsername(), "User response should not be null"),
                () -> assertEquals("elephant", user.getUsername(), "Username mismatch"),
                () -> assertEquals(FRIEND.name(), user.getFriendshipStatus().name(), "Firstname mismatch"));
    }

    @Test
    @User
    @DisplayName("GRPC: Should send invitation")
    void sendFriendshipInvite(UserJson user) {
        final FriendshipRequest friendshipRequest = FriendshipRequest.newBuilder()
                .setUsername("duck")
                .setTargetUsername(user.username())
                .build();
        final UserResponse users = userdataBlockingStub.sendInvitation(friendshipRequest);
        step("Invitation sent to user", () -> assertEquals(INVITE_SENT.toString(), users.getFriendshipStatus().toString()));
    }

    @Test
    @User(
            incomeInvitations = 1
    )
    @DisplayName("GRPC: Should create friend")
    void createFriendship(UserJson user) {
        final FriendshipRequest friendshipRequest = FriendshipRequest.newBuilder()
                .setUsername(user.username())
                .setTargetUsername(user.testData().incomeInvitations().getFirst().username())
                .build();
        userdataBlockingStub.acceptInvitationRequest(friendshipRequest);
        final var requestor = userdataBlockingStub.listAllFriends(FriendRequest.newBuilder()
                .setUsername(user.username())
                .build());
        final var addressee = userdataBlockingStub.listAllFriends(FriendRequest.newBuilder()
                .setUsername(user.testData().incomeInvitations().getFirst().username())
                .build());
        step("Check requester get friend request", () -> assertEquals(1, requestor.getUserCount()));
        step("Check addressee get friend request", () -> assertEquals(1, addressee.getUserCount()));
    }

    @Test
    @User(
            incomeInvitations = 1
    )
    @DisplayName("GRPC: Should decline incoming friend")
    void declineIncoming(UserJson user) {
        final FriendshipRequest friendshipRequest = FriendshipRequest.newBuilder()
                .setUsername(user.username())
                .setTargetUsername(user.testData().incomeInvitations().getFirst().username())
                .build();
        userdataBlockingStub.declineInvitationRequest(friendshipRequest);
        final var incomeInvitationList = userdataBlockingStub.listAllFriends(FriendRequest.newBuilder()
                .setUsername(user.username())
                .build());
        step("Check friend and invitation lists are empty", () -> assertEquals(0, incomeInvitationList.getUserCount()));
    }

    @Test
    @User(
            friends = 1
    )
    @DisplayName("GRPC: Should delete incoming friend")
    void deleteFriend(UserJson user) {
        final FriendshipRequest friendshipRequest = FriendshipRequest.newBuilder()
                .setUsername(user.username())
                .setTargetUsername(user.testData().friends().getFirst().username())
                .build();
        userdataBlockingStub.removeFriend(friendshipRequest);
        final var friends = userdataBlockingStub.listAllFriends(FriendRequest.newBuilder()
                .setUsername(user.username())
                .build());
        step("Check list friends are empty", () -> assertEquals(0, friends.getUserCount()));
    }
}
