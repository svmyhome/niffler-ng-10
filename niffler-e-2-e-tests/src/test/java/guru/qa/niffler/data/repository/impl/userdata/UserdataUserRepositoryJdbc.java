package guru.qa.niffler.data.repository.impl.userdata;

import static guru.qa.niffler.data.tpl.Connections.holder;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.userdata.UserdataUserDao;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.userdata.UserdataUserRepository;
import guru.qa.niffler.model.spend.CurrencyValues;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

  private static final Config CFG = Config.getInstance();

  @Override
  public UserEntity create(UserEntity user) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "INSERT INTO \"user\" (username, currency, firstname, surname, full_name, photo, photo_small)"
            +
            "VALUES(?,?,?,?,?,?,?)",
        Statement.RETURN_GENERATED_KEYS
    )) {
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getCurrency().name());
      ps.setString(3, user.getFirstname());
      ps.setString(4, user.getSurname());
      ps.setString(5, user.getFullname());
      ps.setBytes(6, user.getPhoto());
      ps.setBytes(7, user.getPhotoSmall());
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
  public UserEntity createWithFriendship(UserEntity requester, UserEntity addressee) {
    try (PreparedStatement userPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "INSERT INTO \"user\" (username, currency, firstname, surname, full_name, photo, photo_small)"
            +
            "VALUES(?,?,?,?,?,?,?)",
        Statement.RETURN_GENERATED_KEYS
    );
        PreparedStatement friendPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "INSERT INTO friendship (requester_id, addressee_id, status, created_date) VALUES (?, ?, ?,?)")) {
      userPs.setString(1, requester.getUsername());
      userPs.setString(2, requester.getCurrency().name());
      userPs.setString(3, requester.getFirstname());
      userPs.setString(4, requester.getSurname());
      userPs.setString(5, requester.getFullname());
      userPs.setBytes(6, requester.getPhoto());
      userPs.setBytes(7, requester.getPhotoSmall());
      userPs.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = userPs.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can't find id in ResultSet");
        }
      }
      requester.setId(generatedKey);

      friendPs.setObject(1, generatedKey);
      friendPs.setObject(2, addressee.getId());
      friendPs.setString(3, FriendshipStatus.ACCEPTED.name());
      friendPs.setDate(4, new java.sql.Date(new Date().getTime()));
      friendPs.executeUpdate();
      return requester;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UserEntity> findById(UUID id) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "SELECT * FROM \"user\" WHERE id = ?"
    )) {
      ps.setObject(1, id);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          UserEntity ue = new UserEntity();
          ue.setId(rs.getObject("id", UUID.class));
          ue.setUsername(rs.getString("username"));
          String currencyString = rs.getString("currency");
          ue.setCurrency(CurrencyValues.valueOf(currencyString));
          ue.setFirstname(rs.getString("firstname"));
          ue.setSurname(rs.getString("surname"));
          ue.setFullname(rs.getString("full_name"));
          ue.setPhoto(rs.getBytes("photo"));
          ue.setPhotoSmall(rs.getBytes("photo_small"));
          return Optional.of(ue);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "SELECT * FROM \"user\" WHERE username = ?"
    )) {
      ps.setObject(1, username);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          UserEntity ue = new UserEntity();
          ue.setId(rs.getObject("id", UUID.class));
          ue.setUsername(rs.getString("username"));
          String currencyString = rs.getString("currency");
          ue.setCurrency(CurrencyValues.valueOf(currencyString));
          ue.setFirstname(rs.getString("firstname"));
          ue.setSurname(rs.getString("surname"));
          ue.setFullname(rs.getString("full_name"));
          ue.setPhoto(rs.getBytes("photo"));
          ue.setPhotoSmall(rs.getBytes("photo_small"));
          return Optional.of(ue);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(UserEntity user) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "DELETE FROM \"user\" WHERE id =?"
    )) {
      ps.setObject(1, user.getId());
      ps.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<UserEntity> findAll() {
    List<UserEntity> userEntities = new ArrayList<>();
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "SELECT * FROM \"user\""
    )) {
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          UserEntity ue = new UserEntity();
          while (rs.next()) {
            ue.setId(rs.getObject("id", UUID.class));
            ue.setUsername(rs.getString("username"));
            String currencyString = rs.getString("currency");
            ue.setCurrency(CurrencyValues.valueOf(currencyString));
            ue.setFirstname(rs.getString("firstname"));
            ue.setSurname(rs.getString("surname"));
            ue.setFullname(rs.getString("full_name"));
            ue.setPhoto(rs.getBytes("photo"));
            ue.setPhotoSmall(rs.getBytes("photo_small"));
            userEntities.add(ue);
          }
          return userEntities;
        } else {
          throw new SQLException("Can't find in ResultSet");
        }
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "INSERT INTO friendship (requester_id, addressee_id, status, created_date) VALUES (?, ?, ?,?)"
    )) {
      ps.setObject(1, requester.getId());
      ps.setObject(2, addressee.getId());
      ps.setString(3, FriendshipStatus.PENDING.name());
      ps.setDate(4, new java.sql.Date(new Date().getTime()));
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "INSERT INTO friendship (requester_id, addressee_id, status, created_date) VALUES (?, ?, ?,?)"
    )) {
      ps.setObject(1, requester.getId());
      ps.setObject(2, addressee.getId());
      ps.setString(3, FriendshipStatus.PENDING.name());
      ps.setDate(4, new java.sql.Date(new Date().getTime()));
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void addFriend(UserEntity requester, UserEntity addressee) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "INSERT INTO friendship (requester_id, addressee_id, status, created_date) VALUES (?, ?, ?,?)"
    )) {
      ps.setObject(1, requester.getId());
      ps.setObject(2, addressee.getId());
      ps.setString(3, FriendshipStatus.ACCEPTED.name());
      ps.setDate(4, new java.sql.Date(new Date().getTime()));
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
