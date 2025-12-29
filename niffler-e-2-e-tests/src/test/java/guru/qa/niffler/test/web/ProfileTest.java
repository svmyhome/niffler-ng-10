package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class ProfileTest {

  private static final Config CFG = Config.getInstance();

  @Test
  public void openProfile() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login("duck", "12345")
        .openProfile()
        .checkProfileIsDisplayed();
  }

  @User
  @Test
  public void editNameUserProfile(UserJson user) {
    String name = "Ivan";
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .openProfile()
        .checkProfileIsDisplayed()
        .setName(name)
        .saveChanges()
        .goToMainPage()
        .openProfile()
        .checkName(name);
  }
}
