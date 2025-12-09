package guru.qa.niffler.data.repository.impl.spend;

import static guru.qa.niffler.data.tpl.Connections.holder;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.spend.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.spend.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.jpa.EntityManagers;
import guru.qa.niffler.data.repository.spend.SpendRepository;
import guru.qa.niffler.model.spend.CurrencyValues;
import jakarta.persistence.EntityManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class SpendRepositorySpringJdbc implements SpendRepository {

  private final Config CFG = Config.getInstance();
  private final EntityManager entityManager = EntityManagers.em(CFG.spendJdbcUrl());
  private final SpendDaoJdbc spendDaoJdbc = new SpendDaoJdbc();
  private final CategoryDaoJdbc categoryDaoJdbc = new CategoryDaoJdbc();

  @Override
  public SpendEntity create(SpendEntity spend) {
    return spendDaoJdbc.create(spend);
  }

  @Override
  public SpendEntity update(SpendEntity spend) {
    return spendDaoJdbc.update(spend);
  }

  @Override
  public Optional<SpendEntity> findSpendById(UUID id) {
    return spendDaoJdbc.findById(id);
  }

  @Override
  public Optional<SpendEntity> findByUsernameAndSpendDescription(String username,
      String description) {
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
        "SELECT * FROM spend WHERE username = ? AND description = ?;"
    )) {
      ps.setObject(1, username);
      ps.setObject(2, description);
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
  public CategoryEntity createCategory(CategoryEntity category) {
    return categoryDaoJdbc.create(category);
  }

  @Override
  public CategoryEntity updateCategory(CategoryEntity category) {
    return categoryDaoJdbc.update(category);
  }

  @Override
  public Optional<CategoryEntity> findCategoryById(UUID id) {
    return categoryDaoJdbc.findById(id);
  }

  @Override
  public Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name) {
    return categoryDaoJdbc.findCategoryByUsernameAndSpendName(username, name);
  }

  @Override
  public void remove(SpendEntity spend) {
    spendDaoJdbc.delete(spend);
  }

  @Override
  public void removeCategory(CategoryEntity category) {
    categoryDaoJdbc.delete(category);
  }
}
