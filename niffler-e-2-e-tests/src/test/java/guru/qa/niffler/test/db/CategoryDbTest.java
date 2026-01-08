package guru.qa.niffler.test.db;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.service.SpendDbClient;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class CategoryDbTest {

  private static final Config CFG = Config.getInstance();

  @Test
  void findCategoryById() {
    SpendDbClient spendDbClient = new SpendDbClient();
    Optional<CategoryJson> categoryById = spendDbClient.findCategoryById(
        UUID.fromString("0a091f06-b5bb-11f0-93fc-aa5c32f82d84"));
  }

  @Test
  void findCategoryByUsernameAndSpendName() {
    SpendDbClient spendDbClient = new SpendDbClient();
    Optional<CategoryJson> categoryByUsernameAndSpendName = spendDbClient.findCategoryByUsernameAndSpendName(
        "duck", "Машина");
  }

  @Test
  void createCategoryTest() {
    SpendDbClient spendDbClient = new SpendDbClient();

    CategoryEntity categoryEntity = new CategoryEntity();
    categoryEntity.setName("Test1qaa");
    categoryEntity.setUsername("duck");
    categoryEntity.setArchived(false);

    spendDbClient.createCategory(CategoryJson.fromEntity(categoryEntity));
  }

  @Test
  void removeCategoryTest() {
    SpendDbClient spendDbClient = new SpendDbClient();

    CategoryEntity categoryEntity = new CategoryEntity();
    categoryEntity.setName("Test5qaa");
    categoryEntity.setUsername("duck");
    categoryEntity.setArchived(false);
    spendDbClient.createCategory(CategoryJson.fromEntity(categoryEntity));
    spendDbClient.removeCategory(CategoryJson.fromEntity(categoryEntity));
  }
}
