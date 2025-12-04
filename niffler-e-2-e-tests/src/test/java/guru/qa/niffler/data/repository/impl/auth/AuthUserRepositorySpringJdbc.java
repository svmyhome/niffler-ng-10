package guru.qa.niffler.data.repository.impl.auth;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.extractor.AuthUserEntityListResultSetExtractor;
import guru.qa.niffler.data.extractor.AuthUserEntityResultSetExtractor;
import guru.qa.niffler.data.repository.auth.AuthUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class AuthUserRepositorySpringJdbc implements AuthUserRepository {

  private static final Config CFG = Config.getInstance();

  @Override
  public AuthUserEntity create(AuthUserEntity user) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    KeyHolder kh = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
          PreparedStatement ps = con.prepareStatement(
              "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)"
                  +
                  "VALUES(?,?,?,?,?,?)",
              Statement.RETURN_GENERATED_KEYS
          );
          ps.setString(1, user.getUsername());
          ps.setString(2, user.getPassword());
          ps.setBoolean(3, user.getEnabled());
          ps.setBoolean(4, user.getAccountNonExpired());
          ps.setBoolean(5, user.getAccountNonLocked());
          ps.setBoolean(6, user.getCredentialsNonExpired());
          return ps;
        }, kh
    );
    final UUID generatedKey = (UUID) kh.getKeys().get("id");
    user.setId(generatedKey);
    return user;
  }

  @Override
  public Optional<AuthUserEntity> findByUsername(String username) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    return Optional.ofNullable(jdbcTemplate.query(
        "SELECT * FROM \"user\" u JOIN authority a on u.id = a.user_id WHERE username = ?",
        AuthUserEntityResultSetExtractor.instance,
        username
    ));
  }

  @Override
  public Optional<AuthUserEntity> findById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    return Optional.ofNullable(jdbcTemplate.query(
        "SELECT " +
            "u.id as user_id, " +
            "u.username, " +
            "u.password, " +
            "u.enabled, " +
            "u.account_non_expired, " +
            "u.account_non_locked, " +
            "u.credentials_non_expired, " +
            "a.id as authority_id, " +
            "a.authority " +
            "FROM \"user\" u " +
            "JOIN authority a ON u.id = a.user_id " +
            "WHERE u.id = ?",
        AuthUserEntityResultSetExtractor.instance,
        id
    ));
  }

  @Override
  public void delete(AuthUserEntity user) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    jdbcTemplate.update(
        "DELETE FROM \"user\" WHERE id = ?",
        user.getId()
    );
  }

  @Override
  public List<AuthUserEntity> findAll() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    return jdbcTemplate.query(
        "SELECT * FROM \"user\" u JOIN public.authority a on u.id = a.user_id",
        AuthUserEntityListResultSetExtractor.instance
    );
  }
}
