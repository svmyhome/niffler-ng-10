package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;

public class MainPage {

  private final ElementsCollection tableRows = $$("#spendings tr"),
      sectionHeaders = $$("h2");

  private final SelenideElement mainPage = $("#root"),
      menu = $(".MuiAvatar-root"),
      profile = $("[href='/profile']"),
      friends = $("[href='/people/friends']");

  Header header = new Header();
  SearchField search = new SearchField();

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
//    menu.click();
    header.openMenu();
//    return header.newProfile();
    profile.click();
    return new ProfilePage();
  }

  @Step("Switch to friends page")
  public FriendsPage openFriends() {
    menu.click();
    friends.click();
    return new FriendsPage();
  }

  @Step("Switch to spending page")
  public EditSpendingPage openNewSpending() {
    return header.openNewSpending();
//    return new EditSpendingPage();
  }

  @Step("Open All People list")
  public MainPage historyOfSpendingIsVisible() {
    sectionHeaders.find(text("History of Spendings")).shouldBe(visible).click();
    return this;
  }

  @Step("Find friend")
  public MainPage searchSpending(String spendingName) {
    search.fill(spendingName);
    return this;
  }
}
