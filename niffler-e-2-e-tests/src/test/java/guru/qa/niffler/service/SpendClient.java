package guru.qa.niffler.service;

import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import java.util.Optional;
import java.util.UUID;

public interface SpendClient {

    SpendJson create(SpendJson spend);

    SpendJson update(SpendJson spend);

    CategoryJson createCategory(CategoryJson category);

    CategoryJson updateCategory(CategoryJson spend);

    Optional<CategoryJson> findCategoryById(UUID id);

    Optional<CategoryJson> findCategoryByUsernameAndSpendName(String username, String name);

    Optional<SpendJson> findById(UUID id);

    Optional<SpendJson> findByUsernameAndSpendDescription(String username, String description);

    void remove(SpendJson spend);

    void removeCategory(CategoryJson category);
}