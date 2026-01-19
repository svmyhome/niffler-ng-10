package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.AllPeoplesPage;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import io.qameta.allure.Step;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class Header extends BaseComponent<Header> {

  private final SelenideElement openMenu = self.$(".MuiAvatar-root"),
      menu = $("#account-menu"),
      profile = $("[href='/profile']"),
      friends = $("[href='/people/friends']"),
      allPeople = $("[href='/people/all']"),
      spending = self.$("[href='/spending']"),
      main = self.$("[href='/main']");

  private final ElementsCollection menuItems = $("#account-menu").$$("li"),
      actionButtons = $$("button");

  public Header() {
    super($("#root header"));
  }

  @Step("Open menu")
  public @Nonnull Header openMenu() {
    openMenu.click();
    menu.shouldBe(visible);
    return this;
  }

  @Step("Open profile page")
  public @Nonnull ProfilePage toProfilePage() {
    openMenu();
    profile.shouldBe(visible).click();
    return new ProfilePage();
  }

  @Step("Open friends page")
  public @Nonnull FriendsPage toFriendsPage() {
    openMenu();
    friends.shouldBe(visible).click();
    return new FriendsPage();
  }

  @Step("Open all people page")
  public @Nonnull AllPeoplesPage toAllPeoplesPage() {
    openMenu();
    allPeople.click();
    return new AllPeoplesPage();
  }

  @Step("Select Sign out from menu")
  public @Nonnull Header selectSignOut() {
    menuItems.findBy(text("Sign out")).click();
    return this;
  }

  @Step("Confirm logout")
  public @Nonnull LoginPage confirmLogout() {
    actionButtons.findBy(exactText("Log out")).click();
    return new LoginPage();
  }

  @Step("Sign out")
  public @Nonnull LoginPage signOut() {
    openMenu();
    selectSignOut().confirmLogout();
    return new LoginPage();
  }

  @Step("Open spending page")
  public @Nonnull EditSpendingPage addSpendingPage() {
    spending.click();
    return new EditSpendingPage();
  }

  @Step("Open main page")
  public @Nonnull MainPage toMainPage() {
    main.click();
    return new MainPage();
  }
}

