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
import guru.qa.niffler.data.repository.impl.auth.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.userdata.UserdataUserRepositoryJdbc;
import guru.qa.niffler.data.repository.userdata.UserdataUserRepository;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.model.user.UserJson;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserDbClient implements UserClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
  private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();
  private final AuthUserRepository authUserRepository = new AuthUserRepositoryJdbc();
  private final UserdataUserDao userdataUserDAO = new UserdataUserDaoJdbc();
  private final FriendshipDao friendshipDAO = new FriendshipDaoJdbc();
  private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryJdbc();

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
      authUser.setAuthorities(Arrays.stream(Authority.values()).map(
          a -> {
            AuthorityEntity ae = new AuthorityEntity();
            ae.setUser(authUser);
            ae.setAuthority(a);
            return ae;
          }
      ).toList());

      authUserRepository.create(authUser);
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
              authUser.setAuthorities(Arrays.stream(Authority.values()).map(
                  a -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUser(authUser);
                    ae.setAuthority(a);
                    return ae;
                  }
              ).toList());
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

  public UserEntity createUserWithFriend(UserEntity requester, UserEntity addressee) {
    return userdataUserRepository.createWithFriendship(requester, addressee);
  }

  public Optional<UserEntity> findUserById(UUID id) {
    return userdataUserRepository.findById(id);
  }

  public UserJson createUser1(UserJson user, UserEntity addressee) {
    return UserJson.fromEntity(
        xaTransactionTemplate.execute(() -> {
              AuthUserEntity authUser = new AuthUserEntity();
              authUser.setUsername(user.username());
              authUser.setPassword(pe.encode("12345"));
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
              authUserRepository.create(authUser);
              UserEntity requester = UserEntity.fromJson(user);
//              userdataUserDAO.create(requester);
              userdataUserRepository.createWithFriendship(requester, addressee);
              return requester;
            }
        )
    );
  }
}
