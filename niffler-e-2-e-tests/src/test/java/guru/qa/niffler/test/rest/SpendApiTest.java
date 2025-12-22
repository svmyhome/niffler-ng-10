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

//  @Test
//  public void updateSpendFromApi() {
//    CategoryEntity categoryEntity = new CategoryEntity();
//    categoryEntity.setName("3edc");
//    categoryEntity.setUsername("cat");
//    categoryEntity.setArchived(false);
//
//    SpendEntity entity = new SpendEntity();
//    entity.setUsername("cat");
//    entity.setCurrency(CurrencyValues.RUB);
//    entity.setSpendDate(new Date());
//    entity.setAmount(123.0);
//    entity.setDescription("908");
//    entity.setCategory(categoryEntity);
//    SpendJson spendJson = SpendJson.fromEntity(entity);
//    SpendJson result = spendApi.update(spendJson);
//    System.out.println(result.id());
//    System.out.println(result.description());
//  }
//  @Test
//  public void updateCategoryFromApi() {
//    CategoryEntity categoryEntity = new CategoryEntity();
//    categoryEntity.setName("1298761543218");
//    categoryEntity.setUsername("mouse");
//    categoryEntity.setArchived(true);
//
//    CategoryJson categoryJson = CategoryJson.fromEntity(categoryEntity);
//    CategoryJson result = spendApi.updateCategory(categoryJson);
//    System.out.println(result.id());
//    System.out.println(result.name());
//  }

//  @Test
//  public void findSpendByIdFromApi() {
//    Optional<SpendJson> byId = spendApi.findById(
//        UUID.fromString("da13d8e9-da7c-4883-b350-35716bec948e"));
//    System.out.println(byId.get().description());
//  }

  @Test
  public void CategoryByUsernameAndSpendNameFromApi() {
    Optional<CategoryJson> categoryByUsernameAndSpendName = spendApi.findCategoryByUsernameAndSpendName(
        "Машина", "duck");
    System.out.println(categoryByUsernameAndSpendName.get().name());
    System.out.println(categoryByUsernameAndSpendName.get().username());
    System.out.println(categoryByUsernameAndSpendName.get().id());
  }


}
