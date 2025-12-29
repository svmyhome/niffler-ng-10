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
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SpendingTable {

  private final SelenideElement self = $("#spendings .MuiTableContainer-root");

  private final ElementsCollection rows = $$("tbody tr");
  private final SelenideElement row = $("tbody tr");


  public SpendingTable selectPeriod(DataFilterValues period) {
    $("#period").click();
    $(String.format("[data-value='%s']", period)).click();
    return this;
  }

  public SpendingTable editSpending(String description) {
    searchSpendingByDescription(description);
    $("[aria-label*='Edit spending']").click();
    return this;
  }

  public SpendingTable deleteSpending(String description) {
    searchSpendingByDescription(description);
    $(".PrivateSwitchBase-input[type='checkbox']").click();
    $("#delete").click();
    $(".MuiDialogActions-root").$(byText("Delete")).click();
    return this;
  }

  public SpendingTable searchSpendingByDescription(String description) {
    self.$("[placeholder='Search']").val(description);
    return this;
  }

  public SpendingTable checkTableContains(String... expectedSpends) {
    rows.shouldHave(texts(expectedString(expectedSpends)));
    return this;
  }

  private static String expectedString(String[] expectedSpends) {
    return Arrays.stream(expectedSpends)
        .map(SpendingTable::convertCurrency)
        .collect(Collectors.joining(" ")).trim();
  }

  private static String convertCurrency(String value) {
    return switch (value) {
      case "RUB" -> "₽";
      case "USD" -> "$";
      case "EUR" -> "€";
      case "KZT" -> "₸";
      default -> value;
    };
  }

  public SpendingTable checkTableSize(int expectedSize) {
    rows.shouldHave(CollectionCondition.size(expectedSize));
    return this;
  }

  public SpendingTable checkPeriodIsSelected(DataFilterValues period) {
    $("#period").shouldHave(text(period.getPeriod()));
    return this;
  }
}