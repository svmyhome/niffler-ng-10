package guru.qa.niffler.data.impl;

import guru.qa.niffler.data.dao.UserdataUserDAO;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.mapper.UserdataUserEntityRowMapper;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class UserdataUserDaoSpringJdbc implements UserdataUserDAO {

  private final DataSource dataSource;

  public UserdataUserDaoSpringJdbc(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public UserEntity create(UserEntity user) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    KeyHolder kh = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(
          "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name)"
              +
              "VALUES(?,?,?,?,?,?,?)",
          Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getCurrency().name());
      ps.setString(3, user.getFirstname());
      ps.setString(4, user.getSurname());
      ps.setBytes(5, user.getPhoto());
      ps.setBytes(6, user.getPhotoSmall());
      ps.setString(7, user.getFullname());
      return ps;
    }, kh);

    final UUID generatedKey = (UUID) kh.getKeys().get("id");
    user.setId(generatedKey);
    return user;
  }

  @Override
  public Optional<UserEntity> findById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT * FROM \"user\" WHERE id = ?",
            UserdataUserEntityRowMapper.instance,
            id
        )
    );
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.ofNullable(jdbcTemplate.queryForObject(
        "SELECT * FROM \"user\" WHERE username = ?",
        UserdataUserEntityRowMapper.instance,
        username
    ));
  }

  @Override
  public void delete(UserEntity user) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.update(
        "DELETE FROM \"user\" WHERE id = ?",
        user.getId()
    );
  }

  @Override
  public List<UserEntity> findAll() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.query(
        "SELECT * FROM \"user\"",
        UserdataUserEntityRowMapper.instance
    );
  }
}