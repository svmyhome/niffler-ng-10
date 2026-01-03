package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.constants.Currency;
import guru.qa.niffler.data.constants.DataFilterValues;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.spend.CurrencyValues;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

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
  void spendingDescriptionShouldBeVisible1(UserJson user) {
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
  void shouldCreateNewSpendingWithValidData(UserJson user) {
    final String newDescription = "qaz";
    Calendar cal = Calendar.getInstance();
    cal.set(2011, Calendar.NOVEMBER, 12);

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .historyOfSpendingIsVisible()
        .openNewSpending()
        .fillSpending(123.0, Currency.RUB,"QAZ", cal, newDescription)
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
  void spendingShouldBeEdit(UserJson user) {
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
  void selectPeriodSpending(UserJson user) {
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
  void checkTableContains(UserJson user) {
    String today = LocalDate.now()
        .format(DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH));

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .historyOfSpendingIsVisible()
        .checkTableContent("Машина", "89900", String.valueOf(CurrencyValues.RUB),
            "Обучение Niffler 2.0 юбилейный поток!", today)
        .checkTableSize(1);
  }
}


