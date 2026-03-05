package guru.qa.niffler.test.grpc;

import guru.qa.niffler.grpc.StreamUserRequest;
import guru.qa.niffler.grpc.UserRequest;
import guru.qa.niffler.grpc.UserResponse;
import guru.qa.niffler.grpc.UsernameRequest;
import guru.qa.niffler.grpc.UsersResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserdataGrpcTest extends BaseGrpcTest{

    @Test
    @DisplayName("GRPC: Should return info about user")
    public void shouldReturnUserInfo(){
        final UsernameRequest usernameRequest = UsernameRequest.newBuilder()
                .setUsername("Ptaha5")
                .build();
        final UserResponse user = userdataBlockingStub.getUser(usernameRequest);
        Assertions.assertNotNull(user);
        Assertions.assertEquals("Ptaha5", user.getUsername());
        Assertions.assertEquals("First", user.getFirstname());
        Assertions.assertEquals("Sure", user.getSurname());
        Assertions.assertEquals("413bef2a-c7b9-11f0-bd21-fa8c6dc05d6e", user.getId());
    }


    @Test
    @DisplayName("GRPC: Should return list users")
    public void shouldReturnUsersList(){
        final UserRequest usernameRequest = UserRequest.newBuilder()
                .setUsername("duck")
                .build();
        final UsersResponse users = userdataBlockingStub.listAllUser(usernameRequest);
        Assertions.assertNotNull(users);
    }

    @Test
    @DisplayName("GRPC: Should return page users")
    public void shouldReturnUsersPage(){
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
}
