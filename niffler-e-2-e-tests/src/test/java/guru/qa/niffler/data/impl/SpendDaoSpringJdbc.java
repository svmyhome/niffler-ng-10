package guru.qa.niffler.data.impl;

import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.mapper.SpendEntityRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoSpringJdbc implements SpendDao {

    private final DataSource dataSource;

    public SpendDaoSpringJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public SpendEntity create(SpendEntity spend) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO spend (username, spend_date, currency, amount, description, category_id)" +
                            "VALUES(?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );
                    ps.setString(1, spend.getUsername());
                    ps.setDate(2, spend.getSpendDate());
                    ps.setObject(3, spend.getCurrency());
                    ps.setDouble(4, spend.getAmount());
                    ps.setString(5,spend.getDescription());
                    ps.setObject(6, spend.getCategory());
                return ps;
        }, kh);

        final UUID generatedKey = (UUID) kh.getKeys().get("id");
        spend.setId(generatedKey);
        return spend;
    }

    @Override
    public Optional<SpendEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                "SELECT * FROM spend WHERE id = ?",
                SpendEntityRowMapper.instance,
                id
        ));
    }

    @Override
    public List<SpendEntity> findAllByCategoryId(UUID categoryId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        return jdbcTemplate.query(
                "SELECT * FROM spend WHERE category_id = ?",
                SpendEntityRowMapper.instance,
                categoryId
        );
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(
                "SELECT * FROM spend WHERE username = ?",
                SpendEntityRowMapper.instance,
                username
        );
    }

    @Override
    public void delete(SpendEntity spend) {
        throw new UnsupportedOperationException("Method updateCategory() is not implemented yet");
    }
}
