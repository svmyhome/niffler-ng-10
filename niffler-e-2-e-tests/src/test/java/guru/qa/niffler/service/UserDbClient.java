package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.auth.AuthAuthorityDao;
import guru.qa.niffler.data.dao.auth.AuthUserDao;
import guru.qa.niffler.data.dao.impl.auth.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.auth.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.userdata.FriendshipDaoJdbc;
import guru.qa.niffler.data.dao.impl.userdata.UserdataUserDaoJdbc;
import guru.qa.niffler.data.dao.userdata.FriendshipDao;
import guru.qa.niffler.data.dao.userdata.UserdataUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.auth.AuthUserRepository;
import guru.qa.niffler.data.repository.impl.auth.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.auth.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.userdata.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.userdata.UserdataUserRepositoryJdbc;
import guru.qa.niffler.data.repository.userdata.UserdataUserRepository;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.model.spend.CurrencyValues;
import guru.qa.niffler.model.user.UserJson;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import utils.RandomDataUtils;

public class UserDbClient implements UserClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
  private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();
  private final AuthUserRepository authUserRepository = new AuthUserRepositoryJdbc();
  private final UserdataUserDao userdataUserDAO = new UserdataUserDaoJdbc();
  private final FriendshipDao friendshipDAO = new FriendshipDaoJdbc();
  private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryJdbc();
  private final AuthUserRepository authUserRepositoryHibernate = new AuthUserRepositoryHibernate();
  private final UserdataUserRepository userdataUserRepositoryHibernate = new UserdataUserRepositoryHibernate();

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
      CFG.authJdbcUrl(),
      CFG.userdataJdbcUrl());

  public static UserEntity userEntity(String username) {
    UserEntity ue = new UserEntity();
    ue.setUsername(username);
    ue.setCurrency(CurrencyValues.RUB);
    return ue;
  }

  public UserJson createUserSpringJdbc(String username, String password) {
    return xaTransactionTemplate.execute(() -> {
      AuthUserEntity authUser = authUserEntity(username, password);
      authUserRepositoryHibernate.create(authUser);
      return UserJson.fromEntity(userdataUserRepositoryHibernate.create(userEntity(username)));
    });
  }

  private AuthUserEntity authUserEntity(String username, String password) {
    AuthUserEntity authUser = new AuthUserEntity();
    authUser.setUsername(username);
    authUser.setPassword(pe.encode(password));
    authUser.setEnabled(true);
    authUser.setAccountNonExpired(true);
    authUser.setAccountNonLocked(true);
    authUser.setCredentialsNonExpired(true);
    authUser.setAuthorities(Arrays.stream(Authority.values()).map(
        a -> {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setUser(authUser);
          ae.setAuthority(a);
          return ae;
        }
    ).toList());
    return authUser;
  }

  @Override
  public UserJson createUser(UserJson user) {
    return UserJson.fromEntity(
        xaTransactionTemplate.execute(() -> {
              AuthUserEntity authUser = authUserEntity("user",
                  "1234"); // TODO переделать на стринг сейчас тут заглушка
              authUserRepository.create(authUser);
              UserEntity ue = UserEntity.fromJson(user);
              userdataUserDAO.create(ue);
              return ue;
            }
        )
    );
  }

  public List<AuthUserJson> findAll() {
    List<AuthUserEntity> entities = authUserRepository.findAll();
    return entities.stream().map(AuthUserJson::fromEntity).collect(Collectors.toList());
  }

  public void delete(AuthUserEntity user) {
    authUserRepository.delete(user);
  }

  public void addIncomeInvitationHiber(UserJson targetUser, int count) {
    if (count > 0) {
      UserEntity targetEntity = userdataUserRepositoryHibernate.findById(targetUser.id()).orElseThrow();
      for (int i = 0; i < count; i++) {
        xaTransactionTemplate.execute(() -> {
          String username = RandomDataUtils.randomUsername();
          AuthUserEntity authUser = authUserEntity(username, "12345");
          authUserRepositoryHibernate.create(authUser);
          UserEntity adressee = userdataUserRepositoryHibernate.create(userEntity(username));
          userdataUserRepositoryHibernate.addIncomeInvitation(targetEntity, adressee);
          return null;
        });
      }
    }
  } //TODO объеденить с методами ниже

  public void addOutcomeInvitationHiber(UserJson targetUser, int count) {
    if (count > 0) {
      UserEntity targetEntity = userdataUserRepositoryHibernate.findById(targetUser.id()).orElseThrow();
      for (int i = 0; i < count; i++) {
        xaTransactionTemplate.execute(() -> {
          String username = RandomDataUtils.randomUsername();
          AuthUserEntity authUser = authUserEntity(username, "12345");
          authUserRepositoryHibernate.create(authUser);
          UserEntity adressee = userdataUserRepositoryHibernate.create(userEntity(username));
          userdataUserRepositoryHibernate.addOutcomeInvitation(targetEntity, adressee);
          return null;
        });
      }
    }
  } //TODO объеденить с методами ниже

  public void addFriendHiber(UserJson targetUser, int count) {
    if (count > 0) {
      UserEntity targetEntity = userdataUserRepositoryHibernate.findById(targetUser.id()).orElseThrow();
      for (int i = 0; i < count; i++) {
        xaTransactionTemplate.execute(() -> {
          String username = RandomDataUtils.randomUsername();
          AuthUserEntity authUser = authUserEntity(username, "12345");
          authUserRepositoryHibernate.create(authUser);
          UserEntity adressee = userdataUserRepositoryHibernate.create(userEntity(username));
          userdataUserRepositoryHibernate.addFriend(targetEntity, adressee);
          return null;
        });
      }
    }
  }//TODO объеденить с методами ниже

  public void addFriend(UserEntity requester, UserEntity addressee) {
    userdataUserRepository.addFriend(requester, addressee);
  }

  public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
    userdataUserRepository.addIncomeInvitation(requester, addressee);
  }

  public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
    userdataUserRepository.addOutcomeInvitation(requester, addressee);
  }

  public List<FriendshipEntity> findFriendshipsByRequesterId(UUID requesterId) {
    return friendshipDAO.findByRequester(requesterId);
  }

  public void deleteFriendship(FriendshipEntity friendship) {
    friendshipDAO.delete(friendship);
  }

  public Optional<UserEntity> findUserById(UUID id) {
    return userdataUserRepository.findById(id);
  }
}
