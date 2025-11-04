package guru.qa.niffler.service;

import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import java.util.List;
import java.util.Optional;

public interface SpendClient {

  Optional<SpendJson> findSpendById(String id);

  List<SpendJson> findSpendsByUserName(String username);

  SpendJson createSpend(SpendJson spend);

  SpendJson updateSpend(SpendJson spendJson);

  List<CategoryJson> findAllCategories(String username);

  CategoryJson createCategory(CategoryJson category);

  CategoryJson updateCategory(CategoryJson categoryJson);

  Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName, String username);
}
