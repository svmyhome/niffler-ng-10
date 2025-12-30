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
import io.qameta.allure.Step;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import utils.RandomDataUtils;

@ParametersAreNonnullByDefault
public class UserDbClient implements UserClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
  private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryHibernate();

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
      CFG.authJdbcUrl(),
      CFG.userdataJdbcUrl());

  public @Nonnull static UserEntity userEntityRequiredField(String username) {
    UserEntity ue = new UserEntity();
    ue.setUsername(username);
    ue.setCurrency(CurrencyValues.RUB);
    return ue;
  }

  private @Nonnull AuthUserEntity authUserEntity(String username, String password) {
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
  @Step("Create user to DB")
  public @Nonnull UserJson createUser(String username, String password) {
    return xaTransactionTemplate.execute(() -> {
      AuthUserEntity authUser = authUserEntity(username, password);
      authUserRepository.create(authUser);
      return UserJson.fromEntity(userdataUserRepository.create(userEntityRequiredField(username)));
    });
  }

  @Step("Find all users in DB")
  public List<AuthUserJson> findAll() {
    List<AuthUserEntity> entities = authUserRepository.findAll();
    return entities.stream().map(AuthUserJson::fromEntity).collect(Collectors.toList());
  }

  @Step("Find user by ID in DB")
  public @Nullable Optional<AuthUserEntity> findAuthUserById(UUID id) {
    return authUserRepository.findById(id);
  }

  public void delete(AuthUserEntity user) {
    xaTransactionTemplate.execute(() -> {
      authUserRepository.remove(user);
      return null;
    });
  }

  @Override
  @Step("Create income invitation for user {targetUser.username} to DB")
  public List<UserJson> createIncomeInvitations(UserJson targetUser, int count) {
    final List<UserJson> users = new ArrayList<>();
    if (count > 0) {
      for (int i = 0; i < count; i++) {
        xaTransactionTemplate.execute(() -> {
          UserEntity targetEntity = userdataUserRepository.findById(targetUser.id()).orElseThrow();
          String username = RandomDataUtils.randomUsername();
          AuthUserEntity authUser = authUserEntity(username, "12345");
          authUserRepository.create(authUser);
          UserEntity adressee = userdataUserRepository.create(userEntityRequiredField(username));
          userdataUserRepository.sendInvitation(adressee, targetEntity);
          users.add(UserJson.fromEntity(adressee));
          return null;
        });
      }
    }
    return users;
  }

  @Override
  @Step("Create outcome invitation for user {targetUser.username} to DB")
  public List<UserJson> createOutcomeInvitations(UserJson targetUser, int count) {
    final List<UserJson> users = new ArrayList<>();
    if (count > 0) {
      for (int i = 0; i < count; i++) {
        xaTransactionTemplate.execute(() -> {
          UserEntity targetEntity = userdataUserRepository.findById(targetUser.id()).orElseThrow();
          String username = RandomDataUtils.randomUsername();
          AuthUserEntity authUser = authUserEntity(username, "12345");
          authUserRepository.create(authUser);
          UserEntity adressee = userdataUserRepository.create(userEntityRequiredField(username));
          userdataUserRepository.sendInvitation(targetEntity, adressee);
          users.add(UserJson.fromEntity(adressee));
          return null;
        });
      }
    }
    return users;
  }

  @Override
  @Step("Create {count} friends for user {targetUser.username} to DB")
  public List<UserJson> createFriends(UserJson targetUser, int count) {
    final List<UserJson> users = new ArrayList<>();
    if (count > 0) {
      for (int i = 0; i < count; i++) {
        xaTransactionTemplate.execute(() -> {
          UserEntity targetEntity = userdataUserRepository.findById(targetUser.id()).orElseThrow();
          String username = RandomDataUtils.randomUsername();
          AuthUserEntity authUser = authUserEntity(username, "12345");
          authUserRepository.create(authUser);
          UserEntity adressee = userdataUserRepository.create(userEntityRequiredField(username));
          userdataUserRepository.addFriend(targetEntity, adressee);
          users.add(UserJson.fromEntity(adressee));
          return null;
        });
      }
    }
    return users;
  }

  @Step("Find user by ID in DB")
  public @Nullable Optional<UserEntity> findUserById(UUID id) {
    return userdataUserRepository.findById(id);
  }

  @Step("Find user by username in DB")
  public @Nullable Optional<AuthUserEntity> findUserByUserName(String username) {
    return authUserRepository.findByUsername(username);
  }

  @Step("Update user in DB")
  public AuthUserEntity update(AuthUserEntity user) {
    return xaTransactionTemplate.execute(() -> {
      authUserRepository.update(user);
      return null;
    });
  }

  @Step("Remove user from DB")
  public void remove(UserEntity user) {
    xaTransactionTemplate.execute(() -> {
      UserEntity managedUser = userdataUserRepository.findById(user.getId())
          .orElseThrow(() -> new RuntimeException("User not found"));
      AuthUserEntity managedAuthUser = authUserRepository.findByUsername(user.getUsername())
          .orElseThrow(() -> new RuntimeException("AuthUser not found"));
      userdataUserRepository.remove(managedUser);
      authUserRepository.remove(managedAuthUser);
      return null;
    });
  }
}
