package guru.qa.niffler.data.repository.impl.userdata;

import static guru.qa.niffler.data.tpl.Connections.holder;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.userdata.UserdataUserEntityRowMapper;
import guru.qa.niffler.data.repository.userdata.UserdataUserRepository;
import guru.qa.niffler.model.spend.CurrencyValues;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

  private static final Config CFG = Config.getInstance();

  @Override
  @Nonnull
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
  public Optional<UserEntity> findById(UUID id) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        """
            SELECT u.*, f.*
            FROM "user" u
            LEFT JOIN friendship f ON u.id IN (f.addressee_id, f.requester_id)
            WHERE u.id = ?
            """
    )) {
      ps.setObject(1, id);
      ps.execute();
      UserEntity user = null;
      var friendshipAddressees = new ArrayList<FriendshipEntity>();
      var friendshipRequests = new ArrayList<FriendshipEntity>();
      try (ResultSet resultSet = ps.getResultSet()) {
        while (resultSet.next()) {
          UUID userId = resultSet.getObject("id", UUID.class);
          if (user == null) {
            user = UserdataUserEntityRowMapper.instance.mapRow(resultSet, 1);
          }
          if (userId.equals(id)) {
            var requester = new UserEntity();
            var addressee = new UserEntity();
            UUID requesterId = resultSet.getObject("requester_id", UUID.class);
            UUID addresseeId = resultSet.getObject("addressee_id", UUID.class);
            requester.setId(requesterId);
            addressee.setId(addresseeId);
            var friendship = new FriendshipEntity();
            friendship.setRequester(requester);
            friendship.setAddressee(addressee);
            friendship.setStatus(FriendshipStatus.valueOf(resultSet.getString("status")));
            friendship.setCreatedDate(resultSet.getDate("created_date"));
            if (addresseeId.equals(userId)) {
              friendshipAddressees.add(friendship);
            } else {
              friendshipRequests.add(friendship);
            }
          }
        }
        if (user == null) {
          return Optional.empty();
        } else {
          user.setFriendshipAddressees(friendshipAddressees);
          user.setFriendshipRequests(friendshipRequests);
          return Optional.of(user);
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
  @Nonnull
  public UserEntity update(UserEntity user) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "UPDATE \"user\" SET username =?, currency=?, firstname=?, surname=?, full_name=?, photo=?, photo_small=? WHERE id =?"
    )) {
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getCurrency().name());
      ps.setString(3, user.getFirstname());
      ps.setString(4, user.getSurname());
      ps.setString(5, user.getFullname());
      ps.setBytes(6, user.getPhoto());
      ps.setBytes(7, user.getPhotoSmall());
      ps.setObject(8, user.getId());
      ps.executeUpdate();
      return user;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void remove(UserEntity user) {
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
  @Nonnull
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
  public void sendInvitation(UserEntity requester, UserEntity addressee) {
    createFriendship(requester, addressee, FriendshipStatus.PENDING);
  }

  @Override
  public void addFriend(UserEntity requester, UserEntity addressee) {
    createFriendship(requester, addressee, FriendshipStatus.ACCEPTED);
    createFriendship(addressee, requester, FriendshipStatus.ACCEPTED);
  }

  private void createFriendship(UserEntity firstFriend, UserEntity secondFriend,
      FriendshipStatus status) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "INSERT INTO friendship (requester_id, addressee_id, status, created_date) VALUES (?, ?, ?,?)"
    )) {
      ps.setObject(1, firstFriend.getId());
      ps.setObject(2, secondFriend.getId());
      ps.setString(3, status.name());
      ps.setDate(4, new java.sql.Date(new Date().getTime()));
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
