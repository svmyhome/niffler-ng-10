package guru.qa.niffler.page;

import static com.codeborne.selenide.Selenide.$;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class BasePage<T extends BasePage<T>> {

  protected final Header header = new Header();
  protected final SelenideElement snackbar = $(".MuiAlert-message");

  @SuppressWarnings("unchecked")
  public T checkSnackBarText(String message) {
    snackbar.shouldBe(Condition.visible).shouldHave(Condition.text(message));
    return (T) this;
  }

}
