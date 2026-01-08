package guru.qa.niffler.data.mapper.spend;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.spend.CurrencyValues;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.jdbc.core.RowMapper;

@ParametersAreNonnullByDefault
public class SpendEntityRowMapper implements RowMapper<SpendEntity> {

  public static final SpendEntityRowMapper instance = new SpendEntityRowMapper();

  private SpendEntityRowMapper() {
  }

  @Override
  @Nonnull
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
