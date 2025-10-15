package guru.qa.niffler.data.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoJdbc implements SpendDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO spend (username, spend_date, currency, amount, description, category_id)" +
                            "VALUES(?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                ps.setString(1, spend.getUsername());
                ps.setDate(2, spend.getSpendDate());
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
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM spend WHERE id = ?"
            )) {
                ps.setObject(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        SpendEntity se = new SpendEntity();
                        se.setId(rs.getObject("id", UUID.class));
                        se.setUsername(rs.getString("username"));
                        se.setSpendDate(rs.getDate("spend_date"));
                        String currencyString = rs.getString("currency");
                        se.setCurrency(CurrencyValues.valueOf(currencyString));
                        se.setAmount(rs.getDouble("amount"));
                        se.setDescription(rs.getString("description"));
                        String categoryId = rs.getString("category_id");

                        se.setCategory(rs.getString("category_id"));

                        return Optional.of(se);
                    } else {
                        return Optional.empty();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        List<SpendEntity> entityList = new ArrayList<>();
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM spend WHERE username = ?"
            )) {
                ps.setString(1, username);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        while (rs.next()) {
                            SpendEntity se = new SpendEntity();
                            se.setId(rs.getObject("id", UUID.class));
                            se.setUsername(rs.getString("username"));
                            se.setSpendDate(rs.getDate("spend_date"));
                            String currencyString = rs.getString("currency");
                            se.setCurrency(CurrencyValues.valueOf(currencyString));
                            se.setAmount(rs.getDouble("amount"));
                            se.setDescription("description");
                            se.setId(rs.getObject("category_id", UUID.class));
                            entityList.add(se);
                        }
                    } else {
                        throw new SQLException("Can't find username in ResultSet");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return entityList;
    }

    @Override
    public void deleteSpend(SpendEntity spend) {
        try(Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
            try(PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM spend WHERE id =?"
            )) {
                ps.setObject(1, spend.getId());
                ps.execute();

//                try(ResultSet rs = ps.getResultSet()) {
//                    if (rs.next()) {
//
//                    }
//                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
