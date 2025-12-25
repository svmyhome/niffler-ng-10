package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import com.codeborne.selenide.SelenideElement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Calendar {
//  private final SelenideElement self = $("[name='date']");
  private final SelenideElement self = $("//*[text()='Date']").parent();


  public Calendar selectDateInCalendar(Date date) {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    String dateString = sdf.format(date);

    $("[name='date']").setValue("03/20/2024");
    executeJavaScript(
        "arguments[0].dispatchEvent(new Event('change'));",
        $("[name='date']")
    );
//    $("[name='date']").click();
//    $("[name='date']").val(dateString);
//    $("[name='date']").type("3");
//    self.$("//button[@type='button']").click();
    return new Calendar();
  }
}

////*[text()='Date']/parent::*//button[@type='button']
//public class Calendar {
//  public Calendar selectDateInCalendar(Date date) {}
//}
