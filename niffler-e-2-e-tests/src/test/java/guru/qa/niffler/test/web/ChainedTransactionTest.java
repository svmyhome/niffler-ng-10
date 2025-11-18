package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.dao.impl.auth.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.auth.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.userdata.UserdataUserDaoJdbc;
import guru.qa.niffler.data.tpl.ChainedTransactionTemplate;
import guru.qa.niffler.model.spend.CurrencyValues;
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
          readAuth.setUser(createdAuthUser);
          readAuth.setAuthority(Authority.read);

          AuthorityEntity writeAuth = new AuthorityEntity();
          writeAuth.setUser(createdAuthUser);
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
