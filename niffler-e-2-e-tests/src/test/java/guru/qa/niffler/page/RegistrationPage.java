package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

public class RegistrationPage {

  private final SelenideElement usernameInput = $("#username");
  private final SelenideElement passwordInput = $("#password");
  private final SelenideElement passwordBtn = $("#passwordBtn");
  private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
  private final SelenideElement passwordSubmitBtn = $("#passwordSubmitBtn");
  private final SelenideElement submitBtn = $("#register-button");
  private final SelenideElement logIn = $("[href='/login']");
  private final SelenideElement checkRegisterResult = $(".form__paragraph_success");
  private final SelenideElement switchToLogin = $(".form_sign-in");
  private final SelenideElement formError = $(".form__error");

  @Step("Register user with credentials")
  public RegistrationPage registerUser(String username, String password, String passwordSubmit) {
    setUsername(username)
        .setPassword(password)
        .setPasswordSubmit(passwordSubmit)
        .submitRegistration();
    return new RegistrationPage();
  }

  @Step("Set username: '{username}'")
  public RegistrationPage setUsername(String username) {
    usernameInput.val(username);
    return this;
  }

  @Step("Set password: '{password}'")
  public RegistrationPage setPassword(String password) {
    passwordInput.val(password);
    return this;
  }

  @Step("Set password submit: '{passwordSubmit}'")
  public RegistrationPage setPasswordSubmit(String passwordSubmit) {
    passwordSubmitInput.val(passwordSubmit);
    return this;
  }

  @Step("Submit registration")
  public RegistrationPage submitRegistration() {
    submitBtn.click();
    return this;
  }

  @Step("Successful registration")
  public void registrationShouldBeSuccessful(String value) {
    checkRegisterResult.shouldHave(text(value));
  }

  @Step("Verify error '{expectedErrorText}' visible")
  public void checkFormError(String expectedErrorText) {
    formError.shouldHave(text(expectedErrorText));
  }

  @Step("Switch to login page")
  public LoginPage switchToLoginPage() {
    switchToLogin.click();
    return new LoginPage();
  }
}
