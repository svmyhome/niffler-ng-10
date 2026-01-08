package guru.qa.niffler.data.repository.userdata;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UserdataUserRepository {

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
