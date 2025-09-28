package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class LoginTest {
    private static final Config CFG = Config.getInstance();

    @Test
    void shouldLoginUser() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345")
                .mainPageShouldBeDisplayed();
    }

    @Test
    void userStayOnLoginPageAfterLoginWithBadCredential() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .loginWithBadCredential("duck", "1234")
                .checkFormError("Неверные учетные данные пользователя")
                .loginPageShouldBeDisplayed();
    }

}
