package guru.qa.niffler.data.repository.spend;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.impl.spend.SpendRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.spend.SpendRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.spend.SpendRepositorySpringJdbc;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface SpendRepository {

  @Nonnull
  static SpendRepository getInstance() {
    return switch (System.getProperty("repository", "jpa")) {
      case "jpa" -> new SpendRepositoryHibernate();
      case "jdbc" -> new SpendRepositoryJdbc();
      case "sjdbc" -> new SpendRepositorySpringJdbc();
      default -> throw new IllegalStateException(
          "Unknown repository: " + System.getProperty("repository"));
    };
  }

  @Nonnull
  SpendEntity create(SpendEntity spend);

  @Nonnull
  SpendEntity update(SpendEntity spend);

  Optional<SpendEntity> findSpendById(UUID id);

  Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description);

  @Nonnull
  CategoryEntity createCategory(CategoryEntity category);

  @Nonnull
  CategoryEntity updateCategory(CategoryEntity category);

  Optional<CategoryEntity> findCategoryById(UUID id);

  Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name);

  void remove(SpendEntity spend);

  void removeCategory(CategoryEntity category);
}
