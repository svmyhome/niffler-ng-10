package guru.qa.niffler.data.repository.impl.auth;

import static guru.qa.niffler.data.tpl.Connections.holder;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.auth.AuthUserEntityRowMapper;
import guru.qa.niffler.data.repository.auth.AuthUserRepository;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthUserRepositoryJdbc implements AuthUserRepository {

  private static final Config CFG = Config.getInstance();

  @Override
  public AuthUserEntity create(AuthUserEntity user) {
    try (PreparedStatement userPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)"
            +
            "VALUES(?,?,?,?,?,?)",
        Statement.RETURN_GENERATED_KEYS
    );
        PreparedStatement authorityPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(
            "INSERT INTO authority (user_id, authority) VALUES(?,?)")) {
      userPs.setString(1, user.getUsername());
      userPs.setString(2, user.getPassword());
      userPs.setBoolean(3, user.getEnabled());
      userPs.setBoolean(4, user.getAccountNonExpired());
      userPs.setBoolean(5, user.getAccountNonLocked());
      userPs.setBoolean(6, user.getCredentialsNonExpired());

      userPs.executeUpdate();
      final UUID generatedKey;
      try (ResultSet rs = userPs.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can't find id in ResultSet");
        }
      }
      user.setId(generatedKey);

      for (AuthorityEntity a : user.getAuthorities()) {
        authorityPs.setObject(1, generatedKey);
        authorityPs.setString(2, a.getAuthority().name());
        authorityPs.addBatch();
      }
      authorityPs.executeBatch();

      return user;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public AuthUserEntity update(AuthUserEntity user) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "UPDATE \"user\" SET username=?, password=?, enabled=?, " +
            "account_non_expired=?, account_non_locked=?, credentials_non_expired=? " +
            "WHERE id=?"
    )) {
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getPassword());
      ps.setBoolean(3, user.getEnabled());
      ps.setBoolean(4, user.getAccountNonExpired());
      ps.setBoolean(5, user.getAccountNonLocked());
      ps.setBoolean(6, user.getCredentialsNonExpired());
      ps.setObject(7, user.getId());

      int rows = ps.executeUpdate();
      if (rows == 0) {
        throw new RuntimeException("User not found with id: " + user.getId());
      }
      return user;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthUserEntity> findByUsername(String username) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "SELECT " +
            "u.id as user_id, u.username, u.password, u.enabled, " +
            "u.account_non_expired, u.account_non_locked, u.credentials_non_expired, " +
            "a.id as authority_id, a.authority " +
            "FROM \"user\" u JOIN authority a on u.id = a.user_id WHERE u.username = ?"
    )) {
      ps.setString(1, username);
      try (ResultSet rs = ps.executeQuery()) {
        AuthUserEntity user = null;
        List<AuthorityEntity> authorityEntities = new ArrayList<>();
        while (rs.next()) {
          if (user == null) {
            user = AuthUserEntityRowMapper.instance.mapRow(rs, 1);
          }
          AuthorityEntity ae = new AuthorityEntity();
          ae.setUser(user);
          ae.setId(rs.getObject("authority_id", UUID.class));
          ae.setAuthority(Authority.valueOf(rs.getString("authority")));
          authorityEntities.add(ae);
        }
        if (user == null) {
          return Optional.empty();
        } else {
          user.setAuthorities(authorityEntities);
          return Optional.of(user);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthUserEntity> findById(UUID id) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "SELECT " +
            "u.id as user_id, u.username, u.password, u.enabled, " +
            "u.account_non_expired, u.account_non_locked, u.credentials_non_expired, " +
            "a.id as authority_id, a.authority " +
            "FROM \"user\" u JOIN authority a on u.id = a.user_id WHERE u.id = ?"
    )) {
      ps.setObject(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        AuthUserEntity user = null;
        List<AuthorityEntity> authorityEntities = new ArrayList<>();
        while (rs.next()) {
          if (user == null) {
            user = AuthUserEntityRowMapper.instance.mapRow(rs, 1);
          }
          AuthorityEntity ae = new AuthorityEntity();
          ae.setUser(user);
          ae.setId(rs.getObject("authority_id", UUID.class));
          ae.setAuthority(Authority.valueOf(rs.getString("authority")));
          authorityEntities.add(ae);
        }
        if (user == null) {
          return Optional.empty();
        } else {
          user.setAuthorities(authorityEntities);
          return Optional.of(user);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void remove(AuthUserEntity user) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "DELETE FROM \"user\" WHERE id = ?"
    )) {
      ps.setObject(1, user.getId());
      ps.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<AuthUserEntity> findAll() {
    List<AuthUserEntity> users = new ArrayList<>();
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "SELECT * FROM \"user\" u JOIN public.authority a on u.id = a.user_id"
    )) {
      try (ResultSet rs = ps.executeQuery()) {
        AuthUserEntity user = null;
        List<AuthorityEntity> authorityEntities = new ArrayList<>();
        while (rs.next()) {
          if (user == null) {
            user = AuthUserEntityRowMapper.instance.mapRow(rs, 1);
          }

          AuthorityEntity ae = new AuthorityEntity();
          ae.setUser(user);
          ae.setId(rs.getObject("id", UUID.class));
          ae.setAuthority(Authority.valueOf(rs.getString("authority")));
          authorityEntities.add(ae);
          user.setAuthorities(authorityEntities);
          users.add(user);
        }
        return users;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}