package guru.qa.niffler.api;

import guru.qa.niffler.data.constants.DataFilterValues;
import guru.qa.niffler.model.gw.v2.StatisticV2Json;
import guru.qa.niffler.model.page.RestResponsePage;
import guru.qa.niffler.model.spend.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.user.UserJson;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

@ParametersAreNonnullByDefault
public interface GatewayV2Api {

    @GET("api/v2/friends/all")
    Call<RestResponsePage<UserJson>> allFriends(@Header("Authorization") String bearerToken,
                                                @Query("page") int page,
                                                @Query("size") int size,
                                                @Query("sort") List<String> sort,
                                                @Query("searchQuery") @Nullable String searchQuery);

    @GET("api/v2/spends/all")
    Call<RestResponsePage<SpendJson>> allSpends(@Header("Authorization") String bearerToken,
                                        @Query("page") int page,
                                        @Query("size") int size,
                                        @Query("sort") List<String> sort,
                                        @Query("filterPeriod") @Nullable DataFilterValues filterPeriod,
                                        @Query("filterCurrency") @Nullable CurrencyValues filterCurrency,
                                        @Query("searchQuery") @Nullable String searchQuery);


    @GET("api/v2/stat/total")
    Call<StatisticV2Json> getTotalStatistic(@Header("Authorization") String bearerToken,
                                            @Query("filterPeriod") @Nullable DataFilterValues filterPeriod,
                                            @Query("filterCurrency") @Nullable CurrencyValues filterCurrency,
                                            @Query("searchQuery") @Nullable String searchQuery);

    @GET("api/v2/users/all")
    Call<RestResponsePage<UserJson>> allUsers(@Header("Authorization") String bearerToken,
                                      @Query("page") int page,
                                      @Query("size") int size,
                                      @Query("sort") List<String> sort,
                                      @Query("searchQuery") @Nullable String searchQuery);

}
