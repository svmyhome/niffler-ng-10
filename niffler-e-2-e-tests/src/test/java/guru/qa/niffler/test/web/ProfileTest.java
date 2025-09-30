package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;

public class ProfileTest {

    private static final Config CFG = Config.getInstance();


    @Category(
            name="qaz11111",
            username = "duck",
            archived = false
    )
    @Test
    public void openProfile(){
        Selenide.open(CFG.frontUrl(), ProfilePage.class)
                .loginPage
                .login("duck", "12345")
                .openMenu()
                .openProfile()
                .profilePage
                .checkProfile();
    }
}
