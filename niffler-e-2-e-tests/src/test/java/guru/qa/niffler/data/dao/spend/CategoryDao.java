package guru.qa.niffler.data.dao.spend;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface CategoryDao {

  @Nonnull
  CategoryEntity create(CategoryEntity category);

  Optional<CategoryEntity> findById(UUID id);

  Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username,
      String name);

  @Nonnull
  List<CategoryEntity> findAllByUsername(String username);

  void delete(CategoryEntity category);

  @Nonnull
  List<CategoryEntity> findAll();

  @Nonnull
  CategoryEntity update(CategoryEntity category);
}
