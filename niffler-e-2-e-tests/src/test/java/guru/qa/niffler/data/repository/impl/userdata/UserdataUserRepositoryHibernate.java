package guru.qa.niffler.data.repository.impl.userdata;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.jpa.EntityManagers;
import guru.qa.niffler.data.repository.userdata.UserdataUserRepository;
import jakarta.persistence.EntityManager;
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
    return Optional.empty(); //TODO дописать
  }

  @Override
  public UserEntity update(UserEntity user) {
    return null;
  }

  @Override
  public void remove(UserEntity user) {

  }

  @Override
  public List<UserEntity> findAll() {
    return List.of();
  }

  @Override
  public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
    entityManager.joinTransaction();
    addressee.addFriends(FriendshipStatus.PENDING, requester);
  }

  @Override
  public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
    entityManager.joinTransaction();
    requester.addFriends(FriendshipStatus.PENDING, addressee);
  }

  @Override
  public void sendInvitation(UserEntity requester, UserEntity addressee) {

  }

  @Override
  public void addFriend(UserEntity requester, UserEntity addressee) {
    entityManager.joinTransaction();
    requester.addFriends(FriendshipStatus.ACCEPTED, addressee);
    addressee.addFriends(FriendshipStatus.ACCEPTED, requester);
  }
}
