package guru.qa.niffler.data.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.AuthorityEntity;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

public class AuthAuthorityDaoSpringJdbc implements AuthAuthorityDao {

  private final DataSource dataSource;

  public AuthAuthorityDaoSpringJdbc(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void create(AuthorityEntity... authority) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.batchUpdate(
        "INSERT INTO authority (user_id, authority)" +
            "VALUES(?,?)",
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setObject(1, authority[i].getUserId());
            ps.setObject(2, authority[i].getAuthority().name());
          }

          @Override
          public int getBatchSize() {
            return authority.length;
          }
        }
    );

  }

  @Override
  public AuthorityEntity create(AuthorityEntity authority) {
    return null;
  }

  @Override
  public List<AuthorityEntity> findAuthoritiesByUserId(UUID id) {
    return List.of();
  }

  @Override
  public Optional<AuthorityEntity> findById(UUID id) {
    return Optional.empty();
  }

  @Override
  public void delete(AuthorityEntity authority) {

  }
}
