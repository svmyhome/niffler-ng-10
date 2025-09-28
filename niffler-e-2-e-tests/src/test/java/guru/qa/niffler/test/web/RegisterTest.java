package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.Test;

public class RegisterTest {
    private static final Config CFG = Config.getInstance();
    private static final String REGISTRATION_SUCCESS = "Congratulations! You've registered!";

    @Test
    void shouldRegisterNewUser() {

        Selenide.open(CFG.frontUrl(), RegisterPage.class)
                .loginPage
                .switchToRegisterPage()
                .register("horse41", "12345", "12345")
                .checkRegister(REGISTRATION_SUCCESS);
    }

    @Test
    void shouldRegisterNewUserWithStep() {
        Selenide.open(CFG.frontUrl(), RegisterPage.class)
                .loginPage
                .switchToRegisterPage()
                .setUsername("Moo01")
                .setPassword("1234")
                .setPasswordSubmit("1234")
                .submitRegister()
                .checkRegister(REGISTRATION_SUCCESS);
    }

    @Test
    void shouldRegisterNewUserWithStep2() {
        Selenide.open(CFG.frontUrl(), RegisterPage.class)
                .loginPage
                .switchToRegisterPage()
                .registerWithStep("horse01", "12345", "12345")
                .checkRegister(REGISTRATION_SUCCESS);
    }

    @Test
    void shouldRegisterNewUserSwitchToLogin() {
        Selenide.open(CFG.frontUrl(), RegisterPage.class)
                .loginPage
                .switchToRegisterPage()
                .setUsername("Moo1311")
                .setPassword("1234")
                .setPasswordSubmit("1234")
                .submitRegister()
                .switchToLoginPage()
                .loginPageShouldBeDisplayed();
    }

    @Test
    void shouldNotRegisterNewUserWithExistingUser() {
        Selenide.open(CFG.frontUrl(), RegisterPage.class)
                .loginPage
                .switchToRegisterPage()
                .register("duck", "12345", "12345")
                .checkFormError("Username `duck` already exists");
    }

    @Test
    void shouldNotRegisterNewUserWithPasswordLessThreeChar() {
        Selenide.open(CFG.frontUrl(), RegisterPage.class)
                .loginPage
                .switchToRegisterPage()
                .register("duck", "12", "12")
                .checkFormError("Allowed password length should be from 3 to 12 characters");
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        Selenide.open(CFG.frontUrl(), RegisterPage.class)
                .loginPage
                .switchToRegisterPage()
                .register("duck", "123", "1234")
                .checkFormError("Passwords should be equal");
    }

}
