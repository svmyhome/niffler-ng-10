package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.visible;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SearchField extends BaseComponent<SearchField> {

  private final SelenideElement self;
  private final SelenideElement searchField,
      clearSearchField;

    public SearchField(SelenideDriver driver, SelenideElement self) {
        super(driver, self);
        this.self = driver.$("form.MuiBox-root");
        this.searchField = self.$("[placeholder='Search']");
        this.clearSearchField = self.$("#input-clear");
    }

//    public SearchField() {
//    super($("form.MuiBox-root"));
//  }

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