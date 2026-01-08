package guru.qa.niffler.data.dao.userdata;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface FriendshipDao {

  void create(FriendshipEntity friendship);

  @Nonnull
  List<FriendshipEntity> findByRequester(UUID requesterId);

  @Nonnull
  List<FriendshipEntity> findByAddressee(UUID requesterId);

  void delete(FriendshipEntity user);
}
