package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class CategoryWebTest {

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
}
