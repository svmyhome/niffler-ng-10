package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.AuthorityEntity;
import guru.qa.niffler.data.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import utils.RandomDataUtils;

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
                RandomDataUtils.randomUsername(),
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
            RandomDataUtils.randomUsername(),
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
            RandomDataUtils.randomUsername(),
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
            RandomDataUtils.randomUsername(),
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
  public void springCategoryUsernameJdbcTest() {
    SpendDbClient dbClient = new SpendDbClient();
    List<CategoryJson> user = dbClient.findAllCategories("duck");
    System.out.println(user);
  }

  @Test
  public void springSpendUsernameJdbcTest() {
    SpendDbClient dbClient = new SpendDbClient();
    List<SpendJson> user = dbClient.findSpendsByUserName("duck");
    System.out.println(user);
  }

  @Test
    public void findAll() {
      UserDbClient dbClient = new UserDbClient();
      List<AuthorityEntity> authorityEntities = dbClient.findAll();
      System.out.println(authorityEntities);
  }

    @Test
    public void delete() {
        UserDbClient dbClient = new UserDbClient();
        AuthorityEntity ae = new AuthorityEntity();
        ae.setUserId(UUID.fromString("5f7f3e38-02db-4c95-8af0-8c6e313497f5"));
        AuthorityEntity ae2 = new AuthorityEntity();
        ae2.setUserId(UUID.fromString("53d26f86-5312-42ab-8861-796ca8d41f08"));
        dbClient.delete(ae, ae2);
        System.out.println();
    }
}
