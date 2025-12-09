package guru.qa.niffler.data.dao.spend;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDao {

  CategoryEntity create(CategoryEntity category);

  Optional<CategoryEntity> findById(UUID id);

  Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username,
      String name);

  List<CategoryEntity> findAllByUsername(String username);

  void delete(CategoryEntity category);

  List<CategoryEntity> findAll();

  CategoryEntity update(CategoryEntity category);
}
