package guru.qa.niffler.data.impl;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.AuthUserEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoJdbc implements AuthUserDao {

  private final Connection connection;

  public AuthUserDaoJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public AuthUserEntity create(AuthUserEntity user) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)"
            +
            "VALUES(?,?,?,?,?,?)",
        Statement.RETURN_GENERATED_KEYS
    )) {
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getPassword());
      ps.setBoolean(3, user.getEnabled());
      ps.setBoolean(4, user.getAccountNonExpired());
      ps.setBoolean(5, user.getAccountNonLocked());
      ps.setBoolean(6, user.getCredentialsNonExpired());

      ps.executeUpdate();
      final UUID generatedKey;
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can't find id in ResultSet");
        }
      }
      user.setId(generatedKey);
      return user;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthUserEntity> findByUsername(String username) {
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT * FROM \"user\" WHERE username = ?"
    )) {
      ps.setString(1, username);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          AuthUserEntity aue = new AuthUserEntity();
          aue.setId(rs.getObject("id", UUID.class));
          aue.setPassword(rs.getString("password"));
          aue.setUsername(rs.getString("username"));
          aue.setEnabled(rs.getBoolean("enabled"));
          aue.setAccountNonExpired(rs.getBoolean("account_non_expired"));
          aue.setAccountNonLocked(rs.getBoolean("account_non_locked"));
          aue.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
          return Optional.of(aue);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthUserEntity> findById(UUID id) {
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT * FROM \"user\" WHERE id = ?"
    )) {
      ps.setObject(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          AuthUserEntity aue = new AuthUserEntity();
          aue.setId(rs.getObject("id", UUID.class));
          aue.setPassword(rs.getString("password"));
          aue.setUsername(rs.getString("username"));
          aue.setEnabled(rs.getBoolean("enabled"));
          aue.setAccountNonExpired(rs.getBoolean("account_non_expired"));
          aue.setAccountNonLocked(rs.getBoolean("account_non_locked"));
          aue.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
          return Optional.of(aue);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(AuthUserEntity user) {
    try (PreparedStatement ps = connection.prepareStatement(
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
    List<AuthUserEntity> authUserEntities = new ArrayList<>();
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT * FROM \"user\""
    )) {
      AuthUserEntity aue = new AuthUserEntity();
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          while (rs.next()) {
            aue.setId(rs.getObject("id", UUID.class));
            aue.setUsername(rs.getString("username"));
            aue.setPassword(rs.getString("password"));
            aue.setEnabled(rs.getBoolean("enabled"));
            aue.setAccountNonExpired(rs.getBoolean("account_non_expired"));
            aue.setAccountNonLocked(rs.getBoolean("account_non_locked"));
            aue.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
            authUserEntities.add(aue);
          }
          return authUserEntities;
        } else {
          throw new SQLException("Can't find authUserEntity in ResultSet");
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}