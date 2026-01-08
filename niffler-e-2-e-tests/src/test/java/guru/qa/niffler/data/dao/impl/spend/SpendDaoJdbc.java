package guru.qa.niffler.data.dao.impl.spend;

import static guru.qa.niffler.data.tpl.Connections.holder;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.spend.SpendDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.spend.CurrencyValues;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SpendDaoJdbc implements SpendDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public @Nonnull SpendEntity create(SpendEntity spend) {

    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
        "INSERT INTO spend (username, spend_date, currency, amount, description, category_id)" +
            "VALUES(?,?,?,?,?,?)",
        Statement.RETURN_GENERATED_KEYS
    )) {
      ps.setString(1, spend.getUsername());
      ps.setDate(2, new java.sql.Date(spend.getSpendDate().getTime()));
      ps.setString(3, spend.getCurrency().name());
      ps.setDouble(4, spend.getAmount());
      ps.setString(5, spend.getDescription());
      ps.setObject(6, spend.getCategory().getId());
      ps.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can't find id in ResultSet");
        }
      }
      spend.setId(generatedKey);
      return spend;

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<SpendEntity> findById(UUID id) {
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
        "SELECT * FROM spend WHERE id = ?"
    )) {
      ps.setObject(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          SpendEntity se = new SpendEntity();
          se.setId(rs.getObject("id", UUID.class));
          se.setUsername(rs.getString("username"));
          se.setSpendDate(rs.getDate("spend_date"));
          se.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
          se.setAmount(rs.getDouble("amount"));
          se.setDescription(rs.getString("description"));
          UUID categoryId = rs.getObject("category_id", UUID.class);
          CategoryEntity category = new CategoryEntity();
          category.setId(categoryId);
          se.setCategory(category);
          return Optional.of(se);
        } else {
          return Optional.empty();
        }
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public @Nonnull List<SpendEntity> findAllByCategoryId(UUID categoryId) {
    List<SpendEntity> entityList = new ArrayList<>();
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
        "SELECT * FROM spend WHERE category_id = ?"
    )) {
      ps.setObject(1, categoryId);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          SpendEntity se = new SpendEntity();
          se.setId(rs.getObject("id", UUID.class));
          se.setUsername(rs.getString("username"));
          se.setSpendDate(rs.getDate("spend_date"));
          se.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
          se.setAmount(rs.getDouble("amount"));
          se.setDescription(rs.getString("description"));
          CategoryEntity category = new CategoryEntity();
          category.setId(categoryId);
          se.setCategory(category);
          entityList.add(se);
        }
        return entityList;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public @Nonnull List<SpendEntity> findAllByUsername(String username) {
    List<SpendEntity> entityList = new ArrayList<>();
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
        "SELECT * FROM spend WHERE username = ?"
    )) {
      ps.setString(1, username);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          SpendEntity se = new SpendEntity();
          se.setId(rs.getObject("id", UUID.class));
          se.setUsername(rs.getString("username"));
          se.setSpendDate(rs.getDate("spend_date"));
          se.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
          se.setAmount(rs.getDouble("amount"));
          se.setDescription("description");
          CategoryEntity category = new CategoryEntity();
          category.setId(UUID.fromString(rs.getString("category_id")));
          se.setCategory(category);
          entityList.add(se);
        }
        return entityList;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(SpendEntity spend) {
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
        "DELETE FROM spend WHERE id =?"
    )) {
      ps.setObject(1, spend.getId());
      ps.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public @Nonnull List<SpendEntity> findAll() {
    List<SpendEntity> spends = new ArrayList<>();
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
        "SELECT * FROM spend"
    )) {
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          SpendEntity sp = new SpendEntity();
          sp.setId(rs.getObject("id", UUID.class));
          sp.setUsername(rs.getString("username"));
          sp.setSpendDate(rs.getDate("spend_date"));
          sp.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
          sp.setAmount(rs.getDouble("amount"));
          sp.setDescription(rs.getString("description"));
          CategoryEntity category = new CategoryEntity();
          category.setId(UUID.fromString(rs.getString("category_id")));
          sp.setCategory(category);
          spends.add(sp);
        }
        return spends;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public @Nonnull SpendEntity update(SpendEntity spend) {
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
        "UPDATE spend SET currency = ?, amount = ?, description = ?, category_id = ? WHERE id = ?",
        Statement.RETURN_GENERATED_KEYS
    )) {
      ps.setString(1, String.valueOf(spend.getCurrency()));
      ps.setDouble(2, spend.getAmount());
      ps.setString(3, spend.getDescription());
      ps.setObject(4, spend.getCategory().getId());
      ps.setObject(5, spend.getId());
      ps.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can't find id in ResultSet");
        }
      }
      spend.setId(generatedKey);
      return spend;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
