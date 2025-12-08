package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.spend.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.spend.SpendDaoJdbc;
import guru.qa.niffler.data.dao.spend.CategoryDao;
import guru.qa.niffler.data.dao.spend.SpendDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.impl.spend.SpendRepositoryHibernate;
import guru.qa.niffler.data.repository.spend.SpendRepository;
import java.util.Optional;
import java.util.UUID;

public class SpendDnEntityClient implements SpendClientEntity{

  private static final Config CFG = Config.getInstance();
  private final SpendRepository spendRepository = new SpendRepositoryHibernate();

  @Override
  public SpendEntity create(SpendEntity spend) {
    return null;
  }

  @Override
  public SpendEntity update(SpendEntity spend) {
    return null;
  }

  @Override
  public CategoryEntity createCategory(CategoryEntity category) {
    return null;
  }

  @Override
  public Optional<CategoryEntity> findCategoryById(UUID id) {
    return spendRepository.findCategoryById(id);
  }

  @Override
  public Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name) {
    return Optional.empty();
  }

  @Override
  public Optional<SpendEntity> findById(UUID id) {
    return spendRepository.findSpendById(id);
  }

  @Override
  public Optional<SpendEntity> findByUsernameAndSpendDescription(String username,
      String description) {
    return Optional.empty();
  }

  @Override
  public void remove(SpendEntity spend) {

  }

  @Override
  public void removeCategory(CategoryEntity spend) {

  }
}
