package guru.qa.niffler.data.repository.impl.userdata;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.extractor.UserdataUserEntityResultSetExtractor;
import guru.qa.niffler.data.mapper.userdata.UserdataUserEntityRowMapper;
import guru.qa.niffler.data.repository.userdata.UserdataUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class UserdataUserRepositorySpringJdbc implements UserdataUserRepository {

  private static final Config CFG = Config.getInstance();

  @Override
  public UserEntity create(UserEntity user) {
    JdbcTemplate template = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(conn -> {
      PreparedStatement ps = conn.prepareStatement(
          """
              INSERT INTO "user" (username, currency, firstname, surname, photo, photo_small, full_name)
              VALUES ( ?, ?, ?, ?, ?, ?, ?)
              """,
          Statement.RETURN_GENERATED_KEYS
      );
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getCurrency().name());
      ps.setString(3, user.getFirstname());
      ps.setString(4, user.getSurname());
      ps.setObject(5, user.getPhoto());
      ps.setObject(6, user.getPhotoSmall());
      ps.setString(7, user.getFullname());
      return ps;
    }, keyHolder);
    UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
    user.setId(generatedKey);
    return user;
  }

  @Override
  public Optional<UserEntity> findById(UUID id) {
    JdbcTemplate template = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    return Optional.ofNullable(template.query(
        """
            SELECT u.*, f.*
            FROM "user" u
            LEFT JOIN friendship f ON u.id IN (f.addressee_id, f.requester_id)
            WHERE u.id = ?
            """,
        UserdataUserEntityResultSetExtractor.instance,
        id
    ));
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
    JdbcTemplate template = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    return Optional.ofNullable(template.query("SELECT * FROM \"user\" WHERE username = ?",
        UserdataUserEntityResultSetExtractor.instance,
        username
    ));
  }

  @Override
  public void delete(UserEntity user) {
    JdbcTemplate template = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    template.update(
        "DELETE FROM \"user\" WHERE id = ?",
        user.getId()
    );
  }

  @Override
  public List<UserEntity> findAll() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    return jdbcTemplate.query(
        "SELECT * FROM \"user\"",
        UserdataUserEntityRowMapper.instance
    );
  }

  @Override
  public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
    createFriendship(requester, addressee, FriendshipStatus.PENDING);
  }

  @Override
  public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
    createFriendship(addressee, requester, FriendshipStatus.PENDING);
  }

  @Override
  public void addFriend(UserEntity requester, UserEntity addressee) {
    createFriendship(requester, addressee, FriendshipStatus.ACCEPTED);
    createFriendship(addressee, requester, FriendshipStatus.ACCEPTED);
  }

  private void createFriendship(UserEntity firstFriend, UserEntity secondFriend,
      FriendshipStatus status) {
    JdbcTemplate template = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    template.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          """
              INSERT INTO friendship (requester_id, addressee_id, status)
              VALUES (?, ?, ?)
              """
      );
      ps.setObject(1, firstFriend.getId());
      ps.setObject(2, secondFriend.getId());
      ps.setString(3, status.name());
      return ps;
    });
  }
}
