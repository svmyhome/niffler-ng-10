package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.AuthUserEntity;
import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.data.entity.AuthorityEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.tpl.ChainedTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import org.junit.jupiter.api.Test;
import utils.RandomDataUtils;

public class ChainedTransactionTest {

  private static final Config CFG = Config.getInstance();

  @Test
  void chainedTransactionSuccess() {
    String userName = RandomDataUtils.randomUsername();
    ChainedTransactionTemplate chainedTx = new ChainedTransactionTemplate(
        CFG.authJdbcUrl(),
        CFG.userdataJdbcUrl()
    );

    UserEntity createdUser = chainedTx.execute(
        () -> {
          AuthUserEntity authUser = new AuthUserEntity();
          authUser.setUsername(userName);
          authUser.setPassword("12345");
          authUser.setEnabled(true);
          authUser.setAccountNonExpired(true);
          authUser.setAccountNonLocked(true);
          authUser.setCredentialsNonExpired(true);

          AuthUserEntity createdAuthUser = new AuthUserDaoJdbc().create(authUser);

          AuthorityEntity readAuth = new AuthorityEntity();
          readAuth.setUserId(createdAuthUser.getId());
          readAuth.setAuthority(Authority.read);

          AuthorityEntity writeAuth = new AuthorityEntity();
          writeAuth.setUserId(createdAuthUser.getId());
          writeAuth.setAuthority(Authority.write);
          new AuthAuthorityDaoJdbc().create(readAuth, writeAuth);
          return null;
        },
        () -> {
          UserEntity udUser = new UserEntity();
          udUser.setUsername(userName);
          udUser.setCurrency(CurrencyValues.RUB);
          udUser.setFirstname(RandomDataUtils.randomFirstName());
          udUser.setSurname(RandomDataUtils.randomLastName());
          udUser.setFullname(RandomDataUtils.randomFullName());
          return new UserdataUserDaoJdbc().create(udUser);
        }
    );
  }
}
