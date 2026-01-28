package guru.qa.niffler.test.web;

import static utils.SelenideUtils.chromeConfig;
import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.converter.Browser;
import guru.qa.niffler.jupiter.converter.BrowserConverter;
import guru.qa.niffler.jupiter.extension.NonStaticBrowsersExtension;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.parallel.Isolated;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.EnumSource;


@Isolated
@Epic("UI")
@Feature("Navigation")
@Story("Main page")
@ExtendWith(NonStaticBrowsersExtension.class)
public class MainTest {

    private static final Config CFG = Config.getInstance();

    @RegisterExtension
    private final NonStaticBrowsersExtension nonStaticBrowsersExtension = new NonStaticBrowsersExtension();
    private final SelenideDriver driver = new SelenideDriver(chromeConfig);


    @User
    @ParameterizedTest
    @EnumSource(Browser.class)
    @DisplayName("User should be able to navigate from profile back to main page")
    public void userShouldNavigateFromProfileToMainPage(@ConvertWith(BrowserConverter.class) SelenideDriver driver, UserJson user) {
        nonStaticBrowsersExtension.drivers().add(driver);

        driver.open(CFG.frontUrl());
        new LoginPage(driver)
                .login(user.username(), user.testData().password())
                .openProfile()
                .checkProfileIsDisplayed()
                .goToMainPage()
                .mainPageShouldBeDisplayed();
    }
}
