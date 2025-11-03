package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.entity.AuthUserEntity;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.impl.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import utils.RandomDataUtils;

public class TransactionTest {

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
  public void findCategoriesByUsernameTest() {
    SpendDbClient dbClient = new SpendDbClient();
    List<CategoryJson> user = dbClient.findAllCategories("duck");
    System.out.println(user);
  }

  @Test
  public void findSpendsByUsernameTest() {
    SpendDbClient dbClient = new SpendDbClient();
    List<SpendJson> user = dbClient.findSpendsByUserName("duck");
    System.out.println(user);
  }

  @Test
  public void findAll() {
    UserDbClient userdbClient = new UserDbClient();
    List<AuthUserJson> authorityJson = userdbClient.findAll();
    System.out.println(authorityJson);
  }

  @Test
  public void delete() {
    UserDbClient dbClient = new UserDbClient();
    AuthUserEntity ae = new AuthUserEntity();
    ae.setId(UUID.fromString("2db76cc4-8de4-4eee-9fee-dacd3c44b5f6"));
    dbClient.delete(ae);
    System.out.println();
  }

  @Test
  public void findCategoryAndUser() {
    SpendDbClient spendDbClient = new SpendDbClient();
    Optional<CategoryJson> category = spendDbClient.findCategoryByNameAndUsername("duck", "Машина");
    category.stream().forEach(System.out::println);
  }

  @Test
  public void createCategory() {
    SpendDbClient spendDbClient = new SpendDbClient();
    CategoryJson categoryJson = spendDbClient.createCategory(
        new CategoryJson(
            null,
            "QAZ1qaz",
            "duck",
            false
        )
    );
    System.out.println(categoryJson);
  }

  @Test
  public void deleteCategory() {
    SpendDbClient spendDbClient = new SpendDbClient();
    CategoryEntity category = new CategoryEntity();
    category.setId(UUID.fromString("2a6a67f0-b5bb-11f0-8a0e-aa5c32f82d84"));
    spendDbClient.deleteCategory(category);
  }

  @Test
  public void deleteSpend() {
    SpendDbClient spendDbClient = new SpendDbClient();
    SpendEntity spend = new SpendEntity();
    spend.setId(UUID.fromString("9d8cfc1e-b5bd-11f0-bfb0-aa5c32f82d84"));
    spendDbClient.deleteSpend(spend);
  }

  @Test
  public void findAllSpends() {
    SpendDbClient spendDbClient = new SpendDbClient();
    List<SpendJson> spends = spendDbClient.findAllSpends();
    System.out.println(spends);
  }

  @Test
  public void findSpendById() {
    SpendDbClient spendDbClient = new SpendDbClient();
    Optional<SpendJson> spend = spendDbClient.findSpendById("1328a312-b5bc-11f0-a017-aa5c32f82d84");
    System.out.println(spend);
  }

  @Test
  public void updateSpendById() {
    SpendDbClient spendDbClient = new SpendDbClient();
    SpendJson spend = spendDbClient.updateSpend(
        new SpendJson(UUID.fromString("888ca6da-b6e3-11f0-8e67-ea06c42c5790"),
            new Date(new Date().getTime()),
            new CategoryJson(UUID.fromString("888ba94c-b6e3-11f0-8e67-ea06c42c5790"), null, null,
                false),
            CurrencyValues.RUB,
            180.0,
            "11111",
            "duck"
        )
    );
  }

  @Test
  public void updateCategoryById() {
    SpendDbClient spendDbClient = new SpendDbClient();
    CategoryJson category = spendDbClient.updateCategory(
        new CategoryJson(
            UUID.fromString("9d8c393c-b5bd-11f0-bfb0-aa5c32f82d84"),
            "1q1a1a1", "duck", false)
    );
  }

  private static final Config CFG = Config.getInstance();

  @Test
  public void createUserJdbcWithTransactionTest() {
    AuthUserJson user = getNewUserJson();
    XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
        CFG.authJdbcUrl(),
        CFG.userdataJdbcUrl()
    );
    AuthUserDao authUserDao = new AuthUserDaoJdbc();
    UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc();
    xaTransactionTemplate.execute(() -> {
      var savedUser = authUserDao.create(AuthUserEntity.fromJson(user));
      userdataUserDao.create(getEntityFromUser(savedUser));
      return null;
    });
  }

  @Test
  public void createUserJdbcWithoutTransactionTest() {
    AuthUserJson user = getNewUserJson();
    try (Connection authConnection = DataSources.dataSource(CFG.authJdbcUrl()).getConnection();
        Connection userdataConnection = DataSources.dataSource(CFG.userdataJdbcUrl()).getConnection()) {
      var savedUser = new AuthUserDaoJdbc().create(AuthUserEntity.fromJson(user));
      new UserdataUserDaoJdbc().create(getEntityFromUser(savedUser));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void createUserSpringJdbcWithTransactionTest() {
    AuthUserJson user = getNewUserJson();

    XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
        CFG.authJdbcUrl(),
        CFG.userdataJdbcUrl()
    );
    AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
    UserdataUserDao userdataUserDao = new UserdataUserDaoSpringJdbc();

    xaTransactionTemplate.execute(() -> {
      var savedUser = authUserDao.create(AuthUserEntity.fromJson(user));
      userdataUserDao.create(getEntityFromUser(savedUser));
      return null;
    });
  }

  @Test
  public void springJdbcWithoutTransactionTest() {
    AuthUserJson user = getNewUserJson();
    AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
    UserdataUserDao userdataUserDao = new UserdataUserDaoSpringJdbc();

    var savedUser = authUserDao.create(AuthUserEntity.fromJson(user));
    userdataUserDao.create(getEntityFromUser(savedUser));
  }


  private AuthUserJson getNewUserJson() {
    AuthUserJson newUser = new AuthUserJson();
    newUser.setUsername(RandomDataUtils.randomUsername());
    newUser.setPassword("12345");
    newUser.setEnabled(true);
    newUser.setAccountNonExpired(true);
    newUser.setAccountNonLocked(true);
    newUser.setCredentialsNonExpired(true);
    return newUser;
  }

  private UserEntity getEntityFromUser(AuthUserEntity authEntity) {
    UserEntity ue = new UserEntity();
    ue.setUsername(authEntity.getUsername());
    ue.setFullname(RandomDataUtils.randomFullName());
    ue.setFirstname(RandomDataUtils.randomFirstName());
    ue.setSurname(RandomDataUtils.randomLastName());
    ue.setCurrency(CurrencyValues.RUB);
    ue.setPhoto(new byte[0]);
    ue.setPhotoSmall(new byte[0]);
    return ue;
  }

}
