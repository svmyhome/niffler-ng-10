package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Epic("UI")
@Feature("User management")
@Story("Friends")
@ExtendWith({UserQueueExtension.class, BrowserExtension.class})
public class FriendsWebTest {

  private final static Config CFG = Config.getInstance();

  @User(
      friends = 1
  )
  @Test
  @DisplayName("Added friend should be visible in friends list")
  public void friendShouldBePresentInFriendsTable(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .openFriends()
        .verifyMyFriendsSectionDisplayed()
        .verifyUserHasNewFriend(user.testData().friends().getFirst().username());
  }

  @User(
      friends = 1
  )
  @Test
  @DisplayName("Search field should be cleared after clicking clear button")
  public void friendShouldNotBePresentInFriendsTableAfterClear(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .openFriends()
        .verifyMyFriendsSectionDisplayed()
        .verifyUserHasNewFriend(user.testData().friends().getFirst().username())
        .clearFriendBySearch()
        .checkSearchFieldIsCleared();
  }

  @User(
      friends = 0
  )
  @Test
  @DisplayName("Friends table is empty")
  public void friendsTableShouldBeEmptyForNewFriends(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .openFriends()
        .verifyFriendsTableIsEmpty();
  }

  @User(
      incomeInvitations = 1
  )
  @Test
  @DisplayName("User should see incoming friend request in friends table")
  public void incomingFriendRequestShouldBePresentInFriendsTable(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .openFriends()
        .verifyMyFriendsRequestSectionDisplayed()
        .verifyUserHasNewIncomingFriendRequest(
            user.testData().incomeInvitation().getFirst().username());
  }

  @User(
      incomeInvitations = 1
  )
  @Test
  @DisplayName("User should be able to decline incoming friend request")
  public void shouldDeclineFriendRequest(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .openFriends()
        .verifyMyFriendsRequestSectionDisplayed()
        .verifyUserHasNewIncomingFriendRequest(
            user.testData().incomeInvitation().getFirst().username())
        .declineFriendRequest()
        .verifyFriendsTableIsEmpty();
  }

  @User(
      incomeInvitations = 1
  )
  @Test
  @DisplayName("User should be able to accept incoming friend request")
  public void shouldAcceptFriendRequest(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .openFriends()
        .verifyMyFriendsRequestSectionDisplayed()
        .verifyUserHasNewIncomingFriendRequest(
            user.testData().incomeInvitation().getFirst().username())
        .acceptFriendRequest()
        .verifyUserHaveFriend();
  }

  @User(
      outcomeInvitations = 1
  )
  @Test
  @DisplayName("User should see outcoming friend request in friends table")
  public void outcomeInvitationBePresentInFriendsTable(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .openFriends()
        .verifyFriendsTableIsEmpty()
        .openAllPeoplePage()
        .verifyUserHasNewOutcomingFriendRequest(
            user.testData().outcomeInvitation().getFirst().username());
  }
}
