package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.data.constants.DataFilterValues;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SearchField;
import guru.qa.niffler.page.component.SpendingTable;
import io.qameta.allure.Step;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class MainPage  extends BasePage<MainPage> {

  private final ElementsCollection tableRows = $$("#spendings tr"),
      sectionHeaders = $$("h2");

  private final SelenideElement mainPage = $("#root");

  Header header = new Header();
  SearchField search = new SearchField();
  SpendingTable spendingTable = new SpendingTable();

  @Step("Edit spending: '{description}'")
  public @Nonnull EditSpendingPage editSpending(String description) {
    tableRows.find(text(description)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  @Step("Table should have '{description}'")
  public @Nonnull MainPage checkThatTableContains(String description) {
    tableRows.find(text(description)).should(visible);
    return this;
  }

  @Step("Successfully opened main page")
  public @Nonnull MainPage mainPageShouldBeDisplayed() {
    mainPage.shouldHave(text("Statistics"));
    mainPage.shouldHave(text("History of Spendings"));
    return this;
  }

  @Step("Open menu")
  public @Nonnull MainPage openMenu() {
    header.openMenu();
    return this;
  }

  @Step("Switch to friends page")
  public @Nonnull FriendsPage openFriends() {
    return header.toFriendsPage();
  }

  @Step("Switch to All people page")
  public @Nonnull AllPeoplesPage openAllPeople() {
    return header.toAllPeoplesPage();
  }

  @Step("Switch to profile page")
  public @Nonnull ProfilePage openProfile() {
    return header.toProfilePage();
  }

  @Step("Switch to login page")
  public @Nonnull LoginPage signOut() {
    return header.signOut();
  }

  @Step("Switch to spending page")
  public @Nonnull EditSpendingPage openNewSpending() {
    return header.addSpendingPage();
  }

  @Step("Check that 'History of Spendings' header is visible")
  public @Nonnull MainPage historyOfSpendingIsVisible() {
    sectionHeaders.find(text("History of Spendings")).shouldBe(visible).click();
    return this;
  }

  @Step("Find friend")
  public @Nonnull MainPage searchSpending(String spendingName) {
    search.search(spendingName);
    return this;
  }

  @Step("Check spending description from table")
  public void checkSpendingDescriptionTable(String description) {
    spendingTable.searchSpendingByDescription(description);
  }

  @Step("Delete spending from table")
  public @Nonnull MainPage deleteSpendingFromTable(String description) {
    spendingTable.deleteSpending(description);
    return this;
  }

  @Step("Check spend is deleted")
  public void checkSpendIsDeleted() {
    $(".MuiTypography-root").shouldHave(text("Spendings succesfully deleted"));
  }

  @Step("Edit spending from table")
  public @Nonnull EditSpendingPage editSpendingFromTable(String description) {
    spendingTable.editSpending(description);
    return new EditSpendingPage();
  }

  @Step("Select period spendings")
  public @Nonnull MainPage selectPeriodSpendingFromTable(DataFilterValues period) {
    spendingTable.selectPeriod(period);
    return this;
  }

  @Step("Check period is selected")
  public @Nonnull MainPage checkSelectPeriodSpendingFromTable(DataFilterValues period) {
    spendingTable.checkPeriodIsSelected(period);
    return this;
  }

  @Step("Check period is selected")
  public @Nonnull MainPage checkTableContent(String... expectedSpends) {
    spendingTable.checkTableContains(expectedSpends);
    return this;
  }

  @Step("Check table have {count} rows")
  public @Nonnull MainPage checkTableSize(int count) {
    spendingTable.checkTableSize(count);
    return this;
  }
}
