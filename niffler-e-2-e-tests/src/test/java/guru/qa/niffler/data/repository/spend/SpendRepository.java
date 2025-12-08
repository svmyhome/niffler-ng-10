package guru.qa.niffler.data.repository.spend;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendRepository {

  SpendEntity createSpend(SpendEntity spend);

  SpendEntity update(SpendEntity spend);

  Optional<SpendEntity> findSpendById(String id);

  Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description);

  List<SpendEntity> findSpendsByUserName(String username);

  CategoryEntity createCategory(CategoryEntity category);

  CategoryEntity updateCategory(CategoryEntity category);

  Optional<CategoryEntity> findCategoryById(UUID id);

  Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name);

  List<CategoryEntity> findAllCategories(String username);

  void remove(SpendEntity spend);

  void removeCategory(CategoryEntity category);
}
