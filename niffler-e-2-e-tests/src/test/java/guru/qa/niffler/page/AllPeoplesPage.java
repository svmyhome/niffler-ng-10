package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AllPeoplesPage extends BasePage<AllPeoplesPage> {

    private final SelenideElement allUsersTable;

    private final SearchField searchField;
    private final ElementsCollection sectionHeaders;

    public AllPeoplesPage(SelenideDriver driver) {
        super(driver);
        this.allUsersTable = driver.$("#all");
        this.searchField = new SearchField(driver, driver.$("form.MuiBox-root"));
        this.sectionHeaders = driver.$$("h2");
    }

    private ElementsCollection getAllUsers() {
        return allUsersTable.$$("tr td");
    }

    @Step("Check that user has a new outcoming requests")
    public @Nonnull AllPeoplesPage verifyUserHasNewOutcomingFriendRequest(String friendName) {
        getAllUsers().find(text(friendName)).shouldBe(visible);
        return this;
    }

    @Step("Find friend")
    public @Nonnull AllPeoplesPage searchFriend(String friendName) {
        searchField.search(friendName);
        return this;
    }

    @Step("Clear friend by search")
    public @Nonnull AllPeoplesPage clearFriendBySearch() {
        searchField.clearIfNotEmpty();
        return this;
    }
}
