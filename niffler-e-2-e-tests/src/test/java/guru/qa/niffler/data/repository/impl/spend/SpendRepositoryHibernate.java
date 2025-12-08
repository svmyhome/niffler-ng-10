package guru.qa.niffler.data.repository.impl.spend;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.spend.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.spend.SpendDaoJdbc;
import guru.qa.niffler.data.dao.spend.CategoryDao;
import guru.qa.niffler.data.dao.spend.SpendDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.jpa.EntityManagers;
import guru.qa.niffler.data.repository.spend.SpendRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendRepositoryHibernate implements SpendRepository {

  private final Config CFG = Config.getInstance();

  private final EntityManager entityManager = EntityManagers.em(CFG.spendJdbcUrl());

  @Override
  public SpendEntity createSpend(SpendEntity spend) {
    return null;
  }

  @Override
  public SpendEntity update(SpendEntity spend) {
    return null;
  }

  @Override
  public Optional<SpendEntity> findSpendById(UUID id) {
    return Optional.ofNullable(entityManager.find(SpendEntity.class, id));
  }

  @Override
  public Optional<SpendEntity> findByUsernameAndSpendDescription(String username,
      String description) {
    return Optional.empty();
  }

  @Override
  public List<SpendEntity> findSpendsByUserName(String username) {
    return List.of();
  }

  @Override
  public CategoryEntity createCategory(CategoryEntity category) {
    return null;
  }

  @Override
  public CategoryEntity updateCategory(CategoryEntity category) {
    return null;
  }

  @Override
  public Optional<CategoryEntity> findCategoryById(UUID id) {
    return Optional.empty();
  }

  @Override
  public Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name) {
    return Optional.empty();
  }

  @Override
  public List<CategoryEntity> findAllCategories(String username) {
    return List.of();
  }

  @Override
  public void remove(SpendEntity spend) {

  }

  @Override
  public void removeCategory(CategoryEntity category) {

  }
}
