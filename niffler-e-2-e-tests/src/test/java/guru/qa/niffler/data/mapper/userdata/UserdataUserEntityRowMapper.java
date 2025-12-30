package guru.qa.niffler.data.mapper.userdata;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.spend.CurrencyValues;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.jdbc.core.RowMapper;

@ParametersAreNonnullByDefault
public class UserdataUserEntityRowMapper implements RowMapper<UserEntity> {

  public static UserdataUserEntityRowMapper instance = new UserdataUserEntityRowMapper();

  private UserdataUserEntityRowMapper() {
  }

  @Override
  @Nonnull
  public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    UserEntity ue = new UserEntity();
    ue.setId(rs.getObject("id", UUID.class));
    ue.setUsername(rs.getString("username"));
    ue.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
    ue.setFirstname(rs.getString("firstname"));
    ue.setSurname(rs.getString("surname"));
    ue.setPhoto(rs.getBytes("photo"));
    ue.setPhotoSmall(rs.getBytes("photo_small"));
    ue.setFullname(rs.getString("full_name"));
    return ue;
  }
}
