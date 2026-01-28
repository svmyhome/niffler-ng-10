package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.assertions.ImageAssertions;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Epic("UI")
@Feature("Navigation")
@Story("Profile page")
@ExtendWith(BrowserExtension.class)
public class ProfileTest {

  private static final Config CFG = Config.getInstance();

  @User
  @Test
  @DisplayName("Profile name should be editable")
  public void profileNameShouldBeEditable(UserJson user) {
    String name = "Ivan";
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .openProfile()
        .checkProfileIsDisplayed()
        .setName(name)
        .saveChanges()
        .checkSnackBarText("Profile successfully updated")
        .goToMainPage()
        .openProfile()
        .checkName(name);
  }

  @User
  @Test
  @DisplayName("User cannot upload invalid image as profile picture")
  public void shouldFailWhenUploadingInvalidPicture(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .openProfile()
        .checkProfileIsDisplayed()
        .uploadNewPictureInProfile("profile.jpg")
        .saveChanges()
        .checkSnackBarText("Error while updating profile: Failed to read request");
  }

  @User
  @ScreenShotTest(value = "img/avatar.jpg")
  @DisplayName("User can save valid picture in the profile")
  public void shouldSaveValidPictureInProfile(UserJson user, BufferedImage expected) throws IOException {
    ProfilePage getProfile = Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .openProfile()
        .checkProfileIsDisplayed()
        .uploadNewPictureInProfile("avatar.jpg")
        .saveChanges()
        .checkSnackBarText("Profile successfully updated");

    ImageAssertions.checkScreenshotMatches(expected,
        getProfile.captureAvatarScreenshot());
  }
}
