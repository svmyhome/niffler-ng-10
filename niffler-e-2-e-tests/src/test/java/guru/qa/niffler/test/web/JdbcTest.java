package guru.qa.niffler.test.web;

import guru.qa.niffler.model.*;
import guru.qa.niffler.service.AuthUserDbClient;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class JdbcTest {

    @Test
    void txTest() {
        SpendDbClient spendDbClient = new SpendDbClient();
        SpendJson spendJson = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "cat-name-tx-2",
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "cat-name-tx",
                        "duck"
                )
        );
        System.out.println(spendJson);
    }

    @Test
    void userDAOtest() {
        UserDbClient userDbClient = new UserDbClient();
        UserJson user = userDbClient.createUser(
                new UserJson(
                        null,
                        "bird1111",
                        "First",
                        "Sure",
                        "Full",
                        CurrencyValues.RUB,
                        "123467890",
                        "123467890"
                )
        );
        System.out.println(user);
    }

    @Test
    public void successTransactionTest() {
        AuthUserDbClient dbClient = new AuthUserDbClient();
        AuthUserJson user = new AuthUserJson();
        user.setUsername("qwertyu12");
        user.setPassword("qaz1");
        user.setCredentialsNonExpired(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setEnabled(true);
        dbClient.createUser(user);
    }
}
