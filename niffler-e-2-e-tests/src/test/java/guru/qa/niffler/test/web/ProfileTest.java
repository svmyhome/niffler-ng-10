package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.assertions.ImageAssertions;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.ProfilePage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.awt.image.BufferedImage;
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
    @ApiLogin
    @Test
    @DisplayName("Profile name should be editable")
    public void profileNameShouldBeEditable() {
        String name = "Ivan";
        Selenide.open(ProfilePage.URL, ProfilePage.class)
                .setName(name)
                .saveChanges()
                .checkSnackBarText("Profile successfully updated")
                .goToMainPage()
                .openProfile()
                .checkName(name);
    }

    @User
    @ApiLogin
    @Test
    @DisplayName("User cannot upload invalid image as profile picture")
    public void shouldFailWhenUploadingInvalidPicture() {
        Selenide.open(ProfilePage.URL, ProfilePage.class)
                .checkProfileIsDisplayed()
                .uploadNewPictureInProfile("profile.jpg")
                .saveChanges()
                .checkSnackBarText("Error while updating profile: Failed to read request");
    }

    @User
    @ApiLogin
    @ScreenShotTest(value = "img/avatar.jpg")
    @DisplayName("User can save valid picture in the profile")
    public void shouldSaveValidPictureInProfile(BufferedImage expected) {
        ProfilePage getProfile = Selenide.open(ProfilePage.URL, ProfilePage.class)
                .checkProfileIsDisplayed()
                .uploadNewPictureInProfile("avatar.jpg")
                .saveChanges()
                .checkSnackBarText("Profile successfully updated");

        ImageAssertions.checkScreenshotMatches(expected,
                getProfile.captureAvatarScreenshot());
    }
}
