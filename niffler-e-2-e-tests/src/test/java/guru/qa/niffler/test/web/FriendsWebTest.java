package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({UserQueueExtension.class, BrowserExtension.class})
public class FriendsWebTest {

  private final static Config CFG = Config.getInstance();

  @User(
      friends = 1
  )
  @Test
  public void friendShouldBePresentInFriendsTable(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .openFriends()
        .verifyMyFriendsSectionDisplayed()
        .verifyUserHasNewFriend(user.testData().friends().getFirst().username());
    System.out.println(user.username());
    System.out.println(user.testData().friends().getFirst().username());
  }

  @User(
      friends = 0
  )
  @Test
  public void friendsTableShouldBeEmptyForNewFrieds(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .openFriends()
        .verifyFriendsTableIsEmpty();
    System.out.println(user.username());
  }

  @User(
      incomeInvitations = 1
  )
  @Test
  public void incomeInvitationBePresentInFriendsTable(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .openFriends()
        .verifyMyFriendsRequestSectionDisplayed()
        .verifyUserHasNewIncomingFriendRequest(
            user.testData().incomeInvitation().getFirst().username());
    System.out.println(user.username());
    System.out.println(user.testData().incomeInvitation().getFirst().username());
  }

  @User(
      outcomeInvitations = 1
  )
  @Test
  public void outcomeInvitationBePresentInFriendsTable(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .openFriends()
        .verifyFriendsTableIsEmpty()
        .openAllPeopleList()
        .verifyUserHasNewOutcomingFriendRequest(
            user.testData().outcomeInvitation().getFirst().username());
    System.out.println(user.username());
    System.out.println(user.testData().outcomeInvitation().getFirst().username());
  }
}
