package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class BaseComponent<T extends BaseComponent<T>> {

    protected final SelenideElement self;
    protected final SelenideDriver driver;


    protected BaseComponent(SelenideDriver driver, SelenideElement self) {
        this.driver = driver;
        this.self = self;
    }

    protected SelenideElement $(String selector) {
        return driver!=null ? driver.$(selector):Selenide.$(selector);
    }

    protected ElementsCollection $$(String selector) {
        return driver!=null ? driver.$$(selector):Selenide.$$(selector);
    }

    public SelenideElement getSelf() {
        return self;
    }
}
