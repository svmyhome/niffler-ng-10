package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.AuthUserEntity;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
            "petr-2",
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

//  @Test
//  public void findAll() {
//    UserDbClient dbClient = new UserDbClient();
//    List<AuthUserEntity> authorityEntities = dbClient.findAll();
//    System.out.println(authorityEntities);
//  }
//
//  @Test
//  public void delete() {
//    UserDbClient dbClient = new UserDbClient();
//    AuthUserEntity ae = new AuthUserEntity();
//    ae.setId(UUID.fromString("ce7e730c-b022-11f0-b26a-fad236acdb6f"));
//    dbClient.delete(ae);
//    System.out.println();
//  }

  @Test
  public void findCategoryAndUser() {
    SpendDbClient spendDbClient = new SpendDbClient();
    Optional<CategoryJson> cat = spendDbClient.findCategoryByNameAndUsername("Бакалея", "duck");
    System.out.println(cat);
  }

//  @Test
//  public void deleteCategory() {
//    SpendDbClient spendDbClient = new SpendDbClient();
//    CategoryEntity category = new CategoryEntity();
//    category.setId(UUID.fromString("02774bff-7815-45f0-a7de-eb15661cecbc"));
//    spendDbClient.deleteCategory(category);
//  }
//
//  @Test
//  public void deleteSpend() {
//    SpendDbClient spendDbClient = new SpendDbClient();
//    SpendEntity spend = new SpendEntity();
//    spend.setId(UUID.fromString("b8e4f162-a8b9-11f0-ba80-6699f0f3bb28"));
//    spendDbClient.deleteSpend(spend);
//  }
//
//  @Test
//  public void getAllSpends() {
//    SpendDbClient spendDbClient = new SpendDbClient();
//    List<SpendJson> spends = spendDbClient.getAllSpends();
//    System.out.println(spends);
//  }
}
