package guru.qa.niffler.service;

import static org.apache.hc.core5.http.HttpStatus.SC_ACCEPTED;
import static org.apache.hc.core5.http.HttpStatus.SC_CREATED;
import static org.apache.hc.core5.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import retrofit2.Response;

@ParametersAreNonnullByDefault
public final class SpendApiClient extends RestClient implements SpendClient {

    private final SpendApi spendApi;

    public SpendApiClient() {
        super(CFG.spendUrl());
        this.spendApi = create(SpendApi.class);
    }

    @Override
    @Step("Send REST POST('/internal/spends/add') request to niffler-spend")
    public @Nullable SpendJson create(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.addSpend(spend).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_CREATED, response.code());
        return response.body();
    }

    @Override
    @Step("Send REST POST('/internal/spends/edit') request to niffler-spend")
    public @Nullable SpendJson update(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.updateSpend(spend).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }

    @Override
    @Step("Send REST POST('/internal/categories/add') request to niffler-spend")
    public @Nullable CategoryJson createCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.addCategory(category).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }

    @Override
    @Step("Send REST POST('/internal/categories/update') request to niffler-spend")
    public @Nullable CategoryJson updateCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.updateCategory(category).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }

    @Override
    public Optional<CategoryJson> findCategoryById(UUID id) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Step("Send REST POST('/internal/categories/all') request to niffler-spend")
    public @Nullable Optional<CategoryJson> findCategoryByUsernameAndSpendName(String name,
                                                                               String username) {
        final Response<List<CategoryJson>> response;
        try {
            response = spendApi.getCategories(name, username).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());

        List<CategoryJson> categories = Objects.requireNonNullElseGet(
                response.body(),
                ArrayList::new
        );
        return categories.stream().findFirst();
    }

    @Override
    @Step("Send REST POST('/internal/spends/{id}') request to niffler-spend")
    public @Nullable Optional<SpendJson> findById(UUID id) {
        throw new UnsupportedOperationException();
    }

    @Step("Send REST POST('/internal/spends/{id}') request to niffler-spend")
    public @Nullable Optional<SpendJson> findByIdAndUsername(UUID id, String username) {
        final Response<SpendJson> response;
        try {
            response = spendApi.getSpend(String.valueOf(id), username).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return Optional.ofNullable(response.body());
    }

    @Override
    public @Nullable Optional<SpendJson> findByUsernameAndSpendDescription(String username,
                                                                           String description) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Step("Send REST POST('/internal/spends/remove') request to niffler-spend")
    public void remove(SpendJson spend) {
        final Response<Void> response;
        try {
            response = spendApi.removeSpends(spend.username(), List.of(String.valueOf(spend.id())))
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_ACCEPTED, response.code());
    }

    @Override
    public void removeCategory(CategoryJson category) {
        throw new UnsupportedOperationException();
    }

    @Step("Send REST POST('/internal/spends/all') request to niffler-spend")
    public @Nonnull List<SpendJson> findSpendsByUserName(
            String username,
            @Nullable CurrencyValues currencyValues,
            @Nullable String from,
            @Nullable String to
    ) {
        final Response<SpendJson[]> response;
        try {
            response = spendApi.getSpends(username, currencyValues, from, to).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return List.of(Objects.requireNonNullElseGet(response.body(), () -> new SpendJson[0]));
    }

    @Step("Send REST POST('/internal/spends/all') request to niffler-spend")
    public @Nonnull List<SpendJson> findSpendsByUserName(String username) {
        final Response<SpendJson[]> response;
        try {
            response = spendApi.getSpends(username).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return List.of(Objects.requireNonNullElseGet(response.body(), () -> new SpendJson[0]));
    }

    @Step("Send REST POST('/internal/categories/all') request to niffler-spend")
    public @Nullable Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName,
                                                                          String username) {
        final Response<List<CategoryJson>> response;
        try {
            response = spendApi.getCategories(categoryName, username).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());

        List<CategoryJson> categories = Objects.requireNonNullElseGet(
                response.body(),
                ArrayList::new
        );
        return categories.stream().findFirst();
    }

    @Step("Send REST POST('/internal/categories/all') request to niffler-spend")
    public @Nonnull List<CategoryJson> findAllCategories(String username) {
        final Response<List<CategoryJson>> response;
        try {
            response = spendApi.getCategories(username).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body()!=null ? response.body():Collections.emptyList();
    }

    @Step("Send REST POST('/internal/spends/remove') request to niffler-spend")
    public void deleteSpends(String username, List<String> ids) {
        final Response<Void> response;
        try {
            response = spendApi.removeSpends(username, ids).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_ACCEPTED, response.code());
    }
}
