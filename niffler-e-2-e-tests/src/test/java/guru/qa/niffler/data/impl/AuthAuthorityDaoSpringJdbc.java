package guru.qa.niffler.data.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.AuthorityEntity;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;

import guru.qa.niffler.data.mapper.AuthAuthorityEntityRowMapper;
import guru.qa.niffler.data.mapper.SpendEntityRowMapper;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

public class AuthAuthorityDaoSpringJdbc implements AuthAuthorityDao {

  private final DataSource dataSource;

  public AuthAuthorityDaoSpringJdbc(DataSource dataSource) {
    this.dataSource = dataSource;
  }

    @Override
    public AuthorityEntity create(AuthorityEntity authority) {
        return null;
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
  public List<AuthorityEntity> findAllByUserId(UUID userId) {
      JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

      return jdbcTemplate.query(
              "SELECT * FROM authority WHERE user_id = ?",
              AuthAuthorityEntityRowMapper.instance,
              userId
      );  }

  @Override
  public Optional<AuthorityEntity> findById(UUID id) {
      JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
      return Optional.ofNullable(
              jdbcTemplate.queryForObject(
                      "SELECT * FROM authority WHERE id = ?",
                      AuthAuthorityEntityRowMapper.instance,
                      id
              )
      );
  }

  @Override
  public void delete(AuthorityEntity authority) {
      throw new UnsupportedOperationException("Method updateCategory() is not implemented yet");
  }
}
