package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Selenide.$;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;

public class Header {

  private final SelenideElement self = $("#root header");

  public EditSpendingPage openNewSpending() {
    self.$("[href='/spending']").click();
    return new EditSpendingPage();
  }

  public MainPage openMenu() {
    self.$(".MuiAvatar-root").click();
    return new MainPage();
  }

  // TODO как будто пернести в меню
  public ProfilePage newProfile() {
    self.$("[href='/profile']").click();
    return new ProfilePage();
  }
}


//public class Header {
//  public FriendsPage toFriendsPage() {}
//  public PeoplePage toAllPeoplesPage() {}
//  public ProfilePage toProfilePage() {}
//  public LoginPage signOut() {}
//  public EditSpendingPage addSpendingPage() {}
//  public MainPage toMainPage() {}
//}