package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.component.Header;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@Epic("UI")
@Feature("Navigation")
@Story("Main page")
@ExtendWith(BrowserExtension.class)
public class MainTest {

  private static final Config CFG = Config.getInstance();
  Header header = new Header();
  MainPage mainPage = new MainPage();

  @User
  @Test
  @DisplayName("User should be able to navigate from profile back to main page")
  public void userShouldNavigateFromProfileToMainPage(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .openProfile()
        .checkProfileIsDisplayed();

    header.toMainPage();
    mainPage.mainPageShouldBeDisplayed();
  }
}
