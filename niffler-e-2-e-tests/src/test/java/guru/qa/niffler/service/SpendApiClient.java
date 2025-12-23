package guru.qa.niffler.service;

import static org.apache.hc.core5.http.HttpStatus.SC_ACCEPTED;
import static org.apache.hc.core5.http.HttpStatus.SC_CREATED;
import static org.apache.hc.core5.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
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
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@ParametersAreNonnullByDefault
public class SpendApiClient implements SpendClient {

  private static final Config CFG = Config.getInstance();

  private final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(CFG.spendUrl())
      .addConverterFactory(JacksonConverterFactory.create())
      .build();

  private final SpendApi spendApi = retrofit.create(SpendApi.class);

  @Override
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
  public Optional<CategoryJson> findCategoryByUsernameAndSpendName(String name, String username) {
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
  public @Nullable Optional<SpendJson> findById(UUID id) {
    final String username = "duck";
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
  public Optional<SpendJson> findByUsernameAndSpendDescription(String username,
      String description) {
    throw new UnsupportedOperationException();
  }

  @Override
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

  public Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName,
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

  public @Nonnull List<CategoryJson> findAllCategories(String username) {
    final Response<List<CategoryJson>> response;
    try {
      response = spendApi.getCategories(username).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(SC_OK, response.code());
    return response.body()!= null? response.body(): Collections.emptyList();
  }

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
