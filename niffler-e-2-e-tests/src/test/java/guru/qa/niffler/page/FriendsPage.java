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
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class FriendsPage  extends BasePage<FriendsPage> {

  public static final String URL = CFG.frontUrl() + "people/friends";

  private final SelenideElement
      friendsTable = $("#friends"),
      requestsTable = $("#requests"),
      emptyTable = $("#simple-tabpanel-friends");

  private final SearchField searchField = new SearchField();

  private final ElementsCollection sectionHeaders = $$("h2"),
      friendsTablesCells = friendsTable.$$("tr td"),
      requestsTablesCells = requestsTable.$$("tr td");

  @Step("Check that user has a new friend")
  public @Nonnull FriendsPage verifyUserHasNewFriend(String friendName) {
    searchFriend(friendName);
    SelenideElement friendCell = friendsTablesCells.find(text(friendName));
    friendCell.shouldBe(visible);
    return this;
  }

  @Step("Check that user has a new incoming requests")
  public @Nonnull FriendsPage verifyUserHasNewIncomingFriendRequest(String friendName) {
    requestsTablesCells.find(text(friendName)).shouldBe(visible);
    return this;
  }

  @Step("User accept friendship request")
  public @Nonnull FriendsPage acceptFriendRequest() {
    $(byText("Accept")).click();
    return this;
  }

  @Step("Check user have friend")
  public @Nonnull FriendsPage verifyUserHaveFriend() {
    $(byText("Unfriend")).shouldBe(visible);
    return this;
  }

  @Step("User decline friendship request")
  public @Nonnull FriendsPage declineFriendRequest() {
    $(byText("Decline")).click();
    $("[role='dialog']").$$("button").findBy(text("Decline")).click();
    return this;
  }

  @Step("Check that friends table is empty")
  public @Nonnull FriendsPage verifyFriendsTableIsEmpty() {
    emptyTable.shouldHave(text("There are no users yet")).shouldBe(visible);
    return this;
  }

  @Step("Open All People list")
  public @Nonnull AllPeoplesPage openAllPeoplePage() {
    sectionHeaders.find(text("All people")).shouldBe(visible).click();
    return new AllPeoplesPage();
  }

  @Step("Verify My Friends section is displayed")
  public @Nonnull FriendsPage verifyMyFriendsSectionDisplayed() {
    sectionHeaders.find(text("My friends")).shouldBe(visible);
    return this;
  }

  @Step("Verify My Friends request section is displayed")
  public @Nonnull FriendsPage verifyMyFriendsRequestSectionDisplayed() {
    sectionHeaders.find(text("Friend requests")).shouldBe(visible);
    return this;
  }

  @Step("Find friend")
  public @Nonnull FriendsPage searchFriend(String friendName) {
    searchField.search(friendName);
    return this;
  }

  @Step("Clear friend by search")
  public @Nonnull FriendsPage clearFriendBySearch() {
    searchField.clearIfNotEmpty();
    return this;
  }

  @Step("Check search field is cleared from friend name")
  public @Nonnull FriendsPage checkSearchFieldIsCleared() {
    searchField.checkSearchFieldEmpty();
    return this;
  }
}