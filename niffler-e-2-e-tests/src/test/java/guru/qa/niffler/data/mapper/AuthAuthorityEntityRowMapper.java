package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.data.entity.AuthorityEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;

public class AuthAuthorityEntityRowMapper implements RowMapper<AuthorityEntity> {

  public static final AuthAuthorityEntityRowMapper instance = new AuthAuthorityEntityRowMapper();

  private AuthAuthorityEntityRowMapper() {
  }

  @Override
  public AuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    AuthorityEntity authorityEntity = new AuthorityEntity();
    authorityEntity.setId(rs.getObject("id", UUID.class));
    authorityEntity.setUserId(rs.getObject("user_id", UUID.class));
    authorityEntity.setAuthority(Authority.valueOf(rs.getString("authority")));
    return authorityEntity;
  }
}
