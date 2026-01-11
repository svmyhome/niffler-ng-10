package guru.qa.niffler.data.repository.auth;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.repository.impl.auth.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.auth.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.auth.AuthUserRepositorySpringJdbc;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface AuthUserRepository {

  @Nonnull
  static AuthUserRepository getInstance() {
    return switch (System.getProperty("repository", "jpa")) {
      case "jpa" -> new AuthUserRepositoryHibernate();
      case "jdbc" -> new AuthUserRepositoryJdbc();
      case "sjdbc" -> new AuthUserRepositorySpringJdbc();
      default -> throw new IllegalStateException(
          "Unknown repository: " + System.getProperty("repository"));
    };
  }

  @Nonnull
  AuthUserEntity create(AuthUserEntity user);

  @Nonnull
  AuthUserEntity update(AuthUserEntity user);

  Optional<AuthUserEntity> findById(UUID id);

  Optional<AuthUserEntity> findByUsername(String username);

  @Nonnull
  List<AuthUserEntity> findAll();

  void remove(AuthUserEntity user);
}
