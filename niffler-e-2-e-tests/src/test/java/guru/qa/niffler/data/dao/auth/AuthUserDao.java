package guru.qa.niffler.data.dao.auth;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface AuthUserDao {

  @Nonnull
  AuthUserEntity create(AuthUserEntity user);

  @Nullable
  Optional<AuthUserEntity> findByUsername(String username);

  @Nullable
  Optional<AuthUserEntity> findById(UUID id);

  void delete(AuthUserEntity user);

  @Nonnull
  List<AuthUserEntity> findAll();
}
