package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertFalse;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import io.qameta.allure.Step;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import utils.ScreenDiffResult;

@ParametersAreNonnullByDefault
public class ProfilePage extends BasePage<ProfilePage> {

  private final SelenideElement showArchived = $(".MuiSwitch-input"),
      setName = $("#name"),
      saveChanges = $("[type='submit']"),
      uploadNewImage = $("#image__input"),
      avatarImage = $(".MuiContainer-root .MuiAvatar-img");
  private final ElementsCollection
      categories = $$(".MuiChip-filledPrimary"),
      categoriesArchived = $$(".MuiChip-filled.MuiChip-colorDefault"),
      headersH2 = $$("h2");

  private final Header header = new Header();

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
    return new MainPage();
  }


  @Step("Upload new picture in profile")
  public @Nonnull ProfilePage uploadNewPictureInProfile(String fileName) {
    uploadNewImage.uploadFromClasspath("img/" + fileName);
    return this;
  }

  @Step("Check profile picture is correct")
  public @Nonnull ProfilePage checkProfilePictureIsCorrect(BufferedImage expected)
      throws IOException {
    BufferedImage actual = ImageIO.read(avatarImage.screenshot());
    assertFalse(new ScreenDiffResult(
        expected,
        actual
    ));
    return this;
  }

}
