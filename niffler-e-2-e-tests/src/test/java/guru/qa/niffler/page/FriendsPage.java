package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;

public class FriendsPage {

  private final SelenideElement
      friendsTable = $("#friends"),
      requestsTable = $("#requests"),
      allUsersTable = $("#all"),
      emptyTable = $("#simple-tabpanel-friends");

  private final SearchField searchField = new SearchField();

  private final ElementsCollection sectionHeaders = $$("h2");

  public ElementsCollection getAcceptedFriends() {
    return friendsTable.$$("tr td");
  }

  public ElementsCollection getIncomingRequests() {
    return requestsTable.$$("tr td");
  }

  public ElementsCollection getAllUsers() {
    return allUsersTable.$$("tr td");
  }


  @Step("Check that user has a new friend")
  public FriendsPage verifyUserHasNewFriend(String friendName) {
    searchFriend(friendName);
    SelenideElement friendCell = getAcceptedFriends().find(text(friendName));
    friendCell.shouldBe(visible);
    return this;
  }

  @Step("Check that user has a new incoming requests")
  public FriendsPage verifyUserHasNewIncomingFriendRequest(String friendName) {
    getIncomingRequests().find(text(friendName)).shouldBe(visible);
    return this;
  }

  @Step("Check that user has a new outcoming requests")
  public FriendsPage verifyUserHasNewOutcomingFriendRequest(String friendName) {
    getAllUsers().find(text(friendName)).shouldBe(visible);
    return this;
  }

  @Step("Check that friends table is empty")
  public FriendsPage verifyFriendsTableIsEmpty() {
    emptyTable.shouldHave(text("There are no users yet")).shouldBe(visible);
    return this;
  }

  @Step("Open All People list")
  public FriendsPage openAllPeopleList() {
    sectionHeaders.find(text("All people")).shouldBe(visible).click();
    return this;
  }

  @Step("Verify My Friends section is displayed")
  public FriendsPage verifyMyFriendsSectionDisplayed() {
    sectionHeaders.find(text("My friends")).shouldBe(visible);
    return this;
  }

  @Step("Verify My Friends request section is displayed")
  public FriendsPage verifyMyFriendsRequestSectionDisplayed() {
    sectionHeaders.find(text("Friend requests")).shouldBe(visible);
    return this;
  }

  @Step("Find friend")
  public FriendsPage searchFriend(String friendName) {
    searchField.fill(friendName);
    return this;
  }

  @Step("Clear friend by search")
  public FriendsPage clearFriendBySearch() {
    searchField.clear();
    return this;
  }
}
