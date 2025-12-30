package guru.qa.niffler.test.rest;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.service.SpendApiClient;
import guru.qa.niffler.service.SpendClient;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class SpendApiTest {

  private final SpendClient spendApi = new SpendApiClient();

  @Test
  public void createSpendFromApi() {
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
    SpendJson result = spendApi.create(spendJson);
    System.out.println(result.id());
    System.out.println(result.description());
  }

  @Test
  public void createCategoryFromApi() {
    CategoryEntity categoryEntity = new CategoryEntity();
    categoryEntity.setName("1298761543218");
    categoryEntity.setUsername("mouse");
    categoryEntity.setArchived(false);

    CategoryJson result = spendApi.createCategory(CategoryJson.fromEntity(categoryEntity));
    System.out.println(result.id());
    System.out.println(result.name());
  }

  @Test
  public void findSpendByIdAndNameFromApi() {
    SpendApiClient spendApiClient = new SpendApiClient();
    spendApiClient.findByIdAndUsername(
        UUID.fromString("5a39bd6e-ebe9-4dff-a98b-535fcf844969"),
        "duck"
    );
  }

  @Test
  public void CategoryByUsernameAndSpendNameFromApi() {
    Optional<CategoryJson> categoryByUsernameAndSpendName = spendApi.findCategoryByUsernameAndSpendName(
        "Машина", "duck");
    System.out.println(categoryByUsernameAndSpendName.get().name());
    System.out.println(categoryByUsernameAndSpendName.get().username());
    System.out.println(categoryByUsernameAndSpendName.get().id());
  }

}
