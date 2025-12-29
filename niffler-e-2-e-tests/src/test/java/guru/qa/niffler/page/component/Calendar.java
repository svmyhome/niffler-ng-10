package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import com.codeborne.selenide.SelenideElement;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;


public class Calendar {

  private final SelenideElement self = $("[name='date']").parent();
  private int debugCounter = 0;

  public Calendar selectDateInCalendar(Date date) {
    LocalDate localDate = date.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate();

    int day = localDate.getDayOfMonth();
    int year = localDate.getYear();
    int expectedMonth = localDate.getMonthValue();

    self.$("[aria-label*='Choose date']").click();
    $("[aria-label*='calendar view is open']").click();
    $x(String.format("//button[text()='%d']", year)).click();
    int currentMonth = getCurrentMonth();

    while (currentMonth != expectedMonth) {
      if (currentMonth > expectedMonth) {
        $("[data-testid='ArrowLeftIcon']").click();
      } else {
        $("[data-testid='ArrowRightIcon']").click();
      }
      currentMonth = getCurrentMonth();
    }

    $x(String.format("//button[text()='%d']", day)).click();
    return this;
  }

  private int getCurrentMonth() {
    String currentMonthYear = $(".MuiPickersCalendarHeader-label").shouldBe(visible).getText();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH);
    YearMonth yearMonth = YearMonth.parse(currentMonthYear, formatter);
    int result = yearMonth.getMonthValue();
    return result;
  }
}
