package guru.qa.niffler.test.rest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.page.RestResponsePage;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.service.GatewayV2ApiClient;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

@RestTest
@Epic("API")
@Feature("User management")
@Story("Friends")
public class FriendsV2Test {


    @RegisterExtension
    private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.rest();

    private final GatewayV2ApiClient gatewayV2ApiClient = new GatewayV2ApiClient();

    @User(
            incomeInvitations = 1,
            friends = 2
    )
    @ApiLogin
    @Test
    void allFriendsAndIncomeInvitationsShouldBeReturned(@Token String token) {
        final RestResponsePage<UserJson> result = gatewayV2ApiClient.allFriends(token, 0, 10, List.of("username,asc"), null);
        assertEquals(3, result.getContent().size());
        System.out.println(result);
    }
}
