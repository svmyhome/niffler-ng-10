package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.RegistrationPage;
import org.junit.jupiter.api.Test;

public class RegistrationTest {
    private static final Config CFG = Config.getInstance();
    private static final String REGISTRATION_SUCCESS = "Congratulations! You've registered!";

    @Test
    void shouldRegisterNewUser() {

        Selenide.open(CFG.frontUrl(), RegistrationPage.class)
                .loginPage
                .switchToRegisterPage()
                .registerUser("horse421145", "12345", "12345")
                .registrationShouldBeSuccessful(REGISTRATION_SUCCESS);
    }

    @Test
    void shouldRegisterNewUserWithStep() {
        Selenide.open(CFG.frontUrl(), RegistrationPage.class)
                .loginPage
                .switchToRegisterPage()
                .setUsername("Moo015445")
                .setPassword("1234")
                .setPasswordSubmit("1234")
                .submitRegistration()
                .registrationShouldBeSuccessful(REGISTRATION_SUCCESS);
    }

    @Test
    void shouldRegisterNewUserWithStep2() {
        Selenide.open(CFG.frontUrl(), RegistrationPage.class)
                .loginPage
                .switchToRegisterPage()
                .registerUser("horse0441", "12345", "12345")
                .registrationShouldBeSuccessful(REGISTRATION_SUCCESS);
    }

    @Test
    void shouldRegisterNewUserSwitchToLogin() {
        Selenide.open(CFG.frontUrl(), RegistrationPage.class)
                .loginPage
                .switchToRegisterPage()
                .setUsername("Moo13117")
                .setPassword("1234")
                .setPasswordSubmit("1234")
                .submitRegistration()
                .switchToLoginPage()
                .loginPageShouldBeDisplayed();
    }

    @Test
    void shouldNotRegisterNewUserWithExistingUser() {
        Selenide.open(CFG.frontUrl(), RegistrationPage.class)
                .loginPage
                .switchToRegisterPage()
                .registerUser("duck", "12345", "12345")
                .checkFormError("Username `duck` already exists");
    }

    @Test
    void shouldNotRegisterNewUserWithPasswordLessThreeChar() {
        Selenide.open(CFG.frontUrl(), RegistrationPage.class)
                .loginPage
                .switchToRegisterPage()
                .registerUser("duck", "12", "12")
                .checkFormError("Allowed password length should be from 3 to 12 characters");
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        Selenide.open(CFG.frontUrl(), RegistrationPage.class)
                .loginPage
                .switchToRegisterPage()
                .registerUser("duck", "123", "1234")
                .checkFormError("Passwords should be equal");
    }

}
