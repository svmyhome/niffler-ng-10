package guru.qa.niffler.page.component;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.data.constants.DataFilterValues;
import io.qameta.allure.Step;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SpendingTable extends BaseComponent<SpendingTable> {

  private final SelenideElement searchPeriod = self.$("#period"),
      editSpending = $("[aria-label*='Edit spending']"),
      selectAllRows = $("[aria-label='select all rows']"),
      deleteSpendingButton = $("#delete"),
      deleteSubmitButton = $(".MuiDialogActions-root").$(byText("Delete")),
      searchSpending = self.$("[placeholder='Search']");
  private final ElementsCollection rows = $$("tbody tr");

  public SpendingTable() {
    super($("#spendings .MuiTableContainer-root"));
  }


  @Step("Select search period {period}")
  public @Nonnull SpendingTable selectPeriod(DataFilterValues period) {
    searchPeriod.click();
    $(String.format("[data-value='%s']", period)).click();
    return this;
  }

  @Step("Edit spending")
  public @Nonnull SpendingTable editSpending(String description) {
    searchSpendingByDescription(description);
    editSpending.click();
    return this;
  }

  @Step("Delete spending")
  public @Nonnull SpendingTable deleteSpending(String description) {
    searchSpendingByDescription(description);
    selectAllRows.click();
    deleteSpendingButton.click();
    deleteSubmitButton.click();
    return this;
  }

  @Step("Search spending by description")
  public @Nonnull SpendingTable searchSpendingByDescription(String description) {
    searchSpending.val(description).pressEnter();
    return this;
  }

  @Step("Check table values")
  public @Nonnull SpendingTable checkTableContains(String... expectedSpends) {
    rows.shouldHave(texts(expectedString(expectedSpends)));
    return this;
  }

  @Step("Check table size")
  public SpendingTable checkTableSize(int expectedSize) {
    rows.shouldHave(CollectionCondition.size(expectedSize));
    return this;
  }

  @Step("Check period is selected")
  public SpendingTable checkPeriodIsSelected(DataFilterValues period) {
    searchPeriod.shouldHave(text(period.getPeriod()));
    return this;
  }

  private @Nonnull
  static String expectedString(String[] expectedSpends) {
    return Arrays.stream(expectedSpends)
        .map(SpendingTable::convertCurrency)
        .collect(Collectors.joining(" ")).trim();
  }

  private @Nonnull
  static String convertCurrency(String value) {
    return switch (value) {
      case "RUB" -> "₽";
      case "USD" -> "$";
      case "EUR" -> "€";
      case "KZT" -> "₸";
      default -> value;
    };
  }
}