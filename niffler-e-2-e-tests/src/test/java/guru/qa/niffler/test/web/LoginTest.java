package guru.qa.niffler.test.web;

import static utils.SelenideUtils.chromeConfig;
import static utils.SelenideUtils.firefoxConfig;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.converter.Browser;
import guru.qa.niffler.jupiter.converter.BrowserConverter;
import guru.qa.niffler.jupiter.extension.NonStaticBrowsersExtension;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.EnumSource;

@Epic("UI")
@Feature("User management")
@Story("Login")
public class LoginTest {

    private static final Config CFG = Config.getInstance();

    @RegisterExtension
    private final NonStaticBrowsersExtension nonStaticBrowsersExtension = new NonStaticBrowsersExtension();
    private final SelenideDriver driver = new SelenideDriver(chromeConfig);

    @User
    @Test
    @ResourceLock("browser")
    @DisplayName("User should be able to login with valid credentials")
    void shouldLoginUser(UserJson user) {
        nonStaticBrowsersExtension.drivers().add(driver);

        driver.open(CFG.frontUrl());
        new LoginPage(driver)
                .login(user.username(), user.testData().password())
                .mainPageShouldBeDisplayed();
    }

    @Test
    @DisplayName("User should NOT be able to login with incorrect password")
    void userStayOnLoginPageAfterLoginWithBadCredential() {
        driver.open(CFG.frontUrl());
        new LoginPage(driver)
                .loginWithBadCredential("duck", "1234")
                .checkFormError("Неверные учетные данные пользователя")
                .loginPageShouldBeDisplayed();
    }

    @ParameterizedTest
    @EnumSource(Browser.class)
    @DisplayName("User should NOT be able to login with incorrect password")
    void userStayOnLoginPageAfterLoginWithBadCredentialTestDisabled(
            @ConvertWith(BrowserConverter.class) SelenideDriver driver
    ) {
        nonStaticBrowsersExtension.drivers().add(driver);

        driver.open(CFG.frontUrl());
        new LoginPage(driver)
                .loginWithBadCredential("duck", "1234")
                .checkFormError("Неверные учетные данные пользователя")
                .loginPageShouldBeDisplayed();
    }

    @DisabledByIssue("1")
    @Test
    @DisplayName("User should NOT be able to login with incorrect password")
    void userStayOnLoginPageAfterLoginWithBadCredentialTestEnabled() {

        SelenideDriver firefox = new SelenideDriver(firefoxConfig);
        nonStaticBrowsersExtension.drivers().addAll(List.of(driver, firefox));


        driver.open(CFG.frontUrl());
        firefox.open(CFG.frontUrl());
        new LoginPage(driver)
                .loginWithBadCredential("duck", "1234")
                .checkFormError("Неверные учетные данные пользователя1")
                .loginPageShouldBeDisplayed();

        firefox.$(".logo-section__text").should(Condition.text("Niffler1"));
    }
}
