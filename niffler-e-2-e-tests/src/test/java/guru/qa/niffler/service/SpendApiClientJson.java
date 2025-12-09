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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class SpendApiClientJson implements SpendClientJson {

  private static final Config CFG = Config.getInstance();

  private final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(CFG.spendUrl())
      .addConverterFactory(JacksonConverterFactory.create())
      .build();

  private final SpendApi spendApi = retrofit.create(SpendApi.class);

  @Override
  public Optional<SpendJson> findSpendById(String id) {
    final Response<SpendJson> response;
    try {
      response = spendApi.getSpend(id).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(SC_OK, response.code());
    return Optional.ofNullable(response.body());
  }


  public List<SpendJson> findSpendsByUserName(String username, CurrencyValues currencyValues,
      String from, String to) {
    final Response<SpendJson[]> response;
    try {
      response = spendApi.getSpends(username, currencyValues, from, to).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(SC_OK, response.code());
    return List.of(Objects.requireNonNullElseGet(response.body(), () -> new SpendJson[0]));
  }

  @Override
  public List<SpendJson> findSpendsByUserName(String username) {
    final Response<SpendJson[]> response;
    try {
      response = spendApi.getSpends(username).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(SC_OK, response.code());
    return List.of(Objects.requireNonNullElseGet(response.body(), () -> new SpendJson[0]));
  }

  @Override
  public SpendJson createSpend(SpendJson spend) {
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
  public SpendJson updateSpend(SpendJson spend) {
    final Response<SpendJson> response;
    try {
      response = spendApi.updateSpend(spend).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(SC_OK, response.code());
    return response.body();
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

  @Override
  public List<CategoryJson> findAllCategories(String username) {
    final Response<List<CategoryJson>> response;
    try {
      response = spendApi.getCategories(username).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(SC_OK, response.code());
    assert response.body() != null;
    return response.body();
  }

  @Override
  public CategoryJson createCategory(CategoryJson category) {
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
  public CategoryJson updateCategory(CategoryJson category) {
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
}
