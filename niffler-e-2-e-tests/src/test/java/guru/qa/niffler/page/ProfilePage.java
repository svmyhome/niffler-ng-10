package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import io.qameta.allure.Step;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;

@ParametersAreNonnullByDefault
public class ProfilePage extends BasePage<ProfilePage> {

    private final SelenideElement showArchived,
            setName,
            saveChanges,
            uploadNewImage,
            avatarImage;
    private final ElementsCollection
            categories,
            categoriesArchived,
            headersH2;

    private final Header header;

    public ProfilePage(SelenideDriver driver) {
        super(driver);
        this.showArchived = driver.$(".MuiSwitch-input");
        this.setName = driver.$("#name");
        this.saveChanges = driver.$("[type='submit']");
        this.uploadNewImage = driver.$("#image__input");
        this.avatarImage = driver.$(".MuiContainer-root .MuiAvatar-img");
        this.categories = driver.$$(".MuiChip-filledPrimary");
        this.categoriesArchived = driver.$$(".MuiChip-filled.MuiChip-colorDefault");
        this.headersH2 = driver.$$("h2");
        this.header = new Header(driver);
    }

    @Step("Successfully opened profile")
    public @Nonnull ProfilePage checkProfileIsDisplayed() {
        headersH2.find(text("Profile")).should(visible);
        return this;
    }

    @Step("Check category: '{category}'")
    public @Nonnull boolean checkCategoryIsNotDisplayed(String category) {
        categories.find(text(category)).shouldNot(visible);
        return true;
    }

    @Step("Check category: '{category}'")
    public @Nonnull ProfilePage checkCategoryIsDisplayed(String category) {
        categories.find(text(category)).shouldBe(visible);
        return this;
    }

    @Step("Check archived category: '{category}'")
    public @Nonnull ProfilePage checkArchivedCategoryExists(String category) {
        showArchived.click();
        categoriesArchived.find(text(category)).shouldBe(visible);
        return this;
    }

    @Step("Save changes")
    public @Nonnull ProfilePage saveChanges() {
        saveChanges.click();
        return this;
    }

    @Step("Set name")
    public @Nonnull ProfilePage setName(String name) {
        setName.shouldBe(visible).setValue(name);
        return this;
    }

    @Step("Check name")
    public @Nonnull ProfilePage checkName(String name) {
        setName.shouldBe(visible).shouldHave(value(name));
        return this;
    }

    public @Nonnull MainPage goToMainPage() {
        header.toMainPage();
        return new MainPage(driver);
    }

    @Step("Upload new picture in profile")
    public @Nonnull ProfilePage uploadNewPictureInProfile(String fileName) {
        uploadNewImage.uploadFromClasspath("img/" + fileName);
        return this;
    }

    @Step("Take screenshot of user avatar")
    public @Nonnull BufferedImage captureAvatarScreenshot() {
        try {
            return ImageIO.read(Objects.requireNonNull(avatarImage.screenshot()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
