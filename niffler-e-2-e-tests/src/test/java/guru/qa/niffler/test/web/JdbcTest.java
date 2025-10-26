package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.AuthorityEntity;
import guru.qa.niffler.data.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

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
                "cat-name-tx-5",
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
    UserDbClient dbClient = new UserDbClient();
    UserJson user = dbClient.createUser(
        new UserJson(
            null,
            "bird33334",
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
    public void springJdbcTest() {
        UserDbClient dbClient = new UserDbClient();
        UserJson user = dbClient.createUserSpringJdbc(
                new UserJson(
                        null,
                        "pegas1234",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        "123467890",
                        "123467890"
                )
        );
        System.out.println(user);
    }

    @Test
    public void springSearchJdbcTest() {
      DataSource dataSource;
        UserDbClient dbClient = new UserDbClient();
        List<AuthorityEntity> user = dbClient.findAllAuthority();
        System.out.println(user);
    }
}
