package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.AllPeoplesPage;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;

public class Header {

  private final SelenideElement self = $("#root header");
  //#account-menu [href='/people/friends']

  public FriendsPage toFriendsPage() {
    openMenu();
    $("[href='/people/friends']").click();
    return new FriendsPage();
  }

  public AllPeoplesPage toAllPeoplesPage() {
    openMenu();
    $("[href='/people/all']").click();
    return new AllPeoplesPage();
  }

  public ProfilePage toProfilePage() {
    openMenu();
    $("[href='/profile']").click();
    return new ProfilePage();
  }

  public LoginPage signOut() {
    openMenu();
    $$("li").findBy(text("Sign out")).click();
    $$("button").findBy(Condition.exactText("Log out")).click();
    return new LoginPage();
  }

  public EditSpendingPage addSpendingPage() {
    self.$("[href='/spending']").click();
    return new EditSpendingPage();
  }

  public MainPage toMainPage() {
    self.$("[href='/main']").click();
    return new MainPage();
  }

  public Header openMenu() {
    self.$(".MuiAvatar-root").click();
    return this;
  }


}

