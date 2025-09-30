package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class SpendingTest {

  private static final Config CFG = Config.getInstance();

  @Spending(
      username = "duck",
      category = "Учеба",
      amount = 89900,
      currency = CurrencyValues.RUB,
      description = "Обучение Niffler 2.0 юбилейный поток!"
  )
  @Test
  void spendingDescriptionShouldBeEditedByTableAction(SpendJson spending) {
    final String newDescription = "Обучение Niffler Next Generation";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login("duck", "12345")
        .editSpending(spending.description())
        .setNewSpendingDescription(newDescription)
        .checkThatTableContains(newDescription);
  }


  @Category(
          name="qaz11111",
          username = "duck",
          archived = false
  )
  @Spending(
          username = "duck",
          category = "Учеба",
          amount = 89900,
          currency = CurrencyValues.RUB,
          description = "Обучение Niffler 2.0 юбилейный поток!"
  )
  @Test
  void spendingDescriptionShodHaveNewCategory(SpendJson spending){
    final String newDescription = "Обучение Niffler Next Generation1111";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .login("duck", "12345")
            .editSpending(spending.description())
            .setNewSpendingDescription(newDescription)
            .checkThatTableContains(newDescription);
  }
}
