package guru.qa.niffler.data.impl;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.mapper.CategoryEntityRowMapper;
import guru.qa.niffler.model.CategoryJson;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class CategoryDaoSpringJdbc implements CategoryDao {

  private final DataSource dataSource;

  public CategoryDaoSpringJdbc(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public CategoryEntity create(CategoryEntity category) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    KeyHolder kh = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(
          "INSERT INTO category (name,username,archived)" +
              "VALUES(?,?,?)",
          Statement.RETURN_GENERATED_KEYS
      );
      ps.setString(1, category.getName());
      ps.setString(1, category.getUsername());
      ps.setBoolean(1, category.isArchived());
      return ps;
    }, kh);
    final UUID generatedKey = (UUID) kh.getKeys().get("id");
    category.setId(generatedKey);
    return category;
  }

  @Override
  public Optional<CategoryEntity> findById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT * FROM category WHERE id = ?",
            CategoryEntityRowMapper.instance,
            id
        )
    );
  }

  @Override
  public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username,
      String categoryName) {
    throw new UnsupportedOperationException("Method updateCategory() is not implemented yet");
  }

  @Override
  public List<CategoryEntity> findAllByUsername(String username) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.query(
        "SELECT * FROM category WHERE username = ?",
        CategoryEntityRowMapper.instance,
        username
    );
  }

  @Override
  public void delete(CategoryEntity category) {
    throw new UnsupportedOperationException("Method updateCategory() is not implemented yet");
  }

  @Override
  public CategoryJson update(CategoryJson categoryJson) {
    throw new UnsupportedOperationException("Method updateCategory() is not implemented yet");
  }
}
