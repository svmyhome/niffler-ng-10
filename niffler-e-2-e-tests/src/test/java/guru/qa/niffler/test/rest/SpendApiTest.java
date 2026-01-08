package guru.qa.niffler.test.rest;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.service.SpendApiClient;
import guru.qa.niffler.service.SpendClient;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.util.Date;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("API")
@Feature("Categories and Spendings")
@Story("Spending Management")
public class SpendApiTest {

  private final SpendClient spendApi = new SpendApiClient();

  @Test
  @DisplayName("API: Should creat new spend")
  public void shouldCreateSpendFromApi() {
    CategoryEntity categoryEntity = new CategoryEntity();
    categoryEntity.setName("1111111222qaa");
    categoryEntity.setUsername("duck");
    categoryEntity.setArchived(false);

    SpendEntity entity = new SpendEntity();
    entity.setUsername("duck");
    entity.setCurrency(CurrencyValues.RUB);
    entity.setSpendDate(new Date());
    entity.setAmount(123.0);
    entity.setDescription("903");
    entity.setCategory(categoryEntity);
    SpendJson spendJson = SpendJson.fromEntity(entity);
    spendApi.create(spendJson);
  }

  @Test
  @DisplayName("API: Should creat new category")
  public void shouldCreateCategoryFromApi() {
    CategoryEntity categoryEntity = new CategoryEntity();
    categoryEntity.setName("1298761543218");
    categoryEntity.setUsername("mouse");
    categoryEntity.setArchived(false);

    spendApi.createCategory(CategoryJson.fromEntity(categoryEntity));
  }

  @Test
  @DisplayName("API: Should find existing spending by ID and username")
  public void shouldFindSpendingByIdAndUsernameFromApi() {
    SpendApiClient spendApiClient = new SpendApiClient();
    spendApiClient.findByIdAndUsername(
        UUID.fromString("5a39bd6e-ebe9-4dff-a98b-535fcf844969"),
        "duck"
    );
  }

  @Test
  @DisplayName("API: Should find existing category by username and name")
  public void shouldFindCategoryByUsernameAndSpendNameFromApi() {
    spendApi.findCategoryByUsernameAndSpendName(
        "Машина", "duck");
  }
}
