package guru.qa.niffler.service;

import guru.qa.niffler.model.user.UserJson;

public interface UserClient {

  UserJson createUser(UserJson user);
}
