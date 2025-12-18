package guru.qa.niffler.data.repository.impl.spend;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.spend.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.spend.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.mapper.spend.SpendEntityRowMapper;
import guru.qa.niffler.data.repository.spend.SpendRepository;
import guru.qa.niffler.data.tpl.DataSources;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;

public class SpendRepositorySpringJdbc implements SpendRepository {

  private final Config CFG = Config.getInstance();
  private final SpendDaoSpringJdbc spendDaoSpringJdbc = new SpendDaoSpringJdbc();
  private final CategoryDaoSpringJdbc categoryDaoSpringJdbc = new CategoryDaoSpringJdbc();

  @Override
  public SpendEntity create(SpendEntity spend) {
    return spendDaoSpringJdbc.create(spend);
  }

  @Override
  public SpendEntity update(SpendEntity spend) {
    return spendDaoSpringJdbc.update(spend);
  }

  @Override
  public Optional<SpendEntity> findSpendById(UUID id) {
    return spendDaoSpringJdbc.findById(id);
  }

  @Override
  public Optional<SpendEntity> findByUsernameAndSpendDescription(String username,
      String description) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT * FROM spend WHERE username = ? and description = ?",
            SpendEntityRowMapper.instance,
            username,
            description
        )
    );
  }

  @Override
  public CategoryEntity createCategory(CategoryEntity category) {
    return categoryDaoSpringJdbc.create(category);
  }

  @Override
  public CategoryEntity updateCategory(CategoryEntity category) {
    return categoryDaoSpringJdbc.update(category);
  }

  @Override
  public Optional<CategoryEntity> findCategoryById(UUID id) {
    return categoryDaoSpringJdbc.findById(id);
  }

  @Override
  public Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name) {
    return categoryDaoSpringJdbc.findCategoryByUsernameAndCategoryName(username, name);
  }

  @Override
  public void remove(SpendEntity spend) {
    spendDaoSpringJdbc.delete(spend);
  }

  @Override
  public void removeCategory(CategoryEntity category) {
    categoryDaoSpringJdbc.delete(category);
  }
}
