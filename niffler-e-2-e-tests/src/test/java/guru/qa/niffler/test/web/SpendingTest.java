package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.spend.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.service.SpendDnEntityClient;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class SpendingTest {

  private static final Config CFG = Config.getInstance();

  @User(
      username = "duck",
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
  void spendingDescriptionShouldBeEditedByTableAction(SpendJson spending) {
    final String newDescription = "Обучение Niffler Next Generation 10";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login("duck", "12345")
        .editSpending(spending.description())
        .setNewSpendingDescription(newDescription)
        .checkThatTableContains(newDescription);
  }

  @Test
  void findSpendingByIdTest() {
    SpendDnEntityClient spendDnEntityClient = new SpendDnEntityClient();
    Optional<SpendEntity> byId = spendDnEntityClient.findById(
        UUID.fromString("1328a312-b5bc-11f0-a017-aa5c32f82d84"));
    System.out.println(byId);
  }
}
