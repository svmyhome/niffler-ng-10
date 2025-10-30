package guru.qa.niffler.data.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.data.entity.AuthorityEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

  private final Connection connection;

  public AuthAuthorityDaoJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void create(AuthorityEntity... authorities) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO authority (user_id, authority)" +
            "VALUES(?,?)",
        PreparedStatement.RETURN_GENERATED_KEYS
    )) {
      for (AuthorityEntity authority : authorities) {
        ps.setObject(1, authority.getUserId());
        ps.setString(2, authority.getAuthority().name());
        ps.addBatch();
        ps.clearParameters();
      }
      ps.executeBatch();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<AuthorityEntity> findAllByUserId(UUID userId) {
    List<AuthorityEntity> authorities = new ArrayList<>();
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT * FROM authority WHERE user_id = ?"
    )) {
      ps.setObject(1, userId);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          while (rs.next()) {
            AuthorityEntity ae = new AuthorityEntity();
            ae.setId(rs.getObject("id", UUID.class));
            ae.setUserId(rs.getObject("user_id", UUID.class));
            ae.setAuthority(Authority.valueOf(rs.getString("authority")));
            authorities.add(ae);
          }
          return authorities;
        } else {
          throw new SQLException("Can't find authority in ResultSet");
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthorityEntity> findById(UUID id) {
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT * FROM authority WHERE id = ?"
    )) {
      ps.setObject(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setId(rs.getObject("id", UUID.class));
          ae.setUserId(rs.getObject("user_id", UUID.class));
          ae.setAuthority(Authority.valueOf(rs.getString("authority")));
          return Optional.of(ae);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(AuthorityEntity... authorities) {
    try (PreparedStatement ps = connection.prepareStatement(
        "DELETE FROM authority WHERE user_id = ?"
    )) {
      for (AuthorityEntity authority : authorities) {
        ps.setObject(1, authority.getUserId());
        ps.addBatch();
        ps.clearParameters();
      }
      ps.executeBatch();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<AuthorityEntity> findAll() {
    List<AuthorityEntity> authorities = new ArrayList<>();
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT * FROM authority"
    )) {
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          AuthorityEntity ae = new AuthorityEntity();
          while (rs.next()) {
            ae.setId(rs.getObject("id", UUID.class));
            ae.setUserId(rs.getObject("user_id", UUID.class));
            ae.setAuthority(Authority.valueOf(rs.getString("authority")));
            authorities.add(ae);
          }
          return authorities;
        } else {
          throw new SQLException("Can't find in ResultSet");
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
