package guru.qa.niffler.data.dao.spend;

import guru.qa.niffler.data.entity.spend.SpendEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendDao {

  SpendEntity create(SpendEntity spend);

  Optional<SpendEntity> findById(UUID id);

  List<SpendEntity> findAllByCategoryId(UUID categoryId);

  List<SpendEntity> findAllByUsername(String username);

  void delete(SpendEntity spend);

  List<SpendEntity> findAll();

  SpendEntity update(SpendEntity spend);
}
