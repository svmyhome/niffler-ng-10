package guru.qa.niffler.data.repository.userdata;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.impl.userdata.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.userdata.UserdataUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.userdata.UserdataUserRepositorySpringJdbc;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UserdataUserRepository {

  @Nonnull
  static UserdataUserRepository getInstance() {
    return switch (System.getProperty("repository", "jpa")) {
      case "jpa" -> new UserdataUserRepositoryHibernate();
      case "jdbc" -> new UserdataUserRepositoryJdbc();
      case "sjdbc" -> new UserdataUserRepositorySpringJdbc();
      default -> throw new IllegalStateException(
          "Unknown repository: " + System.getProperty("repository"));
    };
  }

  @Nonnull
  UserEntity create(UserEntity user);

  Optional<UserEntity> findById(UUID id);

  Optional<UserEntity> findByUsername(String username);

  @Nonnull
  UserEntity update(UserEntity user);

  @Nonnull
  List<UserEntity> findAll();

  void sendInvitation(UserEntity requester, UserEntity addressee);

  void addFriend(UserEntity requester, UserEntity addressee);

  void remove(UserEntity user);
}
