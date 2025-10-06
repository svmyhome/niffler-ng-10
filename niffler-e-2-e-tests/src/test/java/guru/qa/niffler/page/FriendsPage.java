package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

public class FriendsPage {

  private final ElementsCollection headersH2 = $$("h2"),
      friendsTableCells = $$("#friends tr td"),
      requestTableCells = $$("#requests tr td"),
      allPeopleTableCells = $$("#all tr td");

  private final SelenideElement friendsTable = $("#friends"),
      requestFriendsTable = $("requests"),
      emptyTable = $("#simple-tabpanel-friends"),
      allPeople = $("#all");

  @Step("My friends is display")
  public FriendsPage checkMyFriensIsDisplayed() {
    headersH2.find(text("My friends")).shouldBe(visible);
    return this;
  }

  @Step("Check that user has a friend")
  public FriendsPage userShouldHaveNewFriends(String friendName, String friendshipSataus) {
    friendsTableCells.find(text(friendName)).shouldBe(visible)
        .sibling(0).shouldHave(text(friendshipSataus));
    return this;
  }

  @Step("Check that user has a friend requests")
  public FriendsPage userShouldHaveNewRequestFriends(String friendName, String friendshipSataus) {
    requestTableCells.find(text(friendName)).shouldBe(visible)
        .sibling(0).shouldHave(text(friendshipSataus));
    return this;
  }

  @Step("Check that user has a invite people")
  public FriendsPage userShouldHaveNewOutcominFriends(String friendName, String friendshipSataus) {
    allPeopleTableCells.find(text(friendName)).shouldBe(visible)
        .sibling(0).shouldHave(text(friendshipSataus));
    return this;
  }

  @Step("Check that user has not a new friend in friends table")
  public FriendsPage tableShouldNotHaveNewFriends() {
    emptyTable.shouldHave(text("There are no users yet")).shouldBe(visible);
    return this;
  }

  @Step("Switch to All people list")
  public FriendsPage switchToAllPeople() {
    headersH2.find(text("All people")).shouldBe(visible).click();
    return this;
  }

  @Step("Friend requests is display")
  public FriendsPage friendsRequestIsDisplayed() {
    headersH2.find(text("Friend requests")).shouldBe(visible);
    return this;
  }
}
