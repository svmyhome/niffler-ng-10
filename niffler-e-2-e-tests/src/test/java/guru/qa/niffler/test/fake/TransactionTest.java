package guru.qa.niffler.test.fake;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.auth.AuthUserDao;
import guru.qa.niffler.data.dao.impl.auth.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.auth.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.userdata.UserdataUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.userdata.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.userdata.UserdataUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.impl.auth.AuthUserRepositorySpringJdbc;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.jupiter.extension.SpendClientInjector;
import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import utils.RandomDataUtils;

@ExtendWith(SpendClientInjector.class)
public class TransactionTest {

  private static final Config CFG = Config.getInstance();
  private SpendClient spendClient;

  @Test
  void txTest() {
    SpendJson spendJson = spendClient.create(
        new SpendJson(
            null,
            new Date(),
            new CategoryJson(
                null,
                RandomDataUtils.randomCategoryName(),
                "duck",
                false
            ),
            CurrencyValues.RUB,
            1000.0,
            "cat-name-tx",
            "duck"
        )
    );
  }

  @Test
  public void successTransactionTest() {
    UserDbClient userDbClient = new UserDbClient();
    UserJson user = userDbClient.createUser(
        RandomDataUtils.randomUsername(),
        "12345"
    );
  }

  UserDbClient dbClient = new UserDbClient();

  @ParameterizedTest
  @ValueSource(
      strings = {"vova01111278"}
  )
  public void addFriendTest(String username) {
    UserJson user = dbClient.createUser(
        username,
        "12345"
    );
    List<UserJson> friends = dbClient.createFriends(user, 1);
  }

  @ParameterizedTest
  @ValueSource(
      strings = {"vova111111114"}
  )
  public void createSimpleUserTest(String username) {
    UserDbClient dbClient = new UserDbClient();
    UserJson user = dbClient.createUser(
        username,
        "12345"
    );
  }

  @Test
  public void findCategoryAndUserTest() {
    SpendClient spendClient = new SpendDbClient();
    Optional<CategoryJson> category = spendClient.findCategoryByUsernameAndSpendName("duck",
        "Машина");
    category.stream().forEach(System.out::println);
  }

  @Test
  public void createCategoryTest() {
    SpendClient spendClient = new SpendDbClient();
    CategoryJson categoryJson = spendClient.createCategory(
        new CategoryJson(
            null,
            "QAZ1qaz",
            "duck",
            false
        )
    );
  }

  @Test
  public void deleteCategoryTest() {
    SpendClient spendClient = new SpendDbClient();
    CategoryEntity category = new CategoryEntity();
    category.setId(UUID.fromString("2a6a67f0-b5bb-11f0-8a0e-aa5c32f82d84"));
    CategoryJson categoryJson = CategoryJson.fromEntity(category);
    spendClient.removeCategory(categoryJson);
  }

  @Test
  public void deleteSpendTest() {
    SpendClient spendClient = new SpendDbClient();
    SpendEntity spend = new SpendEntity();
    spend.setId(UUID.fromString("9d8cfc1e-b5bd-11f0-bfb0-aa5c32f82d84"));
    SpendJson spendJson = SpendJson.fromEntity(spend);
    spendClient.remove(spendJson);
  }

  @Test
  public void findSpendByIdTest() {
    SpendClient spendClient = new SpendDbClient();
    Optional<SpendJson> spend = spendClient.findById(
        UUID.fromString("1328a312-b5bc-11f0-a017-aa5c32f82d84"));
  }

  @Test
  public void updateSpendByIdTest() {
    SpendClient spendClient = new SpendDbClient();
    SpendJson spend = spendClient.update(
        new SpendJson(UUID.fromString("888ca6da-b6e3-11f0-8e67-ea06c42c5790"),
            new Date(new Date().getTime()),
            new CategoryJson(UUID.fromString("888ba94c-b6e3-11f0-8e67-ea06c42c5790"), null, null,
                false),
            CurrencyValues.RUB,
            180.0,
            "1111212121211",
            "duck"
        )
    );
  }

  @Test
  public void updateCategoryByIdTest() {
    SpendClient spendClient = new SpendDbClient();
    CategoryJson category = spendClient.updateCategory(
        new CategoryJson(
            UUID.fromString("9d8c393c-b5bd-11f0-bfb0-aa5c32f82d84"),
            "1q1a1a1", "duck", false)
    );
  }

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
        Connection userdataConnection = DataSources.dataSource(CFG.userdataJdbcUrl())
            .getConnection()) {
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

  @Test
  void updateAuthUserTest() {
    UserDbClient dbClient = new UserDbClient();
    String username = RandomDataUtils.randomUsername();
    UserJson user = dbClient.createUser(
        username,
        "12345"
    );
    System.out.println(user.username());
    Optional<AuthUserEntity> authUser = dbClient.findUserByUserName(user.username());
    System.out.println(authUser);

    AuthUserEntity authUserEntity = null;
    if (authUser.isPresent()) {
      authUser.ifPresent(u -> {
        u.setEnabled(false);
      });
      authUserEntity = authUser.get();
    }
    dbClient.update(authUserEntity);
  }

  @Test
  void removeUserTest() {
    UserDbClient dbClient = new UserDbClient();
    String username = RandomDataUtils.randomUsername();
    UserJson user = dbClient.createUser(username, "12345");
    System.out.println(user.username());
    Optional<UserEntity> existingUser = dbClient.findUserById(user.id());
    if (existingUser.isPresent()) {
      dbClient.remove(existingUser.orElse(null));
    }
  }

  @Test
  void updateAuthUserJdbcTest() {
    UserDbClient dbClient = new UserDbClient();
    Optional<AuthUserEntity> ae = dbClient.findAuthUserById(
        UUID.fromString("599dfd2d-691b-447e-af58-6e7fab52b368"));
    ae.ifPresent(u -> {
      u.setEnabled(true);
    });
    AuthUserRepositorySpringJdbc authUserRepositoryJdbc = new AuthUserRepositorySpringJdbc();
    authUserRepositoryJdbc.update(ae.orElse(null));
  }

  @Test
  public void findAllTest() {
    UserDbClient userdbClient = new UserDbClient();
    List<AuthUserJson> authorityJson = userdbClient.findAll();
  }

  @Test
  public void deleteTest() {
    UserDbClient dbClient = new UserDbClient();
    Optional<AuthUserEntity> user = dbClient.findAuthUserById(
        UUID.fromString("b624f757-a7ed-4308-b213-3e3e07d884e1"));
    if (user.isPresent()) {
      AuthUserEntity authUser = user.get();
      dbClient.delete(authUser);
    }
  }
}
