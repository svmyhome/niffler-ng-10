package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.spend.SpendRepository;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import io.qameta.allure.Step;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class SpendDbClient implements SpendClient {

  private static final Config CFG = Config.getInstance();
  private final SpendRepository spendRepository = SpendRepository.getInstance();


  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
      CFG.spendJdbcUrl());

  @Override
  @Step("Create spending to DB")
  public @Nonnull SpendJson create(SpendJson spend) {
    return xaTransactionTemplate.execute(() -> {
      SpendEntity spendEntity = SpendEntity.fromJson(spend);
      spendRepository.create(spendEntity);
      return SpendJson.fromEntity(spendEntity);
    });
  }

  @Override
  @Step("Update spending in DB")
  public @Nonnull SpendJson update(SpendJson spend) {
    return xaTransactionTemplate.execute(() -> {
      SpendEntity spendEntity = SpendEntity.fromJson(spend);
      spendRepository.update(spendEntity);
      return SpendJson.fromEntity(spendEntity);
    });
  }

  @Override
  @Step("Update category in DB")
  public @Nonnull CategoryJson updateCategory(CategoryJson category) {
    return xaTransactionTemplate.execute(() -> {
      CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
      spendRepository.updateCategory(categoryEntity);
      return CategoryJson.fromEntity(categoryEntity);
    });
  }

  @Override
  @Step("Create category to DB")
  public @Nonnull CategoryJson createCategory(CategoryJson category) {
    return xaTransactionTemplate.execute(() -> {
      CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
      spendRepository.createCategory(categoryEntity);
      return CategoryJson.fromEntity(categoryEntity);
    });
  }

  @Override
  @Step("Find category by ID in DB")
  public @Nullable Optional<CategoryJson> findCategoryById(UUID id) {
    return spendRepository.findCategoryById(id)
        .map(CategoryJson::fromEntity);
  }

  @Override
  @Step("Find category by username and spend name in DB")
  public @Nullable Optional<CategoryJson> findCategoryByUsernameAndSpendName(String username,
      String name) {
    return spendRepository.findCategoryByUsernameAndSpendName(username, name)
        .map(CategoryJson::fromEntity);
  }

  @Override
  @Step("Find spend by ID in DB")
  public @Nullable Optional<SpendJson> findById(UUID id) {
    return spendRepository.findSpendById(id).map(SpendJson::fromEntity);
  }

  @Override
  @Step("Find spend by username and spend description in DB")
  public @Nullable Optional<SpendJson> findByUsernameAndSpendDescription(String username,
      String description) {
    return spendRepository.findByUsernameAndSpendDescription(username, description)
        .map(SpendJson::fromEntity);
  }

  @Override
  @Step("Remove spend from DB")
  public void remove(SpendJson spend) {
    xaTransactionTemplate.execute(() -> {
      SpendEntity spendEntity = SpendEntity.fromJson(spend);
      spendRepository.remove(spendEntity);
      return null;
    });
  }

  @Override
  @Step("Remove category from DB")
  public void removeCategory(CategoryJson category) {
    xaTransactionTemplate.execute(() -> {
      CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
      spendRepository.removeCategory(categoryEntity);
      return null;
    });
  }
}
