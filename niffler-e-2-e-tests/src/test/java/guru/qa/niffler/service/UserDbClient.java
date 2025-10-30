package guru.qa.niffler.service;

import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.xaTransaction;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.entity.*;
import guru.qa.niffler.data.impl.*;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserDbClient implements UserClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  public UserJson createUserSpringJdbc(UserJson user) {
    AuthUserEntity authUser = new AuthUserEntity();
    authUser.setUsername(user.username());
    authUser.setPassword(pe.encode("12345"));
    authUser.setEnabled(true);
    authUser.setAccountNonExpired(true);
    authUser.setAccountNonLocked(true);
    authUser.setCredentialsNonExpired(true);

    AuthUserEntity createdAuthUser =
        new AuthUserDaoSpringJdbc(dataSource(CFG.authJdbcUrl())).create(authUser);

    AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
        a -> {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setUserId(createdAuthUser.getId());
          ae.setAuthority(a);
          return ae;
        }
    ).toArray(AuthorityEntity[]::new);

    new AuthAuthorityDaoSpringJdbc(dataSource(CFG.authJdbcUrl())).create(authorityEntities);

    return UserJson.fromEntity(
        new UserdataUserDaoSpringJdbc(dataSource(CFG.userdataJdbcUrl()))
            .create(UserEntity.fromJson(user))
    );

  }

  @Override
  public UserJson createUser(UserJson user) {
    return UserJson.fromEntity(
        xaTransaction(
            new Databases.XaFunction<>(
                con -> {
                  AuthUserEntity authUser = new AuthUserEntity();
                  authUser.setUsername(user.username());
                  authUser.setPassword(pe.encode("12345"));
                  authUser.setEnabled(true);
                  authUser.setAccountNonExpired(true);
                  authUser.setAccountNonLocked(true);
                  authUser.setCredentialsNonExpired(true);
                  new AuthUserDaoJdbc(con).create(authUser);
                  new AuthAuthorityDaoJdbc(con).create(
                      Arrays.stream(Authority.values())
                          .map(a -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUserId(authUser.getId());
                                ae.setAuthority(a);
                                return ae;
                              }
                          ).toArray(AuthorityEntity[]::new));
                  return null;
                },
                CFG.authJdbcUrl()
            ),
            new Databases.XaFunction<>(
                con -> {
                  UserEntity ue = UserEntity.fromJson(user);
                  new UserdataUserDAOJdbc(con).create(ue);
                  return ue;
                },
                CFG.userdataJdbcUrl()
            )
        ));
  }

  //TODO передлать на Authotity Json
    public List<AuthorityEntity> findAll() {
        List<AuthorityEntity> entities = new AuthAuthorityDaoSpringJdbc(
                dataSource(CFG.authJdbcUrl())).findAll();
        return entities;
    }

    public void delete(AuthorityEntity... authority) {
        new AuthAuthorityDaoSpringJdbc(
                dataSource(CFG.authJdbcUrl())).delete(authority);
    }
}
