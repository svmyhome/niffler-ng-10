package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class BasePage<T extends BasePage<T>> {

  protected final Header header;
  protected final SelenideElement snackbar;

    protected BasePage(SelenideDriver driver) {
        this.header = new Header();
        this.snackbar = driver.$(".MuiAlert-message");
    }

    public BasePage() {
        this.header = new Header();
        this.snackbar = Selenide.$(".MuiAlert-message");
    }

    @SuppressWarnings("unchecked")
  public T checkSnackBarText(String message) {
    snackbar.shouldBe(Condition.visible).shouldHave(Condition.text(message));
    return (T) this;
  }

}
