package guru.qa.niffler.data.repository.spend;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface SpendRepository {

  @Nonnull
  SpendEntity create(SpendEntity spend);

  @Nonnull
  SpendEntity update(SpendEntity spend);

  @Nullable
  Optional<SpendEntity> findSpendById(UUID id);

  @Nullable
  Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description);

  @Nonnull
  CategoryEntity createCategory(CategoryEntity category);

  @Nonnull
  CategoryEntity updateCategory(CategoryEntity category);

  @Nullable
  Optional<CategoryEntity> findCategoryById(UUID id);

  @Nullable
  Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name);

  void remove(SpendEntity spend);

  void removeCategory(CategoryEntity category);
}
