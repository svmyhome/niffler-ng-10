package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpendApi {

  @GET("internal/spends/{id}")
  Call<SpendJson> getSpend(@Path("id") String id, @Query("username") String username);

  @GET("internal/spends/all")
  Call<SpendJson[]> getSpends(
      @Query("username") String username,
      @Query("filterCurrency") CurrencyValues filterCurrency,
      @Query("from") String from,
      @Query("to") String to
  );

  @POST("internal/spends/add")
  Call<SpendJson> addSpend(@Body SpendJson spend);

  @PATCH("internal/spends/edit")
  Call<SpendJson> updateSpend(@Body SpendJson spend);

  @DELETE("internal/spends/remove")
  Call<Void> removeSpends(@Query("username") String username, @Query("ids") List<String> ids);

  @GET("internal/categories/all")
  Call<List<CategoryJson>> getCategories(
      @Query("username") String username,
      @Query("excludeArchived") Boolean excludeArchived
  );

  @POST("internal/categories/add")
  Call<CategoryJson> addCategory(@Body CategoryJson category);

  @PATCH("internal/categories/update")
  Call<CategoryJson> updateCategory(@Body CategoryJson category);

}
