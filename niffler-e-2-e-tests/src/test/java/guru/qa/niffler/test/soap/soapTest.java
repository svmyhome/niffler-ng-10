package guru.qa.niffler.test.soap;

import guru.qa.niffler.jupiter.annotation.meta.SoapTest;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.service.UserdataSoapClient;
import java.io.IOException;
import jaxb.userdata.CurrentUserRequest;
import jaxb.userdata.UserResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SoapTest
public class soapTest {

    private final UserdataSoapClient userdataSoapClient = new UserdataSoapClient();

    @Test
    @User
    public void currentUserTest(UserJson user) throws IOException {
        CurrentUserRequest request = new CurrentUserRequest();
        request.setUsername(user.username());
        UserResponse response = userdataSoapClient.currentUser(request);
        Assertions.assertEquals(
                user.username(),
                response.getUser().getUsername()
        );
    }
}
