package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;

public class SpendEntityRowMapper implements RowMapper<SpendEntity> {

  public static final SpendEntityRowMapper instance = new SpendEntityRowMapper();

  private SpendEntityRowMapper() {
  }

  @Override
  public SpendEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    CategoryEntity ce = new CategoryEntity();
    ce.setId(UUID.fromString(rs.getString("category_id")));
    SpendEntity spendEntity = new SpendEntity();
    spendEntity.setId(rs.getObject("id", UUID.class));
    spendEntity.setUsername(rs.getString("username"));
    spendEntity.setSpendDate(rs.getDate("spend_date"));
    spendEntity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
    spendEntity.setAmount(rs.getDouble("amount"));
    spendEntity.setDescription(rs.getString("description"));
    spendEntity.setCategory(ce);
    return spendEntity;
  }
}
