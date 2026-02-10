package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.ProfilePage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Epic("UI")
@Feature("Categories and Spendings")
@Story("Category Management")
@ExtendWith(BrowserExtension.class)
public class CategoryWebTest {

  private static final Config CFG = Config.getInstance();

  @User(
      categories = {@Category(archived = true)}
  )
  @ApiLogin
  @Test
  @DisplayName("Archived category should not appear in the list of active categories")
  void archivedCategoryShouldNotBePresentedInActiveCategoryList(UserJson user) {
    Selenide.open(ProfilePage.URL, ProfilePage.class)
        .checkProfileIsDisplayed()
        .checkCategoryIsNotDisplayed(user.testData().categories().getFirst().name());
  }

  @User(
      categories = {@Category()}
  )
  @ApiLogin
  @Test
  @DisplayName("Active category should appear in the category list")
  void activeCategoryShouldPresentInCategoryList(UserJson user) {
    Selenide.open(ProfilePage.URL, ProfilePage.class)
        .checkProfileIsDisplayed()
        .checkCategoryIsDisplayed(user.testData().categories().getFirst().name());
  }

  @User(
      categories = {
          @Category(archived = true),
          @Category(archived = false)
      }
  )
  @ApiLogin
  @Test
  @DisplayName("Archived category should be visible in archived categories list")
  void archivedCategoryShouldBePresentedInArchivedList(UserJson user) {
    Selenide.open(ProfilePage.URL, ProfilePage.class)
        .checkProfileIsDisplayed()
        .checkArchivedCategoryExists(user.testData().categories().getFirst().name());
  }
}
