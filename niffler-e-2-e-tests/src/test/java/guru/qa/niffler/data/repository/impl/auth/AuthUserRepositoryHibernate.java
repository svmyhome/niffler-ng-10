package guru.qa.niffler.data.repository.impl.auth;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.jpa.EntityManagers;
import guru.qa.niffler.data.repository.auth.AuthUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AuthUserRepositoryHibernate implements AuthUserRepository {

  private final Config CFG = Config.getInstance();
  private final EntityManager entityManager = EntityManagers.em(CFG.authJdbcUrl());

  @Override
  @Nonnull
  public AuthUserEntity create(AuthUserEntity user) {
    entityManager.joinTransaction();
    entityManager.persist(user);
    return user;
  }

  @Override
  @Nonnull
  public AuthUserEntity update(AuthUserEntity user) {
    return entityManager.merge(user);
  }

  @Override
  @Nullable
  public Optional<AuthUserEntity> findByUsername(String username) {
    try {
      return Optional.of(
          entityManager.createQuery("select u from AuthUserEntity u where u.username = :username",
                  AuthUserEntity.class)
              .setParameter("username", username)
              .getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @Override
  @Nullable
  public Optional<AuthUserEntity> findById(UUID id) {
    return Optional.ofNullable(entityManager.find(AuthUserEntity.class, id));
  }

  @Override
  public void remove(AuthUserEntity user) {
    entityManager.joinTransaction();
    AuthUserEntity managedUser = entityManager.find(AuthUserEntity.class, user.getId());
    if (managedUser != null) {
      entityManager.remove(managedUser);
    }
  }

  @Override
  @Nonnull
  public List<AuthUserEntity> findAll() {
    return entityManager.createQuery("select u from AuthUserEntity u ORDER BY u.username",
            AuthUserEntity.class)
        .getResultList();
  }
}
