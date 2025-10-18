package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
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

  private static final Config CFG = Config.getInstance();

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
    return Databases.transaction(connection -> {
          SpendEntity spendEntity = SpendEntity.fromJson(spend);
          if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = new CategoryDaoJdbc(connection).createCategory(
                spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
          }
          return SpendJson.fromEntity(
              new SpendDaoJdbc(connection).createSpend(spendEntity)
          );
        },
        CFG.spendJdbcUrl()
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
    return Databases.transaction(connection -> {
      CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
      return CategoryJson.fromEntity(
          new CategoryDaoJdbc(connection).createCategory(categoryEntity)
      );
    }, CFG.spendJdbcUrl());
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
