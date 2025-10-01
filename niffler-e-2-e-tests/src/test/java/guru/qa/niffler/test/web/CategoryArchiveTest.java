package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class CategoryArchiveTest {
    private static final Config CFG = Config.getInstance();

    @Category(
            username = "duck",
            archived = true
    )
    @Test
    void archivedCategoryShouldNotBePresentedInActiveCategoryList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345")
                .openMenu()
                .openProfile()
                .profilePage
                .checkProfile()
                .checkCategoryIsNotDisplayed(category.name());
    }

    @Category(username = "duck")
    @Test
    void activeCategoryShouldPresentInCategoryList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345")
                .openMenu()
                .openProfile()
                .profilePage
                .checkProfile()
                .checkCategoryIsDisplayed(category.name());
    }

    @Category(
            username = "duck",
            archived = true
    )
    @Test
    void archivedCategoryShouldBePresentedInArchivedList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345")
                .openMenu()
                .openProfile()
                .profilePage
                .checkProfile()
                .checkArchivedCategoryExists(category.name());
    }
}
