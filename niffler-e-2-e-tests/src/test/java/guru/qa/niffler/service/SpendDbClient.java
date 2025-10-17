package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.impl.SpendDaoJdbc;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import java.util.List;
import java.util.Optional;

public class SpendDbClient implements SpendClient {

  private final CategoryDao categoryDao = new CategoryDaoJdbc();
  private final SpendDao spendDao = new SpendDaoJdbc();

  @Override
  public SpendJson getSpendById(String id, String username) {
    return null;
  }

  @Override
  public List<SpendJson> findSpendsByUserName(String username, CurrencyValues currencyValues,
      String from, String to) {
    return List.of();
  }

  @Override
  public SpendJson createSpend(SpendJson spend) {
    SpendEntity spendEntity = SpendEntity.fromJson(spend);
    if (spendEntity.getCategory().getId() == null) {
      CategoryEntity categoryEntity = categoryDao.createCategory(
          spendEntity.getCategory());
      spendEntity.setCategory(categoryEntity);
    }
    return SpendJson.fromEntity(
        spendDao.createSpend(spendEntity)
    );
  }

  @Override
  public SpendJson updateSpend(SpendJson spendJson) {
    return null;
  }

  @Override
  public void deleteSpends(String username, List<String> ids) {

  }

  @Override
  public List<CategoryJson> findAllCategories(String username) {
    return List.of();
  }

  @Override
  public CategoryJson createCategory(CategoryJson category) {
    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);

    return CategoryJson.fromEntity(
        categoryDao.createCategory(categoryEntity)
    );
  }

  @Override
  public CategoryJson updateCategory(CategoryJson categoryJson) {
    return null;
  }

  @Override
  public Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName,
      String username) {
    return Optional.empty();
  }
}
