package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.entity.AuthUserEntity;
import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.data.entity.AuthorityEntity;
import guru.qa.niffler.data.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.impl.AuthUserDaoJdbc;
import guru.qa.niffler.model.AuthUserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthUserDbClient implements AuthUserClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  public AuthUserJson createUser(AuthUserJson authUser) {
    authUser.setPassword(pe.encode(authUser.getPassword()));
    AuthUserEntity ue = AuthUserEntity.fromJson(authUser);
    Databases.xaTransaction(
        new Databases.XaConsumer(
            connection -> {
              AuthUserDaoJdbc userDao = new AuthUserDaoJdbc(connection);
              var createdUser = userDao.create(ue);
              ue.setId(createdUser.getId());
              ue.setPassword(createdUser.getPassword());
              AuthAuthorityDaoJdbc authorityDao = new AuthAuthorityDaoJdbc(connection);
              var ae = new AuthorityEntity();
              ae.setUserId(ue.getId());
              ae.setAuthority(Authority.read);
              authorityDao.create(ae);
            },
            CFG.authJdbcUrl()
        )
    );
    return AuthUserJson.fromEntity(ue);
  }

  @Override
  public AuthUserJson findByUsername(String id, String username) {
    throw new UnsupportedOperationException("Not implemented :(");
  }

  @Override
  public void deleteUser(AuthUserJson user) {
    throw new UnsupportedOperationException("Not implemented :(");
  }
}
