package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import java.util.Date;

import guru.qa.niffler.service.UserDbClient;
import org.junit.jupiter.api.Test;

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
        UserDbClient userDbClient =  new UserDbClient();
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
}
