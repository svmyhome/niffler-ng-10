package guru.qa.niffler.page;

import static com.codeborne.selenide.Selenide.$;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Calendar;
import io.qameta.allure.Step;

public class EditSpendingPage {

  private final SelenideElement
      amountInput = $("#amount"),
      currency= $("#currency"),
  rub = $("[data-value='RUB']"),
  categoryInput = $("#category"), descriptionInput = $("#description");
  private final SelenideElement saveBtn = $("#save");

  Calendar calendar = new Calendar();

  @Step("Add new spending: '{description}'")
  public MainPage setNewSpendingDescription(String description) {
    descriptionInput.val(description);
    saveBtn.click();
    return new MainPage();
  }

  @Step("Add new spending")
  public MainPage fillSpending(double amount, String category, java.util.Calendar cal, String description)
      throws InterruptedException {
    amountInput.setValue(String.valueOf(amount));
    currency.click();
    rub.click();
    categoryInput.setValue(category);
    calendar.selectDateInCalendar(cal.getTime());
    descriptionInput.val(description);
    saveBtn.click();
    return new MainPage();
  }

  @Step("Click add spending")
  public MainPage save() {
    saveBtn.click();
    return new MainPage();
  }
}
