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
            username = "mouse",
            archived = true
    )
    @Test
    void archivedCategoryShouldNotBePresentedInActiveCategoryList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("mouse", "12345")
                .openProfile()
                .checkProfileIsDisplayed()
                .checkCategoryIsNotDisplayed(category.name());
    }

    @Category(username = "cat")
    @Test
    void activeCategoryShouldPresentInCategoryList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("cat", "12345")
                .openProfile()
                .checkProfileIsDisplayed()
                .checkCategoryIsDisplayed(category.name());
    }

    @Category(
            username = "dog",
            archived = true
    )
    @Test
    void archivedCategoryShouldBePresentedInArchivedList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("dog", "12345")
                .openProfile()
                .checkProfileIsDisplayed()
                .checkArchivedCategoryExists(category.name());
    }
}
