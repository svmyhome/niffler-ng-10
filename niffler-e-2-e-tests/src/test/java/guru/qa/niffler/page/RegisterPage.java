package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    public LoginPage loginPage = new LoginPage();

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


    public RegisterPage register(String username, String password, String passwordSubmit) {
        usernameInput.val(username);
        passwordInput.val(password);
        passwordSubmitInput.val(passwordSubmit);
        submitBtn.click();
        return new RegisterPage();
    }

    public RegisterPage registerWithStep(String username, String password, String passwordSubmit) {
        setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(passwordSubmit)
                .submitRegister();
        return new RegisterPage();
    }

    public RegisterPage setUsername(String username) {
        usernameInput.val(username);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.val(password);
        return this;
    }

    public RegisterPage setPasswordSubmit(String passwordSubmit) {
        passwordSubmitInput.val(passwordSubmit);
        return this;
    }

    public RegisterPage submitRegister() {
        submitBtn.click();
        return this;
    }

    public void checkRegister(String value) {
        checkRegisterResult.shouldHave(text(value));

    }

    public void checkFormError(String expectedErrorText) {
        formError.shouldHave(text(expectedErrorText));
    }

    public LoginPage switchToLoginPage() {
        switchToLogin.click();
        return new LoginPage();
    }


}
