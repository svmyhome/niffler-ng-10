package guru.qa.niffler.data.repository.impl.spend;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.jpa.EntityManagers;
import guru.qa.niffler.data.repository.spend.SpendRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SpendRepositoryHibernate implements SpendRepository {

  private final Config CFG = Config.getInstance();
  private final EntityManager entityManager = EntityManagers.em(CFG.spendJdbcUrl());

  @Override
  @Nonnull
  public SpendEntity create(SpendEntity spend) {
    entityManager.joinTransaction();
    entityManager.persist(spend);
    return spend;
  }

  @Override
  @Nonnull
  public SpendEntity update(SpendEntity spend) {
    return entityManager.merge(spend);
  }

  @Override
  @Nullable
  public Optional<SpendEntity> findSpendById(UUID id) {
    return Optional.ofNullable(entityManager.find(SpendEntity.class, id));
  }

  @Override
  @Nullable
  public Optional<SpendEntity> findByUsernameAndSpendDescription(String username,
      String description) {
    try {
      return Optional.of(
          entityManager.createQuery(
                  "select s FROM SpendEntity s WHERE s.username =:username AND s.description = :description",
                  SpendEntity.class)
              .setParameter("username", username)
              .setParameter("description", description)
              .getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @Override
  @Nonnull
  public CategoryEntity createCategory(CategoryEntity category) {
    entityManager.joinTransaction();
    entityManager.persist(category);
    return category;
  }

  @Override
  @Nonnull
  public CategoryEntity updateCategory(CategoryEntity category) {
    return entityManager.merge(category);
  }

  @Override
  @Nullable
  public Optional<CategoryEntity> findCategoryById(UUID id) {
    return Optional.ofNullable(entityManager.find(CategoryEntity.class, id));
  }

  @Override
  @Nullable
  public Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name) {
    try {
      return Optional.of(
          entityManager.createQuery(
                  "select c FROM CategoryEntity c WHERE c.username =:username AND c.name = :name",
                  CategoryEntity.class)
              .setParameter("username", username)
              .setParameter("name", name)
              .getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @Override
  public void remove(SpendEntity spend) {
    entityManager.joinTransaction();
    SpendEntity managedUser = entityManager.find(SpendEntity.class, spend.getId());
    if (managedUser != null) {
      entityManager.remove(managedUser);
    }
  }

  @Override
  public void removeCategory(CategoryEntity category) {
    entityManager.joinTransaction();
    CategoryEntity managedUser = entityManager.find(CategoryEntity.class, category.getId());
    if (managedUser != null) {
      entityManager.remove(managedUser);
    }
  }
}
