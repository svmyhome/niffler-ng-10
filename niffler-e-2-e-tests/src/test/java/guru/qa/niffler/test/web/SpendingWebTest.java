package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.assertions.ImageAssertions;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.constants.Currency;
import guru.qa.niffler.data.constants.DataFilterValues;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.spend.Bubble;
import guru.qa.niffler.model.spend.CurrencyValues;
import guru.qa.niffler.model.spend.RowSpend;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.component.SpendingsHistoryTable;
import guru.qa.niffler.page.component.StatComponent;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Epic("UI")
@Feature("Categories and Spendings")
@Story("Spending Management")
@ExtendWith(BrowserExtension.class)
public class SpendingWebTest {

  private static final Config CFG = Config.getInstance();

  @User(
      spendings = {@Spending(
          category = "Машина",
          amount = 89900,
          currency = CurrencyValues.RUB,
          description = "Обучение Niffler 2.0 юбилейный поток!"
      ),
          @Spending(
              category = "Пиво",
              amount = 100,
              currency = CurrencyValues.RUB,
              description = "Обучение Niffler 2.0 юбилейный поток!"
          )}
  )
  @Test
  @DisplayName("User should be able to edit spending description")
  void spendingDescriptionShouldBeEditedByTableAction(UserJson user) {
    final String newDescription = "Обучение Niffler Next Generation 10";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .editSpending(user.testData().spendings().getFirst().description())
        .setNewSpendingDescription(newDescription)
        .checkThatTableContains(newDescription);
  }

  @User(
      spendings = {@Spending(
          category = "Машина",
          amount = 89900,
          currency = CurrencyValues.RUB,
          description = "Обучение Niffler 2.0 юбилейный поток!"
      ),
          @Spending(
              category = "Пиво",
              amount = 100,
              currency = CurrencyValues.RUB,
              description = "Обучение Niffler 2.0 юбилейный поток!"
          )}
  )
  @Test
  @DisplayName("New spending description should be visible")
  void spendingDescriptionShouldBeVisible(UserJson user) {
    final String newDescription = "Обучение Niffler Next Generation 10";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .historyOfSpendingIsVisible()
        .editSpending(user.testData().spendings().getFirst().description())
        .setNewSpendingDescription(newDescription)
        .searchSpending(newDescription)
        .checkThatTableContains(newDescription);
  }

  @User
  @Test
  @DisplayName("User should be able create new spending")
  void shouldCreateNewSpendingWithValidData(UserJson user) {
    final String newDescription = "qaz";
    Calendar cal = Calendar.getInstance();
    cal.set(2011, Calendar.NOVEMBER, 12);

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .historyOfSpendingIsVisible()
        .openNewSpending()
        .fillSpending(123.0, Currency.RUB, "QAZ", cal, newDescription)
        .checkSnackBarText("New spending is successfully created")
        .checkThatTableContains(newDescription);
  }

  @User(
      spendings = {@Spending(
          category = "Машина",
          amount = 89900,
          currency = CurrencyValues.RUB,
          description = "Обучение Niffler 2.0 юбилейный поток!"
      )}
  )
  @Test
  @DisplayName("User should be able delete spending")
  void spendingShouldBeDeleted(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .historyOfSpendingIsVisible()
        .deleteSpendingFromTable("Обучение Niffler 2.0 юбилейный поток!")
        .checkSpendIsDeleted();
  }

  @User(
      spendings = {@Spending(
          category = "Машина",
          amount = 89900,
          currency = CurrencyValues.RUB,
          description = "Обучение Niffler 2.0 юбилейный поток!"
      )}
  )
  @Test
  @DisplayName("User should be able to edit all fields of a spending")
  void userShouldBeAbleToEditAllSpendingFields(UserJson user) {
    final String newDescription = "Обучение Niffler 2.0 юбилейный поток!";
    Calendar date = Calendar.getInstance();
    date.set(2024, 11, 12);

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .historyOfSpendingIsVisible()
        .editSpendingFromTable(newDescription)
        .fillSpending(123.0, Currency.RUB, "QAZ", date, "qaz")
        .checkSpendingDescriptionTable(newDescription);
  }

  @User(
      spendings = {@Spending(
          category = "Машина",
          amount = 89900,
          currency = CurrencyValues.RUB,
          description = "Обучение Niffler 2.0 юбилейный поток!"
      )}
  )
  @Test
  @DisplayName("User should be able select spending by period")
  void userShouldBeAbleSelectPeriodSpending(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .historyOfSpendingIsVisible()
        .selectPeriodSpendingFromTable(DataFilterValues.WEEK)
        .checkSelectPeriodSpendingFromTable(DataFilterValues.WEEK);
  }

