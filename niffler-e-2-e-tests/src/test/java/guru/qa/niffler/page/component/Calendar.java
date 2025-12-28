package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import com.codeborne.selenide.SelenideElement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


public class Calendar {

  //  private final SelenideElement self = $("[name='date']");
//  private final SelenideElement self = $("//*[text()='Date']").parent();
  private final SelenideElement self = $("[name='date']").parent();


  public Calendar selectDateInCalendar(Date date) {
    LocalDate localDate = date.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate();

    int day = localDate.getDayOfMonth();
    int year = localDate.getYear();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    String dateString = sdf.format(date);
    date.getTime();
    self.$("[aria-label*='Choose date']").click();
    $("[aria-label*='calendar view is open']").click();
    $x(String.format("//button[text()='%d']", year)).click();
    $("[data-testid='ArrowLeftIcon']").click();
    $x(String.format("//button[text()='%d']", day)).click();
    return new Calendar();
  }
}

/// /*[text()='Date']/parent::*//button[@type='button']
//public class Calendar {
//  public Calendar selectDateInCalendar(Date date) {}
//}
