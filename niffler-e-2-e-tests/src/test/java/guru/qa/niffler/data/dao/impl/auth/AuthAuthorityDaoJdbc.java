package guru.qa.niffler.data.dao.impl.auth;

import static guru.qa.niffler.data.tpl.Connections.holder;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.auth.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public void create(AuthorityEntity... authorities) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "INSERT INTO authority (user_id, authority) VALUES(?,?)"
    )) {
      for (AuthorityEntity authority : authorities) {
        ps.setObject(1, authority.getUser().getId());
        ps.setString(2, authority.getAuthority().name());
        ps.addBatch();
      }
      ps.executeBatch();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<AuthorityEntity> findAllByUserId(UUID userId) {
    List<AuthorityEntity> authorities = new ArrayList<>();
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "SELECT * FROM authority WHERE user_id = ?"
    )) {
      ps.setObject(1, userId);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setId(rs.getObject("id", UUID.class));
          AuthUserEntity aue = new AuthUserEntity();
          aue.setId(rs.getObject("user_id", UUID.class));
          ae.setUser(aue);
          ae.setAuthority(Authority.valueOf(rs.getString("authority")));
          authorities.add(ae);
        }
        return authorities;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthorityEntity> findById(UUID id) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "SELECT * FROM authority WHERE id = ?"
    )) {
      ps.setObject(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setId(rs.getObject("id", UUID.class));
          AuthUserEntity aue = new AuthUserEntity();
          aue.setId(rs.getObject("user_id", UUID.class));
          ae.setUser(aue);
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
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "DELETE FROM authority WHERE user_id = ?"
    )) {
      for (AuthorityEntity authority : authorities) {
        ps.setObject(1, authority.getUser().getId());
        ps.addBatch();
      }
      ps.executeBatch();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<AuthorityEntity> findAll() {
    List<AuthorityEntity> authorities = new ArrayList<>();
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "SELECT * FROM authority"
    )) {
      try (ResultSet rs = ps.executeQuery()) {
        AuthorityEntity ae = new AuthorityEntity();
        while (rs.next()) {
          ae.setId(rs.getObject("id", UUID.class));
          AuthUserEntity aue = new AuthUserEntity();
          aue.setId(rs.getObject("user_id", UUID.class));
          ae.setUser(aue);
          ae.setAuthority(Authority.valueOf(rs.getString("authority")));
          authorities.add(ae);
        }
        return authorities;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
