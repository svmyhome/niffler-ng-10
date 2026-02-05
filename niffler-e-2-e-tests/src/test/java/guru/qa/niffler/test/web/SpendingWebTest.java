package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.assertions.ImageAssertions;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.constants.Currency;
import guru.qa.niffler.data.constants.DataFilterValues;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.spend.Bubble;
import guru.qa.niffler.model.spend.CurrencyValues;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.component.SpendingsHistoryTable;
import guru.qa.niffler.page.component.StatComponent;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Epic("UI")
@Feature("Categories and Spendings")
@Story("Spending Management")
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
    @ApiLogin
    @Test
    @DisplayName("User should be able to edit spending description")
    void spendingDescriptionShouldBeEditedByTableAction(UserJson user) {
        final String newDescription = "Обучение Niffler Next Generation 10";

        Selenide.open(MainPage.URL, MainPage.class)
                .editSpending(user.testData().spendings().getFirst().description())
                .setNewSpendingDescription(newDescription)
                .checkThatTableContains(newDescription);
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
    @ApiLogin
    @Test
    @DisplayName("New spending description should be visible")
    void spendingDescriptionShouldBeVisible(UserJson user) {
        final String newDescription = "Обучение Niffler Next Generation 10";

        Selenide.open(MainPage.URL, MainPage.class)
                .historyOfSpendingIsVisible()
                .editSpending(user.testData().spendings().getFirst().description())
                .setNewSpendingDescription(newDescription)
                .searchSpending(newDescription)
                .checkThatTableContains(newDescription);
    }

    @User
    @ApiLogin
    @Test
    @DisplayName("User should be able create new spending")
    void shouldCreateNewSpendingWithValidData() {
        final String newDescription = "qaz";
        Calendar cal = Calendar.getInstance();
        cal.set(2011, Calendar.NOVEMBER, 12);

        Selenide.open(MainPage.URL, MainPage.class)
                .historyOfSpendingIsVisible()
                .openNewSpending()
                .fillSpending(123.0, Currency.RUB, "QAZ", cal, newDescription)
                .checkSnackBarText("New spending is successfully created")
                .checkThatTableContains(newDescription);
    }

    @User(
            spendings = {@Spending(
                    category = "Машина",
                    amount = 89900,
                    currency = CurrencyValues.RUB,
                    description = "Обучение Niffler 2.0 юбилейный поток!"
            )}
    )
    @ApiLogin
    @Test
    @DisplayName("User should be able delete spending")
    void spendingShouldBeDeleted() {
        Selenide.open(MainPage.URL, MainPage.class)
                .historyOfSpendingIsVisible()
                .deleteSpendingFromTable("Обучение Niffler 2.0 юбилейный поток!")
                .checkSpendIsDeleted();
    }

    @User(
            spendings = {@Spending(
                    category = "Машина",
                    amount = 89900,
                    currency = CurrencyValues.RUB,
                    description = "Обучение Niffler 2.0 юбилейный поток!"
            )}
    )
    @ApiLogin
    @Test
    @DisplayName("User should be able to edit all fields of a spending")
    void userShouldBeAbleToEditAllSpendingFields() {
        final String newDescription = "Обучение Niffler 2.0 юбилейный поток!";
        Calendar date = Calendar.getInstance();
        date.set(2024, 11, 12);

        Selenide.open(MainPage.URL, MainPage.class)
                .historyOfSpendingIsVisible()
                .editSpendingFromTable(newDescription)
                .fillSpending(123.0, Currency.RUB, "QAZ", date, "qaz")
                .checkSpendingDescriptionTable(newDescription);
    }

    @User(
            spendings = {@Spending(
                    category = "Машина",
                    amount = 89900,
                    currency = CurrencyValues.RUB,
                    description = "Обучение Niffler 2.0 юбилейный поток!"
            )}
    )
    @ApiLogin
    @Test
    @DisplayName("User should be able select spending by period")
    void userShouldBeAbleSelectPeriodSpending() {
        Selenide.open(MainPage.URL, MainPage.class)
                .historyOfSpendingIsVisible()
                .selectPeriodSpendingFromTable(DataFilterValues.WEEK)
                .checkSelectPeriodSpendingFromTable(DataFilterValues.WEEK);
    }

    @User(
            spendings = {@Spending(
                    category = "Машина",
                    amount = 89900,
                    currency = CurrencyValues.RUB,
                    description = "Обучение Niffler 2.0 юбилейный поток!"
            )}
    )
    @ApiLogin
    @Test
    @DisplayName("Spending should be displayed correctly in the table")
    void spendingShouldBeVisibleFromTable() {
        String today = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH));

        Selenide.open(MainPage.URL, MainPage.class)
                .historyOfSpendingIsVisible()
                .checkTableContent("Машина", "89900", String.valueOf(CurrencyValues.RUB),
                        "Обучение Niffler 2.0 юбилейный поток!", today)
                .checkTableSize(1);
    }

    @User(
            spendings = {@Spending(
                    category = "Машина",
                    amount = 300,
                    currency = CurrencyValues.RUB,
                    description = "На ТО"
            ),
                    @Spending(
                            category = "Книги",
                            amount = 200,
                            currency = CurrencyValues.RUB,
                            description = "Обучение Niffler 2.0 юбилейный поток!"
                    )}
    )
    @ApiLogin
    @ScreenShotTest(value = "img/spendings.png")
    @DisplayName("Spending chart should correctly display test data")
    void spendingChartShouldDisplayTestDataCorrectly(BufferedImage expected) {
        StatComponent statComponent = Selenide.open(MainPage.URL, MainPage.class)
                .historyOfSpendingIsVisible()
                .getStatComponent();

        Selenide.sleep(4000);
        ImageAssertions.checkScreenshotMatches(expected,
                statComponent.getChartScreenshot());
        statComponent.checkBubbles(
                new Bubble(Color.YELLOW, "Машина 300 ₽"),
                new Bubble(Color.GREEN, "Книги 200 ₽")
        );
    }

    @User(
            spendings = {@Spending(
                    category = "Машина",
                    amount = 300,
                    currency = CurrencyValues.RUB,
                    description = "На ТО"
            ),
                    @Spending(
                            category = "Книги",
                            amount = 200,
                            currency = CurrencyValues.RUB,
                            description = "Обучение Niffler 2.0 юбилейный поток!"
                    )}
    )
    @ApiLogin
    @ScreenShotTest(value = "img/spending.png")
    @DisplayName("Spending chart should refresh correctly after deletion")
    void spendingChartShouldRefreshAfterDeletion(BufferedImage expected) {
        StatComponent statComponent = Selenide.open(MainPage.URL, MainPage.class)
                .historyOfSpendingIsVisible()
                .deleteSpendingFromTable("На ТО")
                .getStatComponent();

        Selenide.sleep(4000);
        ImageAssertions.checkScreenshotMatches(expected,
                statComponent.getChartScreenshot());
        statComponent.checkBubbles(new Bubble(Color.YELLOW, "Книги 200 ₽"));
    }

    @User(
            spendings = {@Spending(
                    category = "Машина",
                    amount = 300,
                    currency = CurrencyValues.RUB,
                    description = "На ТО"
            ),
                    @Spending(
                            category = "Книги",
                            amount = 200,
                            currency = CurrencyValues.RUB,
                            description = "Обучение Niffler 2.0 юбилейный поток!"
                    )}
    )
    @ApiLogin
    @ScreenShotTest(value = "img/updateSpending.png")
    @DisplayName("Spending chart should update correctly")
    void spendingChartShouldDisplayCorrectlyAfterUpdate(BufferedImage expected) {
        StatComponent statComponent = Selenide.open(MainPage.URL, MainPage.class)
                .historyOfSpendingIsVisible()
                .editSpending("На ТО")
                .setNewAmount(1000.0)
                .getStatComponent();

        Selenide.sleep(4000);
        ImageAssertions.checkScreenshotMatches(expected,
                statComponent.getChartScreenshot());
        statComponent.checkBubbles(
                new Bubble(Color.YELLOW, "Машина 1000 ₽"),
                new Bubble(Color.GREEN, "Книги 200 ₽")
        );
    }

    @User(
            spendings = {@Spending(
                    category = "Машина",
                    amount = 300,
                    currency = CurrencyValues.RUB,
                    description = "На ТО"
            ),
                    @Spending(
                            category = "Книги",
                            amount = 200,
                            currency = CurrencyValues.RUB,
                            description = "Обучение Niffler 2.0 юбилейный поток!"
                    )}
    )
    @ApiLogin
    @ScreenShotTest(value = "img/updateSpending.png")
    @DisplayName("Spending chart should display correctly any order")
    void spendingChartShouldDisplayCorrectlyAnyOrder() {
        StatComponent statComponent = Selenide.open(MainPage.URL, MainPage.class)
                .historyOfSpendingIsVisible()
                .editSpending("На ТО")
                .setNewAmount(1000.0)
                .getStatComponent();

        statComponent.checkBubblesInAnyOrder(
                new Bubble(Color.YELLOW, "Машина 1000 ₽"),
                new Bubble(Color.GREEN, "Книги 200 ₽")
        );
    }

    @User(
            spendings = {@Spending(
                    category = "Машина",
                    amount = 300,
                    currency = CurrencyValues.RUB,
                    description = "На ТО"
            ),
                    @Spending(
                            category = "Книги",
                            amount = 200,
                            currency = CurrencyValues.RUB,
                            description = "Обучение Niffler 2.0 юбилейный поток!"
                    )}
    )
    @ApiLogin
    @ScreenShotTest(value = "img/updateSpending.png")
    @DisplayName("Spending chart should display contains spending")
    void spendingChartShouldDisplayContainsSpending() {
        StatComponent statComponent = Selenide.open(MainPage.URL, MainPage.class)
                .historyOfSpendingIsVisible()
                .editSpending("На ТО")
                .setNewAmount(1000.0)
                .getStatComponent();

        statComponent.checkBubblesContains(
                new Bubble(Color.YELLOW, "Машина 1000 ₽"),
                new Bubble(Color.GREEN, "Книги 200 ₽")
        );
    }

    @User(
            spendings = {@Spending(
                    category = "Машина",
                    amount = 300,
                    currency = CurrencyValues.RUB,
                    description = "На ТО"
            ),
                    @Spending(
                            category = "Книги",
                            amount = 200,
                            currency = CurrencyValues.RUB,
                            description = "Обучение Niffler 2.0 юбилейный поток!"
                    )}
    )
    @ApiLogin
    @Test
    @DisplayName("History of spendings  should display all spendings")
    void historySpendingsShouldDisplayUserSpending(UserJson user) {
        SpendingsHistoryTable spendingTable = Selenide.open(MainPage.URL, MainPage.class)
                .historyOfSpendingIsVisible()
                .getSpendingsHistoryComponent();

        spendingTable.checkSpends(
                user.testData().spendings().getFirst(),
                user.testData().spendings().get(1));
    }
}