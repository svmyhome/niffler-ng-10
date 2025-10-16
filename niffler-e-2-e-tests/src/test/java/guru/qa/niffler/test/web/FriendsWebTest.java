package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.UserType;
import guru.qa.niffler.jupiter.annotation.UserType.FriendType;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.model.StaticUser;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({UserQueueExtension.class, BrowserExtension.class})
public class FriendsWebTest {

  private final static Config CFG = Config.getInstance();

  @Test
  public void friendShouldBePresentInFriendsTable(
      @UserType(FriendType.WITH_FRIEND) StaticUser user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.password())
        .openFriends()
        .verifyMyFriendsSectionDisplayed()
        .verifyUserHasNewFriend(user.friend());
  }

  @Test
  public void friendsTableShouldBeEmptyForNewFrieds(
      @UserType(FriendType.EMPTY) StaticUser user
  ) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.password())
        .openFriends()
        .verifyFriendsTableIsEmpty();
  }

  @Test
  public void incomeInvitationBePresentInFriendsTable(
      @UserType(FriendType.WITH_INCOME_REQUEST) StaticUser user
  ) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.password())
        .openFriends()
        .verifyMyFriendsRequestSectionDisplayed()
        .verifyUserHasNewIncomingFriendRequest(user.income());
  }

  @Test
  public void outcomeInvitationBePresentInFriendsTable(
      @UserType(FriendType.WITH_OUTCOME_REQUEST) StaticUser user
  ) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.password())
        .openFriends()
        .verifyFriendsTableIsEmpty()
        .openAllPeopleList()
        .verifyUserHasNewOutcomingFriendRequest(user.outcome());
  }
}
