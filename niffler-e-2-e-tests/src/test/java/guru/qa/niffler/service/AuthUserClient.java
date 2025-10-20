package guru.qa.niffler.service;

import guru.qa.niffler.model.AuthUserJson;

public interface AuthUserClient {

    AuthUserJson createUser(AuthUserJson user);

    AuthUserJson findByUsername(String id, String username);

    void deleteUser(AuthUserJson user);
}
