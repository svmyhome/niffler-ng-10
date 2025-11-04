package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.auth.AuthAuthorityDao;
import guru.qa.niffler.data.dao.auth.AuthUserDao;
import guru.qa.niffler.data.dao.userdata.UserdataUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.impl.auth.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.impl.auth.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.impl.userdata.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.model.user.UserJson;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserDbClient implements UserClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
  private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();
  private final UserdataUserDao userdataUserDAO = new UserdataUserDaoSpringJdbc();

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
      CFG.authJdbcUrl(),
      CFG.userdataJdbcUrl());

  public UserJson createUserSpringJdbc(UserJson user) {
    return xaTransactionTemplate.execute(() -> {
      AuthUserEntity authUser = new AuthUserEntity();
      authUser.setUsername(user.username());
      authUser.setPassword(pe.encode("12345"));
      authUser.setEnabled(true);
      authUser.setAccountNonExpired(true);
      authUser.setAccountNonLocked(true);
      authUser.setCredentialsNonExpired(true);

      AuthUserEntity createdAuthUser = authUserDao.create(authUser);

      AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
          a -> {
            AuthorityEntity ae = new AuthorityEntity();
            ae.setUserId(createdAuthUser.getId());
            ae.setAuthority(a);
            return ae;
          }
      ).toArray(AuthorityEntity[]::new);
      authAuthorityDao.create(authorityEntities);
      return UserJson.fromEntity(userdataUserDAO.create(UserEntity.fromJson(user)));
    });
  }

  @Override
  public UserJson createUser(UserJson user) {
    return UserJson.fromEntity(
        xaTransactionTemplate.execute(() -> {
              AuthUserEntity authUser = new AuthUserEntity();
              authUser.setUsername(user.username());
              authUser.setPassword(pe.encode("12345"));
              authUser.setEnabled(true);
              authUser.setAccountNonExpired(true);
              authUser.setAccountNonLocked(true);
              authUser.setCredentialsNonExpired(true);
              authUserDao.create(authUser);
              authAuthorityDao.create(
                  Arrays.stream(Authority.values())
                      .map(a -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setUserId(authUser.getId());
                            ae.setAuthority(a);
                            return ae;
                          }
                      ).toArray(AuthorityEntity[]::new));
              UserEntity ue = UserEntity.fromJson(user);
              userdataUserDAO.create(ue);
              return ue;
            }
        )
    );
  }

  public List<AuthUserJson> findAll() {
    List<AuthUserEntity> entities = authUserDao.findAll();
    return entities.stream().map(AuthUserJson::fromEntity).collect(Collectors.toList());
  }

  public void delete(AuthUserEntity user) {
    authUserDao.delete(user);
  }
}
