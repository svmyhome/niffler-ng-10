package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {
    public LoginPage loginPage = new LoginPage();


    private final SelenideElement category = $("#category");
    private final SelenideElement archivedCategoryBtn = $("//*[text()='Учеба']/ancestor::div//button[@aria-label='Archive category']");
    private final SelenideElement archiveBtn = $("//*[text()='Archive']");
    private final SelenideElement showArchived = $("//*[text()='Show archived']");
    private final SelenideElement profile = $("h2:contains('Profile')");


    public void checkProfile() {
        $("h2").shouldHave(text("Profile"));

    }



}
