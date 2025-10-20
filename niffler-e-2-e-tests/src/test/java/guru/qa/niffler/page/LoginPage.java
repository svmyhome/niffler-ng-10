package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

public class LoginPage {

  private final SelenideElement usernameInput = $("#username");
  private final SelenideElement passwordInput = $("#password");
  private final SelenideElement passwordBtn = $(".form__password-button");
  private final SelenideElement submitBtn = $("#login-button");
  private final SelenideElement submitPasskeyBtn = $("#login-with-passkey-button");
  private final SelenideElement registerBtn = $("#register-button");
  private final SelenideElement checkLogin = $(".header");
  private final SelenideElement formError = $(".form__error");

  @Step("Login user with credentials")
  public MainPage login(String username, String password) {
    usernameInput.val(username);
    passwordInput.val(password);
    submitBtn.click();
    return new MainPage();
  }

  @Step("Login with bad credential")
  public LoginPage loginWithBadCredential(String username, String password) {
    usernameInput.val(username);
    passwordInput.val(password);
    submitBtn.click();
    return this;
  }

  @Step("Switch to register page")
  public RegistrationPage switchToRegisterPage() {
    registerBtn.click();
    return new RegistrationPage();
  }

  @Step("Successfully opened profile")
  public LoginPage loginPageShouldBeDisplayed() {
    checkLogin.shouldHave(text("Log in"));
    return this;
  }

  @Step("Verify error '{expectedErrorText}' visible")
  public LoginPage checkFormError(String expectedErrorText) {
    formError.shouldHave(text(expectedErrorText));
    return this;
  }
}
