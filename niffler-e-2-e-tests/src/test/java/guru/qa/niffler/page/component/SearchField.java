package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

public class SearchField {
  private final SelenideElement self = $("form.MuiBox-root");

  @Step("Fill search field {value}")
  public void fill(String value) {
    self.$("[placeholder='Search']").shouldBe(visible).setValue(value).pressEnter();
  }

  @Step("Clear search field")
  public void clear() {
    self.$("#input-clear").shouldBe(visible).click();
  }
}