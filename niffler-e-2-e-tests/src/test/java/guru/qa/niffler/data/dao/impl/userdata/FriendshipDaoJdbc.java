package guru.qa.niffler.data.dao.impl.userdata;

import static guru.qa.niffler.data.tpl.Connections.holder;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.userdata.FriendshipDao;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FriendshipDaoJdbc implements FriendshipDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public void create(FriendshipEntity friendship) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "INSERT INTO friendship (requester_id, addressee_id, status, created_date) VALUES (?, ?, ?,?)"
    )) {
      ps.setObject(1, friendship.getRequester().getId());
      ps.setObject(2, friendship.getAddressee().getId());
      ps.setString(3, friendship.getStatus().name());
      ps.setDate(4, new java.sql.Date(friendship.getCreatedDate().getTime()));
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<FriendshipEntity> findByRequester(UUID requesterId) {
    List<FriendshipEntity> result = new ArrayList<>();
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "SELECT * FROM friendship WHERE requester_id = ?"
    )) {
      ps.setObject(1, requesterId);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          FriendshipEntity friendship = new FriendshipEntity();
          UUID requesterFromDb = rs.getObject("requester_id", UUID.class);
          UserEntity uer = new UserEntity();
          uer.setId(requesterFromDb);
          UUID addresseeFromDb = rs.getObject("addressee_id", UUID.class);
          UserEntity uea = new UserEntity();
          uea.setId(addresseeFromDb);
          friendship.setRequester(uer);
          friendship.setAddressee(uea);
          friendship.setStatus(FriendshipStatus.valueOf(rs.getString("status")));
          friendship.setCreatedDate(rs.getDate("created_date"));
          result.add(friendship);
        }
        if (result.isEmpty()) {
          return List.of();
        } else {
          return result;
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<FriendshipEntity> findByAddressee(UUID addresseeId) {
    List<FriendshipEntity> result = new ArrayList<>();
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "SELECT * FROM friendship WHERE addressee_id = ?"
    )) {
      ps.setObject(1, addresseeId);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          FriendshipEntity friendship = new FriendshipEntity();
          UUID requesterFromDb = rs.getObject("requester_id", UUID.class);
          UserEntity uer = new UserEntity();
          uer.setId(requesterFromDb);
          UUID addresseeFromDb = rs.getObject("addressee_id", UUID.class);
          UserEntity uea = new UserEntity();
          uea.setId(addresseeFromDb);
          friendship.setRequester(uer);
          friendship.setAddressee(uea);
          friendship.setStatus(FriendshipStatus.valueOf(rs.getString("status")));
          friendship.setCreatedDate(rs.getDate("created_date"));
          result.add(friendship);
        }
        if (result.isEmpty()) {
          return List.of();
        } else {
          return result;
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(FriendshipEntity friendship) {
    try(PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "DELETE FROM friendship WHERE requester_id = ? AND addressee_id = ?"
    )) {
      ps.setObject(1, friendship.getRequester().getId());
      ps.setObject(2, friendship.getAddressee().getId());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
