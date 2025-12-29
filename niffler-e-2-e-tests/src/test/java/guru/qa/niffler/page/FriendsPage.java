package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
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

  @Step("User accept friendship request")
  public FriendsPage acceptFriendRequest() {
    $(byText("Accept")).click();
    return this;
  }

  @Step("Check user have friend")
  public FriendsPage verifyUserHaveFriend() {
    $(byText("Unfriend")).shouldBe(visible);
    return this;
  }

  @Step("User decline friendship request")
  public FriendsPage declineFriendRequest() {
    $(byText("Decline")).click();
    $("[role='dialog']").$$("button").findBy(text("Decline")).click();
    return this;
  }

  @Step("Check that friends table is empty")
  public FriendsPage verifyFriendsTableIsEmpty() {
    emptyTable.shouldHave(text("There are no users yet")).shouldBe(visible);
    return this;
  }

  @Step("Open All People list")
  public AllPeoplesPage openAllPeoplePage() {
    sectionHeaders.find(text("All people")).shouldBe(visible).click();
    return new AllPeoplesPage();
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
    searchField.search(friendName);
    return this;
  }

  @Step("Clear friend by search")
  public FriendsPage clearFriendBySearch() {
    searchField.clearIfNotEmpty();
    return this;
  }
}
