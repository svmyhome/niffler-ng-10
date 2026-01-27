package guru.qa.niffler.page.component;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.condition.SpendingConditions;
import guru.qa.niffler.data.constants.DataFilterValues;
import guru.qa.niffler.model.spend.RowSpend;
import guru.qa.niffler.model.spend.SpendJson;
import io.qameta.allure.Step;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SpendingsHistoryTable extends BaseComponent<SpendingsHistoryTable> {

    private final SelenideElement searchPeriod,
            editSpending,
            selectAllRows,
            deleteSpendingButton,
            deleteSubmitButton,
            searchSpending;
    private final ElementsCollection spendingRows;


    public SpendingsHistoryTable(SelenideDriver driver, SelenideElement self) {
        super(driver, driver.$("form.MuiBox-root"));
        this.searchPeriod = driver.$("#period");
        this.editSpending = driver.$("[aria-label*='Edit spending']");
        this.selectAllRows = driver.$("[aria-label='select all rows']");
        this.deleteSpendingButton = driver.$("#delete");
        this.deleteSubmitButton = driver.$(".MuiDialogActions-root").$(byText("Delete"));
        this.searchSpending = self.$("[placeholder='Search']");
        this.spendingRows = driver.$("tbody").$$("tr");
    }

    private @Nonnull
    static String expectedString(String[] expectedSpends) {
        return Arrays.stream(expectedSpends)
                .map(SpendingsHistoryTable::convertCurrency)
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

    @Step("Select search period {period}")
    public @Nonnull SpendingsHistoryTable selectPeriod(DataFilterValues period) {
        searchPeriod.click();
        $(String.format("[data-value='%s']", period)).click();
        return this;
    }

    @Step("Edit spending")
    public @Nonnull SpendingsHistoryTable editSpending(String description) {
        searchSpendingByDescription(description);
        editSpending.click();
        return this;
    }

    @Step("Delete spending")
    public @Nonnull SpendingsHistoryTable deleteSpending(String description) {
        searchSpendingByDescription(description);
        selectAllRows.click();
        deleteSpendingButton.click();
        deleteSubmitButton.click();
        return this;
    }

    @Step("Search spending by description")
    public @Nonnull SpendingsHistoryTable searchSpendingByDescription(String description) {
        searchSpending.val(description).pressEnter();
        return this;
    }

    @Step("Check table values")
    public @Nonnull SpendingsHistoryTable checkTableContains(String... expectedSpends) {
        spendingRows.shouldHave(texts(expectedString(expectedSpends)));
        return this;
    }

    @Step("Check table size")
    public SpendingsHistoryTable checkTableSize(int expectedSize) {
        spendingRows.shouldHave(CollectionCondition.size(expectedSize));
        return this;
    }

    @Step("Check period is selected")
    public SpendingsHistoryTable checkPeriodIsSelected(DataFilterValues period) {
        searchPeriod.shouldHave(text(period.getPeriod()));
        return this;
    }

    @Step("Check spends from table")
    public @Nonnull SpendingsHistoryTable checkSpendsContains(RowSpend... expectedSpends) {
        spendingRows.should(SpendingConditions.spendsContains(expectedSpends));
        return this;
    }

    @Step("Check spends from table")
    public @Nonnull SpendingsHistoryTable checkSpends(SpendJson... expectedSpends) {
        spendingRows.should(SpendingConditions.spends(expectedSpends));
        return this;
    }
}