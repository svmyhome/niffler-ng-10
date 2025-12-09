package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.auth.AuthUserRepository;
import guru.qa.niffler.data.repository.impl.auth.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.userdata.UserdataUserRepositoryHibernate;
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

  private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
  private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryHibernate();

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
      CFG.authJdbcUrl(),
      CFG.userdataJdbcUrl());

  public static UserEntity userEntityRequiredField(String username) {
    UserEntity ue = new UserEntity();
    ue.setUsername(username);
    ue.setCurrency(CurrencyValues.RUB);
    return ue;
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
  public UserJson createUser(String username, String password) {
    return xaTransactionTemplate.execute(() -> {
      AuthUserEntity authUser = authUserEntity(username, password);
      authUserRepository.create(authUser);
      return UserJson.fromEntity(userdataUserRepository.create(userEntityRequiredField(username)));
    });
  }

  public List<AuthUserJson> findAll() {
    List<AuthUserEntity> entities = authUserRepository.findAll();
    return entities.stream().map(AuthUserJson::fromEntity).collect(Collectors.toList());
  }

  public Optional<AuthUserEntity> findAuthUserById(UUID id) {
    return authUserRepository.findById(id);
  }

  public void delete(AuthUserEntity user) {
    xaTransactionTemplate.execute(() -> {
      authUserRepository.remove(user);
      return null;
    });
  }

  @Override
  public void createIncomeInvitations(UserJson targetUser, int count) {
    if (count > 0) {
      UserEntity targetEntity = userdataUserRepository.findById(targetUser.id()).orElseThrow();
      for (int i = 0; i < count; i++) {
        xaTransactionTemplate.execute(() -> {
          String username = RandomDataUtils.randomUsername();
          AuthUserEntity authUser = authUserEntity(username, "12345");
          authUserRepository.create(authUser);
          UserEntity adressee = userdataUserRepository.create(userEntityRequiredField(username));
//          userdataUserRepository.addIncomeInvitation(targetEntity, adressee);
          userdataUserRepository.sendInvitation(adressee, targetEntity);

          return null;
        });
      }
    }
  }

  @Override
  public void createOutcomeInvitations(UserJson targetUser, int count) {
    if (count > 0) {
      UserEntity targetEntity = userdataUserRepository.findById(targetUser.id()).orElseThrow();
      for (int i = 0; i < count; i++) {
        xaTransactionTemplate.execute(() -> {
          String username = RandomDataUtils.randomUsername();
          AuthUserEntity authUser = authUserEntity(username, "12345");
          authUserRepository.create(authUser);
          UserEntity adressee = userdataUserRepository.create(userEntityRequiredField(username));
//          userdataUserRepository.addOutcomeInvitation(targetEntity, adressee);
          userdataUserRepository.sendInvitation(targetEntity, adressee);
          return null;
        });
      }
    }
  }

  @Override
  public void createFriends(UserJson targetUser, int count) {
    if (count > 0) {
      UserEntity targetEntity = userdataUserRepository.findById(targetUser.id()).orElseThrow();
      for (int i = 0; i < count; i++) {
        xaTransactionTemplate.execute(() -> {
          String username = RandomDataUtils.randomUsername();
          AuthUserEntity authUser = authUserEntity(username, "12345");
          authUserRepository.create(authUser);
          UserEntity adressee = userdataUserRepository.create(userEntityRequiredField(username));
          userdataUserRepository.addFriend(targetEntity, adressee);
          return null;
        });
      }
    }
  }//TODO объеденить с методами ниже

//  public void addFriend(UserEntity requester, UserEntity addressee) {
//    xaTransactionTemplate.execute(() -> {
//    userdataUserRepositoryHibernate.addFriend(requester, addressee);
//      return null;
//    });
//  }
//
//  public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
//    userdataUserRepository.addIncomeInvitation(requester, addressee);
//  }
//
//  public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
//    userdataUserRepository.addOutcomeInvitation(requester, addressee);
//  }

//  public List<FriendshipEntity> findFriendshipsByRequesterId(UUID requesterId) {
//    return friendshipDAO.findByRequester(requesterId);
//  }
//
//  public void deleteFriendship(FriendshipEntity friendship) {
//    friendshipDAO.delete(friendship);
//  }

  public Optional<UserEntity> findUserById(UUID id) {
    return userdataUserRepository.findById(id);
  }

  public Optional<AuthUserEntity> findUserByUserName(String username) {
    return authUserRepository.findByUsername(username);
  }


  public AuthUserEntity update(AuthUserEntity user) {
    return xaTransactionTemplate.execute(() -> {
      authUserRepository.update(user);
      return null;
    });
  }
}
