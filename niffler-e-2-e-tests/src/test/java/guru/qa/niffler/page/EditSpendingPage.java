package guru.qa.niffler.page;

import static com.codeborne.selenide.Selenide.$;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.data.constants.Currency;
import guru.qa.niffler.page.component.Calendar;
import io.qameta.allure.Step;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class EditSpendingPage {

  private final SelenideElement
      amountInput = $("#amount"),
      currencyInput = $("#currency"),
      setCurrency = $("[data-value='RUB']"),
      categoryInput = $("#category"), descriptionInput = $("#description");
  private final SelenideElement saveBtn = $("#save");

  private final Calendar calendar = new Calendar();

  @Step("Add new spending: '{description}'")
  public @Nonnull MainPage setNewSpendingDescription(String description) {
    descriptionInput.val(description);
    saveBtn.click();
    return new MainPage();
  }

  @Step("Add new spending")
  public @Nonnull MainPage fillSpending(double amount, Currency currency, String category,
      java.util.Calendar date,
      String description) {
    amountInput.setValue(String.valueOf(amount));
    currencyInput.click();
    $(String.format("[data-value='%s']", currency)).click();
    categoryInput.setValue(category);
    calendar.selectDateInCalendar(date.getTime());
    descriptionInput.val(description);
    saveBtn.click();
    return new MainPage();
  }

  @Step("Click add spending")
  public @Nonnull MainPage save() {
    saveBtn.click();
    return new MainPage();
  }
}
