package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordBtn = $(".form__password-button");
    private final SelenideElement submitBtn = $("#login-button");
    private final SelenideElement submitPasskeyBtn = $("#login-with-passkey-button");
    private final SelenideElement registerBtn = $("#register-button");
    private final SelenideElement checkLogin = $(".header");
    private final SelenideElement formError = $(".form__error");

    public MainPage login(String username, String password) {
        usernameInput.val(username);
        passwordInput.val(password);
        submitBtn.click();
        return new MainPage();
    }

    public LoginPage loginWithBadCredential(String username, String password) {
        usernameInput.val(username);
        passwordInput.val(password);
        submitBtn.click();
        return this;
    }

    public RegisterPage switchToRegisterPage() {
        registerBtn.click();
        return new RegisterPage();
    }

    public MainPage switchToMainPage() {
        registerBtn.click();
        return new MainPage();
    }

    public LoginPage loginPageShouldBeDisplayed() {
        checkLogin.shouldHave(text("Log in"));
        return this;
    }

    public LoginPage checkFormError(String expectedErrorText) {
        formError.shouldHave(text(expectedErrorText));
        return this;
    }
}
