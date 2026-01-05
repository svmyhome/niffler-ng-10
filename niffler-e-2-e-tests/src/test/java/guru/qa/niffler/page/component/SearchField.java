package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BasePage;
import io.qameta.allure.Step;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SearchField extends BasePage<SearchField> {

  private final SelenideElement self = $("form.MuiBox-root");
  private final SelenideElement searchField = self.$("[placeholder='Search']"),
      clearSearchField = self.$("#input-clear");

  @Step("Fill search field {query}")
  public @Nonnull SearchField search(String query) {
    searchField.shouldBe(visible).setValue(query).pressEnter();
    return this;
  }

  @Step("Clear search field")
  public @Nonnull SearchField clearIfNotEmpty() {
    clearSearchField.shouldBe(visible).click();
    return this;
  }

  @Step("Check search field is empty")
  public @Nonnull SearchField checkSearchFieldEmpty() {
    searchField.shouldHave(empty);
    return this;
  }
}