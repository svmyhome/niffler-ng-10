package guru.qa.niffler.data.dao.auth;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface AuthAuthorityDao {

  void create(AuthorityEntity... authorities);

  @Nonnull
  List<AuthorityEntity> findAllByUserId(UUID userId);

  @Nullable
  Optional<AuthorityEntity> findById(UUID id);

  void delete(AuthorityEntity... authority);

  @Nonnull
  List<AuthorityEntity> findAll();
}
