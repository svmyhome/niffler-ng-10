package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class EditSpendingPage {
    private final SelenideElement descriptionInput = $("#description");
    private final SelenideElement saveBtn = $("#save");

    @Step("Add new spending: '{description}'")
    public MainPage setNewSpendingDescription(String description) {
        descriptionInput.val(description);
        saveBtn.click();
        return new MainPage();
    }

    @Step("Click add spending")
    public MainPage save() {
        saveBtn.click();
        return new MainPage();
    }
}