  @User(
      spendings = {@Spending(
          category = "Машина",
          amount = 89900,
          currency = CurrencyValues.RUB,
          description = "Обучение Niffler 2.0 юбилейный поток!"
      )}
  )
  @Test
  @DisplayName("Spending should be displayed correctly in the table")
  void spendingShouldBeVisibleFromTable(UserJson user) {
    String today = LocalDate.now()
        .format(DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH));

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .historyOfSpendingIsVisible()
        .checkTableContent("Машина", "89900", String.valueOf(CurrencyValues.RUB),
            "Обучение Niffler 2.0 юбилейный поток!", today)
        .checkTableSize(1);
  }

  @User(
      spendings = {@Spending(
          category = "Машина",
          amount = 300,
          currency = CurrencyValues.RUB,
          description = "На ТО"
      ),
          @Spending(
              category = "Книги",
              amount = 200,
              currency = CurrencyValues.RUB,
              description = "Обучение Niffler 2.0 юбилейный поток!"
          )}
  )
  @ScreenShotTest(value = "img/spendings.png")
  @DisplayName("Spending chart should correctly display test data")
  void spendingChartShouldDisplayTestDataCorrectly(UserJson user, BufferedImage expected) {
    StatComponent statComponent = Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .historyOfSpendingIsVisible()
        .getStatComponent();

    Selenide.sleep(4000);
    ImageAssertions.checkScreenshotMatches(expected,
        statComponent.getChartScreenshot());
    statComponent.checkBubbles(
        new Bubble(Color.yellow, "Машина 300 ₽"),
        new Bubble(Color.green, "Книги 200 ₽")
    );
  }

  @User(
      spendings = {@Spending(
          category = "Машина",
          amount = 300,
          currency = CurrencyValues.RUB,
          description = "На ТО"
      ),
          @Spending(
              category = "Книги",
              amount = 200,
              currency = CurrencyValues.RUB,
              description = "Обучение Niffler 2.0 юбилейный поток!"
          )}
  )
  @ScreenShotTest(value = "img/spending.png")
  @DisplayName("Spending chart should refresh correctly after deletion")
  void spendingChartShouldRefreshAfterDeletion(UserJson user, BufferedImage expected) {
    StatComponent statComponent = Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .historyOfSpendingIsVisible()
        .deleteSpendingFromTable("На ТО")
        .getStatComponent();

    Selenide.sleep(4000);
    ImageAssertions.checkScreenshotMatches(expected,
        statComponent.getChartScreenshot());
    statComponent.checkBubbles(new Bubble(Color.yellow, "Книги 200 ₽"));
  }

  @User(
      spendings = {@Spending(
          category = "Машина",
          amount = 300,
          currency = CurrencyValues.RUB,
          description = "На ТО"
      ),
          @Spending(
              category = "Книги",
              amount = 200,
              currency = CurrencyValues.RUB,
              description = "Обучение Niffler 2.0 юбилейный поток!"
          )}
  )
  @ScreenShotTest(value = "img/updateSpending.png")
  @DisplayName("Spending chart should update correctly")
  void spendingChartShouldDisplayCorrectlyAfterUpdate(UserJson user, BufferedImage expected) {
    StatComponent statComponent = Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .historyOfSpendingIsVisible()
        .editSpending("На ТО")
        .setNewAmount(1000.0)
        .getStatComponent();

    Selenide.sleep(4000);
    ImageAssertions.checkScreenshotMatches(expected,
        statComponent.getChartScreenshot());
    statComponent.checkBubbles(
        new Bubble(Color.yellow, "Машина 1000 ₽"),
        new Bubble(Color.green, "Книги 200 ₽")
    );
  }

  @User(
      spendings = {@Spending(
          category = "Машина",
          amount = 300,
          currency = CurrencyValues.RUB,
          description = "На ТО"
      ),
          @Spending(
              category = "Книги",
              amount = 200,
              currency = CurrencyValues.RUB,
              description = "Обучение Niffler 2.0 юбилейный поток!"
          )}
  )
  @ScreenShotTest(value = "img/updateSpending.png")
  @DisplayName("Spending chart should display correctly any order")
  void spendingChartShouldDisplayCorrectlyAnyOrder(UserJson user, BufferedImage expected) {
    StatComponent statComponent = Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .historyOfSpendingIsVisible()
        .editSpending("На ТО")
        .setNewAmount(1000.0)
        .getStatComponent();

    statComponent.checkBubblesInAnyOrder(
        new Bubble(Color.yellow, "Машина 1000 ₽"),
        new Bubble(Color.green, "Книги 200 ₽")
    );
  }

  @User(
      spendings = {@Spending(
          category = "Машина",
          amount = 300,
          currency = CurrencyValues.RUB,
          description = "На ТО"
      ),
          @Spending(
              category = "Книги",
              amount = 200,
              currency = CurrencyValues.RUB,
              description = "Обучение Niffler 2.0 юбилейный поток!"
          )}
  )
  @ScreenShotTest(value = "img/updateSpending.png")
  @DisplayName("Spending chart should display contains spending")
  void spendingChartShouldDisplayContainsSpending(UserJson user, BufferedImage expected) {
    StatComponent statComponent = Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .historyOfSpendingIsVisible()
        .editSpending("На ТО")
        .setNewAmount(1000.0)
        .getStatComponent();

    statComponent.checkBubblesContains(
        new Bubble(Color.yellow, "Машина 1000 ₽"),
        new Bubble(Color.green, "Книги 200 ₽")
    );
  }

  @User(
      spendings = {@Spending(
          category = "Машина",
          amount = 300,
          currency = CurrencyValues.RUB,
          description = "На ТО"
      ),
          @Spending(
              category = "Книги",
              amount = 200,
              currency = CurrencyValues.RUB,
              description = "Обучение Niffler 2.0 юбилейный поток!"
          )}
  )
  @ScreenShotTest(value = "img/updateSpending.png")
  @DisplayName("History of spendings  should display all spendings")
  void historySpendingsShouldDisplaySpending(UserJson user, BufferedImage expected) {
    SpendingsHistoryTable spendingTable = Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .historyOfSpendingIsVisible()
        .getSpendingsHistoryComponent();

    spendingTable.checkSpends(new RowSpend(user.testData().spendings().get(1).category().name(),
            user.testData().spendings().get(1).amount(),
            user.testData().spendings().get(1).currency().name(),
            user.testData().spendings().get(1).description(),
            user.testData().spendings().get(1).spendDate().toString()
        ),
        new RowSpend(user.testData().spendings().getFirst().category().name(),
            user.testData().spendings().getFirst().amount(),
            user.testData().spendings().getFirst().currency().name(),
            user.testData().spendings().getFirst().description(),
            user.testData().spendings().getFirst().spendDate().toString()
        ));
  }
}