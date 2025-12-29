package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static java.lang.Thread.sleep;
import com.codeborne.selenide.SelenideElement;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;


public class Calendar {

  //  private final SelenideElement self = $("[name='date']");
//  private final SelenideElement self = $("//*[text()='Date']").parent();
//  private final SelenideElement self = $("[name='date']").parent();
//
//
//  public Calendar selectDateInCalendar(Date date) {
//    LocalDate localDate = date.toInstant()
//        .atZone(ZoneId.systemDefault())
//        .toLocalDate();
//
//    int day = localDate.getDayOfMonth();
//    int year = localDate.getYear();
//    int expectedMonth = localDate.getMonthValue();
//    self.$("[aria-label*='Choose date']").click();
//    $("[aria-label*='calendar view is open']").click();
//    $x(String.format("//button[text()='%d']", year)).click();
//    int currentMonth = getCurrentMonth();
//    while (currentMonth != expectedMonth) {
//      if (currentMonth > expectedMonth) {
//        $("[data-testid='ArrowLeftIcon']").click();
//        currentMonth = getCurrentMonth();
//      } else {
//        $("[data-testid='ArrowRightIcon']").click();
//        currentMonth = getCurrentMonth();
//      }
//    }
//    $x(String.format("//button[text()='%d']", day)).click();
//    return this;
//  }
//
//  private static int getCurrentMonth() {
//    String currentMonthYear = $(".MuiPickersCalendarHeader-label").getText();
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH);
//    YearMonth yearMonth = YearMonth.parse(currentMonthYear, formatter);
//    return yearMonth.getMonthValue();
//  }

  private final SelenideElement self = $("[name='date']").parent();
  private int debugCounter = 0;

  public Calendar selectDateInCalendar(Date date) throws InterruptedException {
    System.out.println("=== START selectDateInCalendar, вызов #" + (++debugCounter) + " ===");

    try {
      LocalDate localDate = date.toInstant()
          .atZone(ZoneId.systemDefault())
          .toLocalDate();

      int day = localDate.getDayOfMonth();
      int year = localDate.getYear();
      int expectedMonth = localDate.getMonthValue();

      System.out.println("Параметры: день=" + day + ", год=" + year + ", месяц=" + expectedMonth);

      self.$("[aria-label*='Choose date']").click();
      sleep(1000);

      $("[aria-label*='calendar view is open']").click();
      sleep(500);

      $x(String.format("//button[text()='%d']", year)).click();
      sleep(500);

      int currentMonth = getCurrentMonth();
      System.out.println("Текущий месяц после выбора года: " + currentMonth);

      while (currentMonth != expectedMonth) {
        System.out.println("Листаем: текущий=" + currentMonth + ", нужный=" + expectedMonth);
        if (currentMonth > expectedMonth) {
          $("[data-testid='ArrowLeftIcon']").click();
        } else {
          $("[data-testid='ArrowRightIcon']").click();
        }
        sleep(500);
        currentMonth = getCurrentMonth();
      }

      System.out.println("Найден нужный месяц: " + currentMonth);
      System.out.println("Кликаем на день: " + day);

      $x(String.format("//button[text()='%d']", day)).click();
      sleep(1000);

      System.out.println("День выбран, возвращаем this");
      System.out.println("=== END selectDateInCalendar ===");

      return this;

    } catch (Exception e) {
      System.err.println("ОШИБКА в selectDateInCalendar: " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }

  private int getCurrentMonth() {
//    System.out.println(">>> ВХОД в getCurrentMonth");

    String currentMonthYear = $(".MuiPickersCalendarHeader-label").getText();
//    System.out.println("Текст элемента: '" + currentMonthYear + "'");

//     Проверяем не пустая ли строка
//    if (currentMonthYear == null || currentMonthYear.trim().isEmpty()) {
//      System.err.println("ПРЕДУПРЕЖДЕНИЕ: Пустая строка в getCurrentMonth()!");
//      System.err.println(
//          "HTML элемента: " + $(".MuiPickersCalendarHeader-label").getAttribute("outerHTML"));
//      throw new RuntimeException("Пустая строка месяца");
//    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH);
    YearMonth yearMonth = YearMonth.parse(currentMonthYear, formatter);
    int result = yearMonth.getMonthValue();

//    System.out.println("<<< ВЫХОД из getCurrentMonth, результат: " + result);
    return result;
  }
}
