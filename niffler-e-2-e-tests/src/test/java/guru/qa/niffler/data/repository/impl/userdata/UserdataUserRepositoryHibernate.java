package guru.qa.niffler.data.repository.impl.userdata;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.jpa.EntityManagers;
import guru.qa.niffler.data.repository.userdata.UserdataUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserdataUserRepositoryHibernate implements UserdataUserRepository {

  private final Config CFG = Config.getInstance();

  private final EntityManager entityManager = EntityManagers.em(CFG.userdataJdbcUrl());

  @Override
  public UserEntity create(UserEntity user) {
    entityManager.joinTransaction();
    entityManager.persist(user);
    return user;
  }

  @Override
  public UserEntity createWithFriendship(UserEntity requester, UserEntity addressee) {
    return null;
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
    try {
      return Optional.of(entityManager.createQuery("select u from UserEntity u where u.username = :username",
              UserEntity.class)
          .setParameter("username", username)
          .getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<UserEntity> findById(UUID id) {
    return Optional.ofNullable(entityManager.find(UserEntity.class, id));
  }

  @Override
  public void delete(UserEntity user) {

  }

  @Override
  public List<UserEntity> findAll() {
    return List.of();
  }

  @Override
  public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {

  }

  @Override
  public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {

  }

  @Override
  public void addFriend(UserEntity requester, UserEntity addressee) {

  }
}
