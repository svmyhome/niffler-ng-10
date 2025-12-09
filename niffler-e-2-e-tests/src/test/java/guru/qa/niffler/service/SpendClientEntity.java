package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.spend.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.spend.SpendDaoJdbc;
import guru.qa.niffler.data.dao.spend.CategoryDao;
import guru.qa.niffler.data.dao.spend.SpendDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendClientEntity {

  SpendEntity create(SpendEntity spend);

  SpendEntity update(SpendEntity spend);

  CategoryEntity createCategory(CategoryEntity category);

  Optional<CategoryEntity> findCategoryById(UUID id);

  Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name);

  Optional<SpendEntity> findById(UUID id);

  Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description);

  void remove(SpendEntity spend);

  void removeCategory(CategoryEntity category);
}
