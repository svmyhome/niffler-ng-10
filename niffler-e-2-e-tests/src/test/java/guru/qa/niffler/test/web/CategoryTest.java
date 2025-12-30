package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.service.SpendDbClient;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class CategoryTest {

  private static final Config CFG = Config.getInstance();

  @User(
      categories = {@Category(archived = true)}
  )
  @Test
  void archivedCategoryShouldNotBePresentedInActiveCategoryList(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .openProfile()
        .checkProfileIsDisplayed()
        .checkCategoryIsNotDisplayed(user.testData().categories().getFirst().name());
  }

  @User(
      categories = {@Category()}
  )
  @Test
  void activeCategoryShouldPresentInCategoryList(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .openProfile()
        .checkProfileIsDisplayed()
        .checkCategoryIsDisplayed(user.testData().categories().getFirst().name());
  }

  @User(
      categories = {
          @Category(archived = true),
          @Category(archived = false)
      }
  )
  @Test
  void archivedCategoryShouldBePresentedInArchivedList(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .openProfile()
        .checkProfileIsDisplayed()
        .checkArchivedCategoryExists(user.testData().categories().getFirst().name());
  }

  @Test
  void findCategoryById() {
    SpendDbClient spendDbClient = new SpendDbClient();
    Optional<CategoryJson> categoryById = spendDbClient.findCategoryById(
        UUID.fromString("0a091f06-b5bb-11f0-93fc-aa5c32f82d84"));
    System.out.println(categoryById);
  }

  @Test
  void findCategoryByUsernameAndSpendName() {
    SpendDbClient spendDbClient = new SpendDbClient();
    Optional<CategoryJson> categoryByUsernameAndSpendName = spendDbClient.findCategoryByUsernameAndSpendName(
        "duck", "Машина");
    System.out.println(categoryByUsernameAndSpendName);
  }

  @Test
  void createCategoryTest() {
    SpendDbClient spendDbClient = new SpendDbClient();

    CategoryEntity categoryEntity = new CategoryEntity();
    categoryEntity.setName("Test1qaa");
    categoryEntity.setUsername("duck");
    categoryEntity.setArchived(false);

    spendDbClient.createCategory(CategoryJson.fromEntity(categoryEntity));
  }

  @Test
  void removeCategoryTest() {
    SpendDbClient spendDbClient = new SpendDbClient();

    CategoryEntity categoryEntity = new CategoryEntity();
    categoryEntity.setName("Test5qaa");
    categoryEntity.setUsername("duck");
    categoryEntity.setArchived(false);
    spendDbClient.createCategory(CategoryJson.fromEntity(categoryEntity));
    spendDbClient.removeCategory(CategoryJson.fromEntity(categoryEntity));
  }

}
