package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SearchField {
  private final SelenideElement self = $("form.MuiBox-root");

  @Step("Fill search field {query}")
  public SearchField search(String query) {
    self.$("[placeholder='Search']").shouldBe(visible).setValue(query).pressEnter();
    return this;
  }

  @Step("Clear search field")
  public SearchField clearIfNotEmpty() {
    self.$("#input-clear").shouldBe(visible).click();
    return this;
  }
}