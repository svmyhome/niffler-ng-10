package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.impl.UserdataUserDAOJdbc;
import guru.qa.niffler.model.UserJson;

public class UserDbClient implements UserClient {

  private static final Config CFG = Config.getInstance();

  @Override
  public UserJson createUser(UserJson user) {
    return Databases.transaction(connection -> {
          UserEntity userEntity = UserEntity.fromJson(user);
          return UserJson.fromEntity(new UserdataUserDAOJdbc(connection).createUser(userEntity));
        },
        CFG.userdataJdbcUrl());
  }
}
