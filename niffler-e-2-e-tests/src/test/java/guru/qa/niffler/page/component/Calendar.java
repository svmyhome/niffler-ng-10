package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class Calendar extends BaseComponent<Calendar> {

  private final SelenideElement calendarButton,
      selectYearButton,
      previousMonthButton,
      nextMonthButton;
//
//  public Calendar() {
//    super($("[name='date']").parent());
//  }

    public Calendar(SelenideDriver driver, SelenideElement self) {
        super(driver, driver.$("[name='date']").parent());
        this.calendarButton = self.$("[aria-label*='Choose date']");
                this.selectYearButton = driver.$("[aria-label*='calendar view is open']");
                this.previousMonthButton = driver.$("[data-testid='ArrowLeftIcon']");
                this.nextMonthButton = driver.$("[data-testid='ArrowRightIcon']");
    }

    @Step("Select date in calendar {date}")
  public @Nonnull Calendar selectDateInCalendar(Date date) {
    Map<String, String> expectedDate = getMonths(date);
    int expectedMonth = Integer.parseInt(expectedDate.get("expectedMonth"));
    String year = expectedDate.get("year");
    String day = expectedDate.get("day");

    calendarButton.click();
    selectYearButton.click();
    driver.$(byText(year)).shouldBe(visible).click();
    selectMonth(expectedMonth);
    driver.$(byText(day)).shouldBe(visible).click();
    return this;
  }

  private void selectMonth(int expectedMonth) {
    int currentMonth = getCurrentMonth();
    while (currentMonth != expectedMonth) {
      if (currentMonth > expectedMonth) {
        previousMonthButton.click();
      } else {
        nextMonthButton.click();
      }
      currentMonth = getCurrentMonth();
    }
  }

  private @Nonnull
  Map<String, String> getMonths(Date date) {
    LocalDate localDate = date.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate();
    Map<String, String> months = new HashMap<>();
    months.put("day", String.valueOf(localDate.getDayOfMonth()));
    months.put("year", String.valueOf(localDate.getYear()));
    months.put("expectedMonth", String.valueOf(localDate.getMonthValue()));
    return months;
  }

  private @Nonnull int getCurrentMonth() {
    String currentMonthYear = $(".MuiPickersCalendarHeader-label").shouldBe(visible).getText();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH);
    YearMonth yearMonth = YearMonth.parse(currentMonthYear, formatter);
    return yearMonth.getMonthValue();
  }
}
