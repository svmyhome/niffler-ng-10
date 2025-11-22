package guru.qa.niffler.data.dao.userdata;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import java.util.List;
import java.util.UUID;

public interface FriendshipDao {

  void create(FriendshipEntity friendship);

  List<FriendshipEntity> findByRequesterId(UUID requesterId);

  List<FriendshipEntity> findByAddresseeId(UUID requesterId);

  void delete(FriendshipEntity user);
}
