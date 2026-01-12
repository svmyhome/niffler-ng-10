package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class RegistrationPage extends BasePage<RegistrationPage> {

  private final SelenideElement usernameInput = $("#username"),
      passwordInput = $("#password"),
      passwordSubmitInput = $("#passwordSubmit"),
      submitBtn = $("#register-button"),
      checkRegisterResult = $(".form__paragraph_success"),
      switchToLogin = $(".form_sign-in"),
      formError = $(".form__error");

  @Step("Register user with credentials")
  public @Nonnull RegistrationPage registerUser(String username, String password,
      String passwordSubmit) {
    setUsername(username)
        .setPassword(password)
        .setPasswordSubmit(passwordSubmit)
        .submitRegistration();
    return new RegistrationPage();
  }

  @Step("Set username: '{username}'")
  public @Nonnull RegistrationPage setUsername(String username) {
    usernameInput.val(username);
    return this;
  }

  @Step("Set password: '{password}'")
  public @Nonnull RegistrationPage setPassword(String password) {
    passwordInput.val(password);
    return this;
  }

  @Step("Set password submit: '{passwordSubmit}'")
  public @Nonnull RegistrationPage setPasswordSubmit(String passwordSubmit) {
    passwordSubmitInput.val(passwordSubmit);
    return this;
  }

  @Step("Submit registration")
  public @Nonnull RegistrationPage submitRegistration() {
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
  public @Nonnull LoginPage switchToLoginPage() {
    switchToLogin.click();
    return new LoginPage();
  }
}
