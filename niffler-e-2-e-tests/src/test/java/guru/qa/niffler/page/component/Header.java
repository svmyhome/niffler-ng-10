package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
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
    private final SelenideElement openMenu, menu, profile, friends, allPeople, spending, main;
    private final ElementsCollection menuItems, actionButtons;

    public Header(SelenideDriver driver) {
        super(driver, driver != null ? driver.$("#root header") : Selenide.$("#root header"));

        // Инициализируем элементы через методы из BaseComponent
        this.openMenu = self.$(".MuiAvatar-root");
        this.menu = $("#account-menu");  // ← теперь использует $() из BaseComponent
        this.profile = $("[href='/profile']");
        this.friends = $("[href='/people/friends']");
        this.allPeople = $("[href='/people/all']");
        this.spending = self.$("[href='/spending']");
        this.main = self.$("[href='/main']");

        this.menuItems = $("#account-menu").$$("li");
        this.actionButtons = $$("button");
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
    return new ProfilePage(driver);
  }

  @Step("Open friends page")
  public @Nonnull FriendsPage toFriendsPage() {
    openMenu();
    friends.shouldBe(visible).click();
    return new FriendsPage(driver);
  }

  @Step("Open all people page")
  public @Nonnull AllPeoplesPage toAllPeoplesPage() {
    openMenu();
    allPeople.click();
    return new AllPeoplesPage(driver);
  }

  @Step("Select Sign out from menu")
  public @Nonnull Header selectSignOut() {
    menuItems.findBy(text("Sign out")).click();
    return this;
  }

  @Step("Confirm logout")
  public @Nonnull LoginPage confirmLogout() {
    actionButtons.findBy(exactText("Log out")).click();
    return new LoginPage(driver);
  }

  @Step("Sign out")
  public @Nonnull LoginPage signOut() {
    openMenu();
    selectSignOut().confirmLogout();
    return new LoginPage(driver);
  }

  @Step("Open spending page")
  public @Nonnull EditSpendingPage addSpendingPage() {
    spending.click();
    return new EditSpendingPage(driver);
  }

  @Step("Open main page")
  public @Nonnull MainPage toMainPage() {
    main.click();
    return new MainPage(driver);
  }
}

