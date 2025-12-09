package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.spend.CategoryDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.impl.spend.SpendRepositoryJdbc;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.spend.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.service.SpendDbClient;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class SpendingTest {

  private static final Config CFG = Config.getInstance();

  @User(
      username = "duck",
      spendings = {@Spending(
          category = "Машина",
          amount = 89900,
          currency = CurrencyValues.RUB,
          description = "Обучение Niffler 2.0 юбилейный поток!"
      ),
          @Spending(
              category = "Пиво",
              amount = 100,
              currency = CurrencyValues.RUB,
              description = "Обучение Niffler 2.0 юбилейный поток!"
          )}
  )
  @Test
  void spendingDescriptionShouldBeEditedByTableAction(SpendJson spending) {
    final String newDescription = "Обучение Niffler Next Generation 10";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login("duck", "12345")
        .editSpending(spending.description())
        .setNewSpendingDescription(newDescription)
        .checkThatTableContains(newDescription);
  }

  @Test
  void findSpendingByIdTest() {
    SpendDbClient spendDbClient = new SpendDbClient();
    Optional<SpendEntity> byId = spendDbClient.findById(
        UUID.fromString("1328a312-b5bc-11f0-a017-aa5c32f82d84"));
    System.out.println(byId);
  }

  @Test
  void findSpendByNameAndDescription() {
    SpendDbClient spendDbClient = new SpendDbClient();
    Optional<SpendEntity> duck = spendDbClient.findByUsernameAndSpendDescription("duck",
        "11111");
    System.out.println(duck);
  }

  @Test
  void createSpendingTest() {
    SpendDbClient spendDbClient = new SpendDbClient();
    CategoryEntity categoryEntity = new CategoryEntity();
    categoryEntity.setName("1qaa");
    categoryEntity.setUsername("duck");
    categoryEntity.setArchived(false);
    SpendEntity entity = new SpendEntity();
    entity.setUsername("duck");
    entity.setCurrency(CurrencyValues.RUB);
    entity.setSpendDate(new Date());
    entity.setAmount(123.0);
    entity.setDescription("qazsdfg");
    entity.setCategory(categoryEntity);
    spendDbClient.create(entity);
  }

  @Test
  void deleteSpendingTest() {
    SpendDbClient spendDbClient = new SpendDbClient();
    CategoryEntity categoryEntity = new CategoryEntity();
    categoryEntity.setName("555qaa");
    categoryEntity.setUsername("duck");
    categoryEntity.setArchived(false);

    SpendEntity entity = new SpendEntity();
    entity.setUsername("duck");
    entity.setCurrency(CurrencyValues.RUB);
    entity.setSpendDate(new Date());
    entity.setAmount(123.0);
    entity.setDescription("55qazsdfg");
    entity.setCategory(categoryEntity);

    spendDbClient.create(entity);
    spendDbClient.remove(entity);
  }

  @Test
  void updateSpendingTest() {
    SpendDbClient spendDbClient = new SpendDbClient();

    CategoryEntity categoryEntity = new CategoryEntity();
    categoryEntity.setName("777qaa");
    categoryEntity.setUsername("duck");
    categoryEntity.setArchived(false);

    SpendEntity entity = new SpendEntity();
    entity.setUsername("duck");
    entity.setCurrency(CurrencyValues.RUB);
    entity.setSpendDate(new Date());
    entity.setAmount(123.0);
    entity.setDescription("777qazsdfg");
    entity.setCategory(categoryEntity);

    spendDbClient.create(entity);
    entity.setAmount(222.0);
    spendDbClient.update(entity);
  }

  @Test
  void createSpendTest() {
    SpendRepositoryJdbc spendRepositoryJdbc = new SpendRepositoryJdbc();
    CategoryDaoJdbc categoryDaoJdbc = new CategoryDaoJdbc();
    CategoryEntity categoryEntity = new CategoryEntity();
    categoryEntity.setName("1234qaa");
    categoryEntity.setUsername("duck");
    categoryEntity.setArchived(false);
    CategoryEntity savedCategory = categoryDaoJdbc.create(categoryEntity);

    SpendEntity entity = new SpendEntity();
    entity.setUsername("duck");
    entity.setCurrency(CurrencyValues.RUB);
    entity.setSpendDate(new Date());
    entity.setAmount(123.0);
    entity.setDescription("1234qazsdfg");
    entity.setCategory(savedCategory);

    spendRepositoryJdbc.create(entity);

  }

  @Test
  void removeSpendTest() {
    SpendRepositoryJdbc spendRepositoryJdbc = new SpendRepositoryJdbc();
    CategoryDaoJdbc categoryDaoJdbc = new CategoryDaoJdbc();
    CategoryEntity categoryEntity = new CategoryEntity();
    categoryEntity.setName("7819");
    categoryEntity.setUsername("duck");
    categoryEntity.setArchived(false);
    CategoryEntity savedCategory = categoryDaoJdbc.create(categoryEntity);

    SpendEntity entity = new SpendEntity();
    entity.setUsername("duck");
    entity.setCurrency(CurrencyValues.RUB);
    entity.setSpendDate(new Date());
    entity.setAmount(123.0);
    entity.setDescription("7819qazsdfg");
    entity.setCategory(savedCategory);

    SpendEntity savedSpend = spendRepositoryJdbc.create(entity);
    spendRepositoryJdbc.remove(savedSpend);
  }
}

