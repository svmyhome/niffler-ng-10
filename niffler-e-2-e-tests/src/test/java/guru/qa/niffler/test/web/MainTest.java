package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.component.Header;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class MainTest {

  private static final Config CFG = Config.getInstance();
  Header header = new Header();
  MainPage mainPage = new MainPage();

  @Test
  public void openMainPage() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login("duck", "12345")
        .openProfile()
        .checkProfileIsDisplayed();

    header.toMainPage();
    mainPage.mainPageShouldBeDisplayed();
  }
}
