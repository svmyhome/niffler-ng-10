package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LoginPage extends BasePage<LoginPage> {

    private final SelenideElement usernameInput;
    private final SelenideElement passwordInput;
    private final SelenideElement submitBtn;
    private final SelenideElement registerBtn;
    private final SelenideElement checkLogin;
    private final SelenideElement formError;

    public LoginPage(SelenideDriver driver) {
        super(driver);
        this.usernameInput = driver.$("#username");
        this.passwordInput = driver.$("#password");
        this.submitBtn = driver.$("#login-button");
        this.registerBtn = driver.$("#register-button");
        this.checkLogin = driver.$(".header");
        this.formError = driver.$(".form__error");
    }

    public LoginPage() {
        this.usernameInput = Selenide.$("#username");
        this.passwordInput = Selenide.$("#password");
        this.submitBtn = Selenide.$("#login-button");
        this.registerBtn = Selenide.$("#register-button");
        this.checkLogin = Selenide.$(".header");
        this.formError = Selenide.$(".form__error");
    }

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
