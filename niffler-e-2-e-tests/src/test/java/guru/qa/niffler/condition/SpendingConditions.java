package guru.qa.niffler.condition;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;
import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.spend.CurrencyValues;
import guru.qa.niffler.model.spend.DateFormatterUtil;
import guru.qa.niffler.model.spend.RowSpend;
import guru.qa.niffler.model.spend.SpendJson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.commons.lang.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@ParametersAreNonnullByDefault
public class SpendingConditions {

    public static WebElementsCondition spendsContainsAnyOrder(RowSpend... expectedSpends) {
        return new WebElementsCondition() {

            private final String expectedResult = Arrays.stream(expectedSpends)
                    .map(spend -> spend.category() + ": " + spend.amount() + ": " + spend.description() + ": "
                            + spend.date())
                    .collect(Collectors.joining(", ", "[", "]"));

            @Override
            @Nonnull
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedSpends)) {
                    throw new IllegalArgumentException("No expected spends given");
                }
                if (expectedSpends.length > elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)",
                            expectedSpends.length, elements.size());
                    List<String> actualSpends = new ArrayList<>();
                    for (final WebElement spendsRows : elements) {
                        final List<WebElement> elementsToCheck = spendsRows.findElements(By.cssSelector("td"));
                        final String actualCategory = elementsToCheck.get(1).getText();
                        final String actualAmount = elementsToCheck.get(2).getText();
                        final String actualDescription = elementsToCheck.get(3).getText();
                        final String actualDate = elementsToCheck.get(4).getText();
                        actualSpends.add(
                                actualCategory + ": " + actualAmount + ": " + actualDescription + ": "
                                        + actualDate);
                    }
                    return rejected(message, actualSpends.toString());
                }

                List<RowSpend> actualSpends = new ArrayList<>();
                for (final WebElement spendsRows : elements) {
                    final List<WebElement> elementsToCheck = spendsRows.findElements(By.cssSelector("td"));
                    final String actualCategory = elementsToCheck.get(1).getText();
                    final String[] amountCurrency = elementsToCheck.get(2).getText().split(" ");
                    final double actualAmount = Double.parseDouble(amountCurrency[0]);
                    final String actualCurrency = CurrencyValues.fromCurrency(amountCurrency[1]).name();
                    final String actualDescription = elementsToCheck.get(3).getText();
                    String dateString = elementsToCheck.get(4).getText();
                    actualSpends.add(new RowSpend(
                            actualCategory,
                            actualAmount,
                            actualCurrency,
                            actualDescription,
                            dateString
                    ));
                }
                Set<RowSpend> spends = new HashSet<>();
                for (final RowSpend expectedSpend : expectedSpends) {
                    String formattedDate = DateFormatterUtil.formatDate(
                            expectedSpend.date()
                    );
                    spends.add(
                            new RowSpend(expectedSpend.category(), expectedSpend.amount(), expectedSpend.currency(), expectedSpend.description(), formattedDate));
                }
                if (actualSpends.containsAll(spends)) {
                    return accepted();
                } else {
                    final String message = String.format(
                            "List spends mismatch (expected: %s, actual: %s)",
                            expectedResult,
                            actualSpends
                    );
                    return rejected(message, actualSpends.toString());
                }
            }

            @Override
            @Nonnull
            public String toString() {
                return expectedResult;
            }
        };
    }

    public static WebElementsCondition spends(SpendJson... expectedSpends) {
        return new WebElementsCondition() {

            private final List<SpendJson> expectedSpendList = Arrays.asList(expectedSpends);
            private final String expectedResult = formatExpectedSpends(expectedSpendList);

            @Override
            @Nonnull
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (expectedSpendList.isEmpty()) {
                    throw new IllegalArgumentException("No expected spends given");
                }
                if (expectedSpendList.size() > elements.size()) {
                  return handleSizeMismatch(elements);
                }
                List<String> actualSpendsList = new ArrayList<>();
                boolean passed = true;
                for (int i = 0; i < elements.size(); i++) {
                    List<String> currentSpendErrors = new ArrayList<>();
                    SpendJson expectedSpend = expectedSpendList.get(i);
                    final List<WebElement> cells = elements.get(i).findElements(By.cssSelector("td"));
                    if (!cells.get(1).getText().equals(expectedSpend.category().name())) {
                        String message = String.format(
                                "Spend category mismatch (expected: %s, actual: %s)",
                                expectedSpend.category().name(), cells.get(1).getText()
                        );
                        passed = false;
                        currentSpendErrors.add(message);
                    }
                    final String[] amountCurrency = cells.get(2).getText().split(" ");
                    double actualAmount = Double.parseDouble(amountCurrency[0]);
                    if (actualAmount!=expectedSpend.amount()) {
                        String message = String.format(
                                "Spend amount mismatch (expected: %s, actual: %s)",
                                expectedSpend.amount(), actualAmount
                        );
                        passed = false;
                        currentSpendErrors.add(message);
                    }
                    final String actualCurrency = CurrencyValues.fromCurrency(amountCurrency[1]).name();
                    if (!actualCurrency.equals(expectedSpend.currency().name())) {
                        String message = String.format(
                                "Spend currency mismatch (expected: %s, actual: %s)",
                                expectedSpend.currency().name(), actualCurrency
                        );
                        passed = false;
                        currentSpendErrors.add(message);
                    }
                    if (!cells.get(3).getText().equals(expectedSpend.description())) {
                        String message = String.format(
                                "Spend description mismatch (expected: %s, actual: %s)",
                                expectedSpend.description(), cells.get(3).getText()
                        );
                        passed = false;
                        currentSpendErrors.add(message);
                    }
                    String formattedDate = DateFormatterUtil.formatDate(
                            expectedSpend.spendDate().toString()
                    );
                    if (!cells.get(4).getText().equals(formattedDate)) {
                        String message = String.format(
                                "Spend date mismatch (expected: %s, actual: %s)",
                                formattedDate, cells.get(4).getText()
                        );
                        passed = false;
                        currentSpendErrors.add(message);
                    }
                    if (!currentSpendErrors.isEmpty()) {
                        actualSpendsList.add(currentSpendErrors.toString());
                    } else {
                        actualSpendsList.add(formatActualSpend(cells));
                    }
                }
                if (!passed) {
                    final String actualSpends = actualSpendsList.toString();
                    final String message = String.format("List spends mismatch (expected: %s, actual: %s)",
                            expectedResult, actualSpends);
                    return rejected(message, actualSpendsList);
                }
                return accepted();
            }

            @Override
            @Nonnull
            public String toString() {
                return expectedResult;
            }

            @Nonnull
            private String formatExpectedSpends(List<SpendJson> spends) {
                return spends.stream()
                        .map(this::formatSpend)
                        .collect(Collectors.joining(", ", "[", "]"));
            }

            @Nonnull
            private String formatSpend(SpendJson spend) {
                return spend.category().name() + ": " +
                        spend.amount() + ": " +
                        spend.description() + ": " +
                        spend.spendDate();
            }

            @Nonnull
            private CheckResult handleSizeMismatch(List<WebElement> elements) {
                final String message = String.format("List size mismatch (expected: %s, actual: %s)",
                        expectedSpendList.size(), elements.size());
                List<String> actualSpends = new ArrayList<>();
                for (final WebElement spendsRows : elements) {
                    final List<WebElement> elementsToCheck = spendsRows.findElements(By.cssSelector("td"));
                    final String actualCategory = elementsToCheck.get(1).getText();
                    final String actualAmount = elementsToCheck.get(2).getText();
                    final String actualDescription = elementsToCheck.get(3).getText();
                    final String actualDate = elementsToCheck.get(4).getText();
                    actualSpends.add(
                            actualCategory + ": " + actualAmount + ": " + actualDescription + ": "
                                    + actualDate);
                }
                return rejected(message, actualSpends.toString());
            }

            @Nonnull
            private String formatActualSpend(List<WebElement> cells) {
                return cells.get(1).getText() + ": " +
                        cells.get(2).getText() + ": " +
                        cells.get(3).getText() + ": " +
                        cells.get(4).getText();
            }
        };
    }

}
