package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.impl.spend.SpendRepositoryHibernate;
import guru.qa.niffler.data.repository.spend.SpendRepository;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import java.util.Optional;
import java.util.UUID;

public class SpendDbClient implements SpendClientEntity {

  private static final Config CFG = Config.getInstance();
  private final SpendRepository spendRepository = new SpendRepositoryHibernate();


  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
      CFG.spendJdbcUrl());

  @Override
  public SpendEntity create(SpendEntity spend) {
    return xaTransactionTemplate.execute(() -> {
      SpendEntity entity = spendRepository.create(spend);
      return entity;
    });
  }

  @Override
  public SpendEntity update(SpendEntity spend) {
    return xaTransactionTemplate.execute(() -> {
      spendRepository.update(spend);
      return null;
    });
  }

  @Override
  public CategoryEntity createCategory(CategoryEntity category) {
    return xaTransactionTemplate.execute(() -> {
      CategoryEntity entity = spendRepository.createCategory(category);
      return entity;
    });
  }

  @Override
  public Optional<CategoryEntity> findCategoryById(UUID id) {
    return spendRepository.findCategoryById(id);
  }

  @Override
  public Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name) {
    return spendRepository.findCategoryByUsernameAndSpendName(username, name);
  }

  @Override
  public Optional<SpendEntity> findById(UUID id) {
    return spendRepository.findSpendById(id);
  }

  @Override
  public Optional<SpendEntity> findByUsernameAndSpendDescription(String username,
      String description) {
    return spendRepository.findByUsernameAndSpendDescription(username, description);
  }

  @Override
  public void remove(SpendEntity spend) {
    xaTransactionTemplate.execute(() -> {
      spendRepository.remove(spend);
      return null;
    });
  }

  @Override
  public void removeCategory(CategoryEntity category) {
    xaTransactionTemplate.execute(() -> {
      spendRepository.removeCategory(category);
      return null;
    });
  }
}
