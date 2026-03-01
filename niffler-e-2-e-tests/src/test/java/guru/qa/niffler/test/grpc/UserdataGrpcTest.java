package guru.qa.niffler.test.grpc;

import guru.qa.niffler.grpc.UserResponse;
import guru.qa.niffler.grpc.UsernameRequest;
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
}
