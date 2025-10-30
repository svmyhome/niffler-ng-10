package guru.qa.niffler.service;

import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.xaTransaction;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.entity.AuthUserEntity;
import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.data.entity.AuthorityEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.impl.UserdataUserDAOJdbc;
import guru.qa.niffler.data.impl.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.model.UserJson;
import java.util.Arrays;

import org.springframework.jdbc.support.JdbcTransactionManager;
import java.util.List;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

public class UserDbClient implements UserClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private final TransactionTemplate transactionTemplate = new TransactionTemplate(
          new JdbcTransactionManager(Databases.dataSource(CFG.authJdbcUrl()))
  );

  public UserJson createUserSpringJdbc(UserJson user) {
      transactionTemplate.execute(status -> {
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
          return null;
      });

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
  public List<AuthUserEntity> findAll() {
    List<AuthUserEntity> entities = new AuthUserDaoSpringJdbc(
        dataSource(CFG.authJdbcUrl())).findAll();
    return entities;
  }

  public void delete(AuthUserEntity user) {
    new AuthUserDaoSpringJdbc(
        dataSource(CFG.authJdbcUrl())).delete(user);
  }

  public void deleteJdbc(AuthorityEntity... authority) {
    xaTransaction(
        new Databases.XaFunction<Void>(
            con -> {
              new AuthAuthorityDaoJdbc(con).delete(authority);
              return null;
            },
            CFG.authJdbcUrl()
        )
    );
  }

}
