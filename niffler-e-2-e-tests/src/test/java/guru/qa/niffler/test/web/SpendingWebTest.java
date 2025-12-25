package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.spend.CurrencyValues;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
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
    System.out.println(user.username());
    System.out.println(user.testData().spendings().getFirst().description());
    System.out.println(newDescription);
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
    System.out.println(user.username());
    System.out.println(user.testData().spendings().getFirst().description());
    System.out.println(newDescription);
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
//        .createNewSpending()
        .editSpending(user.testData().spendings().getFirst().description())
        .setNewSpendingDescription(newDescription)
        .searchSpending(newDescription)
        .checkThatTableContains(newDescription);
    System.out.println(user.username());
    System.out.println(user.testData().spendings().getFirst().description());
    System.out.println(newDescription);
  }

//  @User
//  @Test
//  void createSpendingDescriptionShouldBeVisible(UserJson user) {
//    final String newDescription = "Обучение Niffler Next Generation 10";
//    Calendar cal = Calendar.getInstance();
//    cal.set(2024, 11, 12);
//
//    Selenide.open(CFG.frontUrl(), LoginPage.class)
//        .login(user.username(), user.testData().password())
//        .historyOfSpendingIsVisible()
//        .openNewSpending()
//        .fillSpending(123.0, "QAZ", cal, "qaz");

  /// /        .createSpending() /
  /// .editSpending(user.testData().spendings().getFirst().description()) /
  /// .setNewSpendingDescription(newDescription) /        .searchSpending(newDescription) /
  /// .checkThatTableContains(newDescription);
//    System.out.println(user.username());
//    System.out.println(user.testData().spendings().getFirst().description());
//    System.out.println(newDescription);
//  }
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
  void searchSpendingShouldBeVisible(UserJson user) {
    final String newDescription = "Обучение Niffler Next Generation 10";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .historyOfSpendingIsVisible()
//        .createNewSpending()
        .editSpending(user.testData().spendings().getFirst().description())
        .setNewSpendingDescription(newDescription)
        .searchSpending(newDescription)
        .checkThatTableContains(newDescription);
    System.out.println(user.username());
    System.out.println(user.testData().spendings().getFirst().description());
    System.out.println(newDescription);
  }
}

