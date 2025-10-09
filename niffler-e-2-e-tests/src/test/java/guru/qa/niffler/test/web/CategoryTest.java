package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
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
}
