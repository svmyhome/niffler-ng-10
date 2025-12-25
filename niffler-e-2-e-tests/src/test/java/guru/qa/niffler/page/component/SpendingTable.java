package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Selenide.$;
import com.codeborne.selenide.SelenideElement;

public class SpendingTable {
    private final SelenideElement self = $("#spendings .MuiTableContainer-root");

  public SpendingTable searchSpendingByDescription(String description) {
    self.$("[placeholder='Search']").val(description);
    return this;
  }
}


//public class SpendingTable {
//  public SpendingTable selectPeriod(DataFilterValues period) { }
//  public EditSpendingPage editSpending(String description) { }
//  public SpendingTable deleteSpending(String description) { }
//  public SpendingTable searchSpendingByDescription(String description) {}
//  public SpendingTable checkTableContains(String... expectedSpends) { }
//