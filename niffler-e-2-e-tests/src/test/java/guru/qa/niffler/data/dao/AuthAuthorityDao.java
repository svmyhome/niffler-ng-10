package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.AuthorityEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthAuthorityDao {

  void create(AuthorityEntity... authorities);

  List<AuthorityEntity> findAllByUserId(UUID userId);

  Optional<AuthorityEntity> findById(UUID id);

  void delete(AuthorityEntity... authority);

  List<AuthorityEntity> findAll();

}
