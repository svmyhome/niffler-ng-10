package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Epic("UI")
@Feature("Navigation")
@Story("Profile page")
@ExtendWith(BrowserExtension.class)
public class ProfileTest {

  private static final Config CFG = Config.getInstance();

  @User
  @Test
  @DisplayName("Profile name should be editable")
  public void profileNameShouldBeEditable(UserJson user) {
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
