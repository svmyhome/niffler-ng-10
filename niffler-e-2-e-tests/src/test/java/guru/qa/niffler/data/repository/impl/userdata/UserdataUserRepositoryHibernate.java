package guru.qa.niffler.data.repository.impl.userdata;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
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
  public Optional<UserEntity> findById(UUID id) {
    return Optional.ofNullable(entityManager.find(UserEntity.class, id));
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
    return entityManager
        .createQuery("SELECT u FROM UserEntity u WHERE u.username = :username", UserEntity.class)
        .setParameter("username", username)
        .getResultStream()
        .findFirst();
  }

  @Override
  public UserEntity update(UserEntity user) {
    return entityManager.merge(user);
  }

  @Override
  public void remove(UserEntity user) {
    entityManager.joinTransaction();
    UserEntity managedUser = entityManager.find(UserEntity.class, user.getId());
    if (managedUser != null) {
      entityManager.remove(managedUser);
    }
  }

  @Override
  public List<UserEntity> findAll() {
    return entityManager.createQuery("select u from UserEntity u ORDER BY u.username",
            UserEntity.class)
        .getResultList();
  }

  public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
    entityManager.joinTransaction();
    addressee.addFriends(FriendshipStatus.PENDING, requester);
  }

  public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
    entityManager.joinTransaction();
    requester.addFriends(FriendshipStatus.PENDING, addressee);
  }

  @Override
  public void sendInvitation(UserEntity requester, UserEntity addressee) {
    entityManager.joinTransaction();
    requester.addFriends(FriendshipStatus.PENDING, addressee);
  }

  @Override
  public void addFriend(UserEntity requester, UserEntity addressee) {
    entityManager.joinTransaction();
    requester.addFriends(FriendshipStatus.ACCEPTED, addressee);
    addressee.addFriends(FriendshipStatus.ACCEPTED, requester);
  }
}
