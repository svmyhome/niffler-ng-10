package guru.qa.niffler.service;

import static guru.qa.niffler.data.Databases.dataSource;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.impl.SpendDaoJdbc;
import guru.qa.niffler.data.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public class SpendDbClient implements SpendClient {

  private static final Config CFG = Config.getInstance();
  private final CategoryDao categoryDao = new CategoryDaoJdbc();
  private final SpendDao spendDao = new SpendDaoJdbc();

  private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(CFG.authJdbcUrl());

  @Override
  public SpendJson findSpendById(String id, String username) {
    throw new UnsupportedOperationException("Not implemented :(");
  }

  @Override
  public List<SpendJson> findSpendsByUserName(String username, CurrencyValues currencyValues,
      String from, String to) {
    throw new UnsupportedOperationException("Method updateCategory() is not implemented yet");
  }

  @Override
  public List<SpendJson> findSpendsByUserName(String username) {
    return List.of();
  }


//  public List<SpendJson> findSpendsByUserName(String username) {
//    List<SpendEntity> entities = new SpendDaoSpringJdbc(
//        dataSource(CFG.spendJdbcUrl())).findAllByUsername(username);
//    return entities.stream().map(SpendJson::fromEntity).collect(Collectors.toList());
//  }

  @Override
  public SpendJson createSpend(SpendJson spend) {
    return jdbcTxTemplate.execute(() -> {
          SpendEntity spendEntity = SpendEntity.fromJson(spend);
          if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = categoryDao.create(
                spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
          }
          return SpendJson.fromEntity(spendDao.create(spendEntity)
          );
        }
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
    return List.of();
  }

  @Override
  public CategoryJson createCategory(CategoryJson category) {
    return null;
  }
//
//  @Override
//  public List<CategoryJson> findAllCategories(String username) {
//    List<CategoryEntity> entities = new CategoryDaoSpringJdbc(
//        dataSource(CFG.spendJdbcUrl())).findAllByUsername(username);
//    return entities.stream().map(CategoryJson::fromEntity).collect(Collectors.toList());
//  }
//
//  @Override
//  public CategoryJson createCategory(CategoryJson category) {
//    return Databases.transaction(connection -> {
//      CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
//      return CategoryJson.fromEntity(
//          new CategoryDaoJdbc(connection).create(categoryEntity)
//      );
//    }, CFG.spendJdbcUrl());
//  }

  @Override
  public CategoryJson updateCategory(CategoryJson categoryJson) {
    throw new UnsupportedOperationException("Not implemented :(");
  }

  @Override
  public Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName,
      String username) {
    return Optional.empty();
  }

//  @Override
//  public Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName,
//      String username) {
//    Optional<CategoryEntity> categoryEntity =
//        new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl()))
//            .findCategoryByUsernameAndCategoryName(categoryName, username);
//    return categoryEntity.map(CategoryJson::fromEntity);
//  }
//
//  public void deleteCategory(CategoryEntity category) {
//    new CategoryDaoSpringJdbc(
//        dataSource(CFG.spendJdbcUrl())).delete(category);
//  }
//
//  public void deleteSpend(SpendEntity spend) {
//    new SpendDaoSpringJdbc(
//        dataSource(CFG.spendJdbcUrl())).delete(spend);
//  }
//
//  public List<SpendJson> getAllSpends() {
//    List<SpendJson> spends = new ArrayList<>();
//    List<SpendEntity> spendEntities = new SpendDaoSpringJdbc(
//        dataSource(CFG.spendJdbcUrl())).findAll();
//    return spendEntities.stream().map(SpendJson::fromEntity).collect(Collectors.toList());
//  }
}
