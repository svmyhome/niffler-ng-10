package guru.qa.niffler.test.fake;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.service.AuthApiClient;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

public class OAuthTest {

    private final AuthApiClient authApiClient = new AuthApiClient();
    public static final Config CFG = Config.getInstance();

    @Test
    @User
    public void oauthRandomUserTest(UserJson user) {
        String token = authApiClient.login(user.username(), user.testData().password());
        assertNotNull(token);
        System.out.println("Final Token: " + token);
    }


    @Test
    public void oauthUserTest() {

        String token = authApiClient.login("duck", "12345");
        assertNotNull(token);
        System.out.println("Final Token: " + token);
        Selenide.open(CFG.frontUrl());
        Selenide.localStorage().setItem("id_token", token);
        WebDriverRunner.getWebDriver().manage().addCookie(
                new Cookie(
                        "JSESSIONID",
                        ThreadSafeCookieStore.INSTANCE.cookieValue("JSESSIONID")
                )
        );
        Selenide.open(MainPage.URL, MainPage.class).mainPageShouldBeDisplayed();
    }

    @Test
    @ApiLogin(username = "mouse", password="12345")
    public void fakeUserApiLoginExtensionTest(@Token String token, UserJson user) {
        System.out.println(user);
        assertNotNull(token);
        System.out.println(token);
    }

    @Test
    @User(friends = 1)
    @ApiLogin()
    public void userApiLoginExtensionTest(@Token String token, UserJson user) {
        System.out.println(user);
        assertNotNull(token);
        System.out.println(token);
    }
}
