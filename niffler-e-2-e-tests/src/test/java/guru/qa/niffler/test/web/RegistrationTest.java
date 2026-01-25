package guru.qa.niffler.test.web;

import static utils.SelenideUtils.chromeConfig;
import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.RandomDataUtils;

@Epic("UI")
@Feature("User management")
@Story("Registration")
@ExtendWith(BrowserExtension.class)
public class RegistrationTest {

    private static final Config CFG = Config.getInstance();
    private final SelenideDriver driver = new SelenideDriver(chromeConfig);
    private static final String REGISTRATION_SUCCESS = "Congratulations! You've registered!";

    @Test
    @DisplayName("New user registration should be successful")
    void shouldRegisterNewUser() {
        driver.open(CFG.frontUrl(), LoginPage.class)
                .switchToRegisterPage()
                .registerUser(RandomDataUtils.randomUsername(), "12345", "12345")
                .registrationShouldBeSuccessful(REGISTRATION_SUCCESS);
    }

    @Test
    @DisplayName("New user registration should be successful with steps")
    void shouldRegisterNewUserWithStep() {
        driver.open(CFG.frontUrl(), LoginPage.class)
                .switchToRegisterPage()
                .setUsername(RandomDataUtils.randomUsername())
                .setPassword("12345")
                .setPasswordSubmit("12345")
                .submitRegistration()
                .registrationShouldBeSuccessful(REGISTRATION_SUCCESS);
    }

    @Test
    @DisplayName("After successful registration user should be switch to login page")
    void shouldRegisterNewUserSwitchToLogin() {
        driver.open(CFG.frontUrl(), LoginPage.class)
                .switchToRegisterPage()
                .setUsername(RandomDataUtils.randomUsername())
                .setPassword("12345")
                .setPasswordSubmit("12345")
                .submitRegistration()
                .switchToLoginPage()
                .loginPageShouldBeDisplayed();
    }

    @Test
    @DisplayName("Registration should fail when username already exists")
    void shouldNotRegisterNewUserWithExistingUser() {
        driver.open(CFG.frontUrl(), LoginPage.class)
                .switchToRegisterPage()
                .registerUser("duck", "12345", "12345")
                .checkFormError("Username `duck` already exists");
    }

    @Test
    @DisplayName("Registration should fail when password is too short (less than 3 characters)")
    void shouldNotRegisterNewUserWithPasswordLessThreeChar() {
        driver.open(CFG.frontUrl(), LoginPage.class)
                .switchToRegisterPage()
                .registerUser("duck", "12", "12")
                .checkFormError("Allowed password length should be from 3 to 12 characters");
    }

    @Test
    @DisplayName("Registration should fail if password and confirm password do not match")
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        driver.open(CFG.frontUrl(), LoginPage.class)
                .switchToRegisterPage()
                .registerUser("duck", "123", "1234")
                .checkFormError("Passwords should be equal");
    }
}
