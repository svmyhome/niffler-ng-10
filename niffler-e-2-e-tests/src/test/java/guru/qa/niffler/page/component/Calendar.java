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
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class Calendar {

  private final SelenideElement self = $("[name='date']").parent();

  @Step("Select date in calendar {date}")
  public @Nonnull Calendar selectDateInCalendar(Date date) {
    LocalDate localDate = date.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate();

    int day = localDate.getDayOfMonth();
    int year = localDate.getYear();
    int expectedMonth = localDate.getMonthValue();

    self.$("[aria-label*='Choose date']").click();
    $("[aria-label*='calendar view is open']").click();
    $(byText(String.valueOf(year))).click();
    int currentMonth = getCurrentMonth();

    while (currentMonth != expectedMonth) {
      if (currentMonth > expectedMonth) {
        $("[data-testid='ArrowLeftIcon']").click();
      } else {
        $("[data-testid='ArrowRightIcon']").click();
      }
      currentMonth = getCurrentMonth();
    }

    $(byText(String.valueOf(day))).click();
    return this;
  }

  private @Nonnull int getCurrentMonth() {
    String currentMonthYear = $(".MuiPickersCalendarHeader-label").shouldBe(visible).getText();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH);
    YearMonth yearMonth = YearMonth.parse(currentMonthYear, formatter);
    return yearMonth.getMonthValue();
  }
}
