package guru.qa.niffler.data.dao.userdata;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UserdataUserDao {

  @Nonnull
  UserEntity create(UserEntity user);

  @Nullable
  Optional<UserEntity> findById(UUID id);

  @Nullable
  Optional<UserEntity> findByUsername(String username);

  void delete(UserEntity user);

  @Nonnull
  List<UserEntity> findAll();
}
