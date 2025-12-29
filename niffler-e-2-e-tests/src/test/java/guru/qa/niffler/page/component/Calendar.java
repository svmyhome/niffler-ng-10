package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
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
public class Calendar {

  private final SelenideElement self = $("[name='date']").parent();
  private final SelenideElement calendarButton = self.$("[aria-label*='Choose date']"),
      selectYearButton = $("[aria-label*='calendar view is open']"),
      previousMonthButton = $("[data-testid='ArrowLeftIcon']"),
      nextMonthButton = $("[data-testid='ArrowRightIcon']");

  @Step("Select date in calendar {date}")
  public @Nonnull Calendar selectDateInCalendar(Date date) {
    Map<String, String> expectedDate = getMonths(date);
    int expectedMonth = Integer.parseInt(expectedDate.get("expectedMonth"));
    String year = expectedDate.get("year");
    String day = expectedDate.get("day");

    calendarButton.click();
    selectYearButton.click();
    $(byText(year)).click();
    selectMonth(expectedMonth);
    $(byText(day)).click();
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
