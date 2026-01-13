package guru.qa.niffler.test.web;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.openqa.selenium.remote.Browser;

@Epic("UI")
@Feature("User management")
@Story("Login")
@WebTest
public class LoginTest {

  private static final Config CFG = Config.getInstance();

  @User
  @Test
  @ResourceLock("browser")
  @DisplayName("User should be able to login with valid credentials")
  void shouldLoginUser(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .mainPageShouldBeDisplayed();
  }

  @User
  @Test
  @ResourceLock("browser")
  @DisplayName("User should be able to sign out after login")
  void userShouldBeAbleToSignOut(UserJson user) {
    Configuration.browser = Browser.FIREFOX.browserName();
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .mainPageShouldBeDisplayed()
        .signOut()
        .loginPageShouldBeDisplayed();
  }


  @Test
  @DisplayName("User should NOT be able to login with incorrect password")
  void userStayOnLoginPageAfterLoginWithBadCredential() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .loginWithBadCredential("duck", "1234")
        .checkFormError("Неверные учетные данные пользователя")
        .loginPageShouldBeDisplayed();
  }

  @DisabledByIssue("5")
  @Test
  @DisplayName("User should NOT be able to login with incorrect password")
  void userStayOnLoginPageAfterLoginWithBadCredentialTestDisabled() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .loginWithBadCredential("duck", "1234")
        .checkFormError("Неверные учетные данные пользователя")
        .loginPageShouldBeDisplayed();
  }

  @DisabledByIssue("1")
  @Test
  @DisplayName("User should NOT be able to login with incorrect password")
  void userStayOnLoginPageAfterLoginWithBadCredentialTestEnabled() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .loginWithBadCredential("duck", "1234")
        .checkFormError("Неверные учетные данные пользователя")
        .loginPageShouldBeDisplayed();
  }
}
