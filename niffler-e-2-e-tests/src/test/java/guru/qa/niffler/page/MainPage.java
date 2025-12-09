package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

public class MainPage {

  private final ElementsCollection tableRows = $$("#spendings tr");
  private final SelenideElement mainPage = $("#root"),
      menu = $(".MuiAvatar-root"),
      profile = $("[href='/profile']"),
      friends = $("[href='/people/friends']");

  @Step("Edit spending: '{description}'")
  public EditSpendingPage editSpending(String description) {
    tableRows.find(text(description)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  @Step("Table should have '{description}'")
  public MainPage checkThatTableContains(String description) {
    tableRows.find(text(description)).should(visible);
    return this;
  }

  @Step("Successfully opened main page")
  public void mainPageShouldBeDisplayed() {
    mainPage.shouldHave(text("Statistics"));
    mainPage.shouldHave(text("History of Spendings"));
  }

  @Step("Open menu")
  public MainPage openMenu() {
    menu.click();
    return this;
  }

  @Step("Switch to profile page")
  public ProfilePage openProfile() {
    menu.click();
    profile.click();
    return new ProfilePage();
  }

  @Step("Switch to friends page")
  public FriendsPage openFriends() {
    menu.click();
    friends.click();
    return new FriendsPage();
  }
}
