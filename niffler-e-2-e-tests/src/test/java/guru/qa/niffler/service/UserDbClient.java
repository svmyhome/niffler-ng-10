package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.UserdataUserDAO;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.impl.UserdataUserDAOJdbc;
import guru.qa.niffler.model.UserJson;

public class UserDbClient implements UserClient {

  private final UserdataUserDAO userDAO = new UserdataUserDAOJdbc();

  @Override
  public UserJson createUser(UserJson user) {
    UserEntity userEntity = UserEntity.fromJson(user);
    return UserJson.fromEntity(userDAO.createUser(userEntity));
  }
}
