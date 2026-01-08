package guru.qa.niffler.test.db;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.service.UserDbClient;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class FriendshipDbTest {

  private static final Config CFG = Config.getInstance();

  @Test
  void findFridByIdTest() {
    UserDbClient userDbClient = new UserDbClient();
    userDbClient.findUserById(UUID.fromString("4d4d6b81-e573-4270-ac88-6b1b10d97d40"));
  }
}
