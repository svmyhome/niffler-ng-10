package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.spend.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.spend.SpendDaoJdbc;
import guru.qa.niffler.data.dao.spend.CategoryDao;
import guru.qa.niffler.data.dao.spend.SpendDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class SpendJsonClient implements SpendClient {

  private static final Config CFG = Config.getInstance();
  private final CategoryDao categoryDao = new CategoryDaoJdbc();
  private final SpendDao spendDao = new SpendDaoJdbc();

  private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
      CFG.authJdbcUrl());

  @Override
  public Optional<SpendJson> findSpendById(String id) {
    return spendDao.findById(UUID.fromString(id))
        .map(SpendJson::fromEntity);
  }

  public List<SpendJson> findSpendsByUserName(String username) {
    List<SpendEntity> entities = spendDao.findAllByUsername(username);
    return entities.stream().map(SpendJson::fromEntity).collect(Collectors.toList());
  }

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
  public SpendJson updateSpend(SpendJson spend) {
    return SpendJson.fromEntity(spendDao.update(SpendEntity.fromJson(spend)));
  }

  @Override
  public List<CategoryJson> findAllCategories(String username) {
    List<CategoryEntity> entities = categoryDao.findAllByUsername(username);
    return entities.stream().map(CategoryJson::fromEntity).collect(Collectors.toList());
  }

  @Override
  public CategoryJson createCategory(CategoryJson category) {
    return jdbcTxTemplate.execute(() -> {
      CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
      return CategoryJson.fromEntity(categoryDao.create(categoryEntity));
    });
  }

  @Override
  public CategoryJson updateCategory(CategoryJson category) {
    return CategoryJson.fromEntity(categoryDao.update(CategoryEntity.fromJson(category)));
  }

  @Override
  public Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName,
      String username) {
    Optional<CategoryEntity> categoryEntity = categoryDao.findCategoryByUsernameAndSpendName(
        categoryName, username);
    return categoryEntity.map(CategoryJson::fromEntity);
  }

  public void deleteCategory(CategoryEntity category) {
    categoryDao.delete(category);
  }

  public void deleteSpend(SpendEntity spend) {
    spendDao.delete(spend);
  }

  public List<SpendJson> findAllSpends() {
    List<SpendEntity> spendEntities = spendDao.findAll();
    return spendEntities.stream().map(SpendJson::fromEntity).collect(Collectors.toList());
  }
}
