package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.CurrencyValues;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.service.SpendDnEntityClient;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class CategoryTest {

  private static final Config CFG = Config.getInstance();

  @User(
      username = "mouse",
      categories = {@Category(archived = true)}
  )
  @Test
  void archivedCategoryShouldNotBePresentedInActiveCategoryList(CategoryJson category) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login("mouse", "12345")
        .openProfile()
        .checkProfileIsDisplayed()
        .checkCategoryIsNotDisplayed(category.name());
  }

  @User(
      username = "cat",
      categories = {@Category()}
  )
  @Test
  void activeCategoryShouldPresentInCategoryList(CategoryJson category) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login("cat", "12345")
        .openProfile()
        .checkProfileIsDisplayed()
        .checkCategoryIsDisplayed(category.name());
  }

  @User(
      username = "dog",
      categories = {
          @Category(archived = true),
          @Category(archived = false)
      }
  )
  @Test
  void archivedCategoryShouldBePresentedInArchivedList(CategoryJson category) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login("dog", "12345")
        .openProfile()
        .checkProfileIsDisplayed()
        .checkArchivedCategoryExists(category.name());
  }

  @Test
  void findCategoryById() {
    SpendDnEntityClient spendDnEntityClient = new SpendDnEntityClient();
    Optional<CategoryEntity> categoryById = spendDnEntityClient.findCategoryById(
        UUID.fromString("0a091f06-b5bb-11f0-93fc-aa5c32f82d84"));
    System.out.println(categoryById);
  }

  @Test
  void findCategoryByUsernameAndSpendName() {
    SpendDnEntityClient spendDnEntityClient = new SpendDnEntityClient();
    Optional<CategoryEntity> categoryByUsernameAndSpendName = spendDnEntityClient.findCategoryByUsernameAndSpendName(
        "duck", "Машина");
    System.out.println(categoryByUsernameAndSpendName);
  }

  @Test
  void createCategoryTest() {
    SpendDnEntityClient spendDnEntityClient = new SpendDnEntityClient();

    CategoryEntity categoryEntity = new CategoryEntity();
    categoryEntity.setName("Test1qaa");
    categoryEntity.setUsername("duck");
    categoryEntity.setArchived(false);

    spendDnEntityClient.createCategory(categoryEntity);
  }

  @Test
  void removeCategoryTest() {
    SpendDnEntityClient spendDnEntityClient = new SpendDnEntityClient();

    CategoryEntity categoryEntity = new CategoryEntity();
    categoryEntity.setName("Test5qaa");
    categoryEntity.setUsername("duck");
    categoryEntity.setArchived(false);
    spendDnEntityClient.createCategory(categoryEntity);
    spendDnEntityClient.removeCategory(categoryEntity);
  }

}
