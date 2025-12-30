package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LoginPage {

  private final SelenideElement usernameInput = $("#username"),
      passwordInput = $("#password"),
      submitBtn = $("#login-button"),
      registerBtn = $("#register-button"),
      checkLogin = $(".header"),
      formError = $(".form__error");

  @Step("Login user with credentials")
  public @Nonnull MainPage login(String username, String password) {
    usernameInput.val(username);
    passwordInput.val(password);
    submitBtn.click();
    return new MainPage();
  }

  @Step("Login with bad credential")
  public @Nonnull LoginPage loginWithBadCredential(String username, String password) {
    usernameInput.val(username);
    passwordInput.val(password);
    submitBtn.click();
    return this;
  }

  @Step("Switch to register page")
  public @Nonnull RegistrationPage switchToRegisterPage() {
    registerBtn.click();
    return new RegistrationPage();
  }

  @Step("Successfully opened profile")
  public @Nonnull LoginPage loginPageShouldBeDisplayed() {
    checkLogin.shouldHave(text("Log in"));
    return this;
  }

  @Step("Verify error '{expectedErrorText}' visible")
  public @Nonnull LoginPage checkFormError(String expectedErrorText) {
    formError.shouldHave(text(expectedErrorText));
    return this;
  }
}
