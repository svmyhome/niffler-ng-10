package guru.qa.niffler.test.grpc;

import static guru.qa.niffler.model.FriendshipStatus.INVITE_SENT;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import guru.qa.niffler.grpc.FriendRequest;
import guru.qa.niffler.grpc.FriendshipRequest;
import guru.qa.niffler.grpc.StreamUserRequest;
import guru.qa.niffler.grpc.UpdateUserRequest;
import guru.qa.niffler.grpc.UserRequest;
import guru.qa.niffler.grpc.UserResponse;
import guru.qa.niffler.grpc.UsernameRequest;
import guru.qa.niffler.grpc.UsersResponse;
import guru.qa.niffler.jupiter.annotation.meta.GrpcTest;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.user.UserJson;
import java.util.ArrayList;
import java.util.Iterator;
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
        Assertions.assertNotNull(user);
        assertEquals("Ptaha5", user.getUsername());
        assertEquals("First", user.getFirstname());
        assertEquals("Sure", user.getSurname());
        assertEquals("413bef2a-c7b9-11f0-bd21-fa8c6dc05d6e", user.getId());
    }


    @Test
    @DisplayName("GRPC: Should return list users")
    public void shouldReturnUsersList() {
        final UserRequest usernameRequest = UserRequest.newBuilder()
                .setUsername("duck")
                .build();
        final UsersResponse users = userdataBlockingStub.listAllUser(usernameRequest);
        Assertions.assertNotNull(users);
    }

    @Test
    @DisplayName("GRPC: Should return page users")
    public void shouldReturnUsersPage() {
        final StreamUserRequest usernameRequest = StreamUserRequest.newBuilder()
                .setUsername("duck")
                .setPage(0)
                .setSize(15)
                .build();

        Iterator<UserResponse> usersIterator = userdataBlockingStub.getAllPage(usernameRequest);

        List<UserResponse> usersList = new ArrayList<>();
        while (usersIterator.hasNext()) {
            usersList.add(usersIterator.next());
        }

        Assertions.assertNotNull(usersList);
        Assertions.assertFalse(usersList.isEmpty(), "Список пользователей не должен быть пустым");
        Assertions.assertTrue(usersList.size() <= 15, "Размер не должен превышать 15");
    }

    @Test
    @DisplayName("GRPC: Should return list friends")
    void getUserFriends() {
        final FriendRequest friendRequest = FriendRequest.newBuilder()
                .setUsername("duck")
                .build();
        final UsersResponse users = userdataBlockingStub.listAllFriends(friendRequest);
        Assertions.assertNotNull(users);
    }

    @Test
    @User
    @DisplayName("GRPC: Should return list friends")
    void sendFriendshipInvite(UserJson user) {
        final FriendshipRequest friendshipRequest = FriendshipRequest.newBuilder()
                .setUsername("duck")
                .setTargetUsername(user.username())
                .build();
        final UserResponse users = userdataBlockingStub.sendInvitation(friendshipRequest);
        assertEquals(INVITE_SENT.toString(), users.getFriendshipStatus().toString());
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
        userdataBlockingStub.acceptFriendshipRequest(friendshipRequest);
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
        userdataBlockingStub.declineFriendshipRequest(friendshipRequest);
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

    @Test
    @User
    @DisplayName("GRPC: Should update user")
    void updateUser(UserJson user) {
        final UserResponse updatedUser1 = userdataBlockingStub.getUser(UsernameRequest.newBuilder()
                .setUsername(user.username())
                .build());
        UpdateUserRequest updateUserRequest = UpdateUserRequest.newBuilder()
                .setUsername(user.username())
                .setFullname("Тест")
//                .setCurrency(CurrencyValues.valueOf(user.currency().name()))
//                .setPhoto(user.photo())
//                .setPhotoSmall(user.photoSmall())
//                .setFriendshipStatus(FriendshipStatus.valueOf(user.friendshipStatus().name()))
                .build();
        userdataBlockingStub.updateUser(updateUserRequest);


        final UserResponse updatedUser = userdataBlockingStub.getUser(UsernameRequest.newBuilder()
                .setUsername(user.username())
                .build());
        step("Check list friends are empty", () -> assertEquals("Тест", updatedUser.getFullname()));
    }

}
