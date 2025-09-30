package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {
    public ProfilePage profilePage = new ProfilePage();

    private final ElementsCollection tableRows = $$("#spendings tr");
    private final SelenideElement mainPage = $("#root");
    private final SelenideElement menu = $("[aria-label='Menu']");
    private final SelenideElement profile = $("[href='/profile']");

    public EditSpendingPage editSpending(String description) {
        tableRows.find(text(description)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public MainPage checkThatTableContains(String description) {
        tableRows.find(text(description)).should(visible);
        return this;
    }

    public void mainPageShouldBeDisplayed() {
        mainPage.shouldHave(text("Statistics"));
        mainPage.shouldHave(text("History of Spendings"));
    }

    public MainPage openMenu() {
        menu.click();
        return this;
    }

    public MainPage openProfile() {
        profile.click();
        return this;
    }

}
