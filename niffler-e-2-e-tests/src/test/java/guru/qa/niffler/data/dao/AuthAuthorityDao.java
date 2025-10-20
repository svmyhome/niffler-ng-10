package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.AuthorityEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthAuthorityDao {

  AuthorityEntity create(AuthorityEntity authority);

  List<AuthorityEntity> findAuthoritiesByUserId(UUID id);

  Optional<AuthorityEntity> findById(UUID id);

  void delete(AuthorityEntity authority);

}
