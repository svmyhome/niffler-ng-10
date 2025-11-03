package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

public class ProfilePage {

  private final SelenideElement category = $("#category"),
      archivedCategoryBtn = $(
          "//*[text()='Учеба']/ancestor::div//button[@aria-label='Archive category']"),
      archiveBtn = $("//*[text()='Archive']"),
      showArchived = $(".MuiSwitch-input");
  private final ElementsCollection
      categories = $$(".MuiChip-filled.MuiChip-colorPrimary"),
      categoriesArchived = $$(".MuiChip-filled.MuiChip-colorDefault"),
      headersH2 = $$("h2");

  @Step("Successfully opened profile")
  public ProfilePage checkProfileIsDisplayed() {
    headersH2.find(text("Profile")).should(visible);
    return this;
  }

  @Step("Check category: '{category}'")
  public boolean checkCategoryIsNotDisplayed(String category) {
    categories.find(text(category)).shouldNot(visible);
    return true;
  }

  @Step("Check category: '{category}'")
  public ProfilePage checkCategoryIsDisplayed(String category) {
    categories.find(text(category)).shouldBe(visible);
    return this;
  }

  @Step("Check archived category: '{category}'")
  public ProfilePage checkArchivedCategoryExists(String category) {
    showArchived.click();
    categoriesArchived.find(text(category)).shouldBe(visible);
    return this;
  }
}
