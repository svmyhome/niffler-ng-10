package guru.qa.niffler.service;

import static guru.qa.niffler.data.Databases.dataSource;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.impl.SpendDaoJdbc;
import guru.qa.niffler.data.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class SpendDbClient implements SpendClient {

  private static final Config CFG = Config.getInstance();

  @Override
  public SpendJson findSpendById(String id, String username) {
    throw new UnsupportedOperationException("Not implemented :(");
  }

    @Override
    public List<SpendJson> findSpendsByUserName(String username, CurrencyValues currencyValues, String from, String to) {
        throw new UnsupportedOperationException("Method updateCategory() is not implemented yet");
    }

    public List<SpendJson> findSpendsByUserName(String username) {
        List<SpendEntity> entities = new SpendDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).findAllByUsername(username);
        return  entities.stream().map(SpendJson::fromEntity).collect(Collectors.toList());
  }

  @Override
  public SpendJson createSpend(SpendJson spend) {
    return Databases.transaction(connection -> {
          SpendEntity spendEntity = SpendEntity.fromJson(spend);
          if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = new CategoryDaoJdbc(connection).create(
                spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
          }
          return SpendJson.fromEntity(
              new SpendDaoJdbc(connection).create(spendEntity)
          );
        },
        CFG.spendJdbcUrl()
    );
  }

  @Override
  public SpendJson updateSpend(SpendJson spendJson) {
    throw new UnsupportedOperationException("Not implemented :(");
  }

  @Override
  public void deleteSpends(String username, List<String> ids) {
    throw new UnsupportedOperationException("Not implemented :(");
  }

  @Override
  public List<CategoryJson> findAllCategories(String username) {
      List<CategoryEntity> entities = new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).findAllByUsername(username);
      return entities.stream().map(CategoryJson::fromEntity).collect(Collectors.toList());
  }

  @Override
  public CategoryJson createCategory(CategoryJson category) {
    return Databases.transaction(connection -> {
      CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
      return CategoryJson.fromEntity(
          new CategoryDaoJdbc(connection).create(categoryEntity)
      );
    }, CFG.spendJdbcUrl());
  }

  @Override
  public CategoryJson updateCategory(CategoryJson categoryJson) {
    throw new UnsupportedOperationException("Not implemented :(");
  }

  @Override
  public Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName,
      String username) {
    throw new UnsupportedOperationException("Not implemented :(");
  }

  //TODO удалить потом
  // TODO отрефакторить метод выше, возможнос оит убрать все лишнее кроме имени
  public List<CategoryEntity> findCategotiesByUserNameList(String username) {
    List<CategoryEntity> CategoryDaoSpringJdbc =
        new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).findAllByUsername(username);
    return CategoryDaoSpringJdbc;
  }
}
