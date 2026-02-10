package guru.qa.niffler.api;

import guru.qa.niffler.data.constants.DataFilterValues;
import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.SessionJson;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.CurrencyJson;
import guru.qa.niffler.model.spend.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.user.UserJson;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

@ParametersAreNonnullByDefault
public interface GatewayApi {

    @GET("api/categories/all")
    Call<List<CategoryJson>> allCategories(@Header("Authorization") String bearerToken);


    @POST("api/categories/add")
    Call<CategoryJson> addCategory(
            @Header("Authorization") String bearerToken,
            @Body CategoryJson category
    );

    @PATCH("api/categories/update")
    Call<CategoryJson> updateCategory(
            @Header("Authorization") String bearerToken,
            @Body CategoryJson category
    );

    @GET("api/currencies/all")
    Call<List<CurrencyJson>> getAllCurrency(@Header("Authorization") String bearerToken);

    @GET("api/friends/all")
    Call<List<UserJson>> allFriends(@Header("Authorization") String bearerToken,
                                    @Query("searchQuery") @Nullable String searchQuery);

    @DELETE("api/friends/remove")
    Call<Void> removeFriend(@Header("Authorization") String bearerToken,
                            @Query("username") @Nullable String username);

    @POST("api/invitations/send")
    Call<UserJson> sendInvitation(@Header("Authorization") String bearerToken,
                                  @Body FriendJson friend);

    @POST("api/invitations/accept")
    Call<UserJson> acceptInvitation(@Header("Authorization") String bearerToken,
                                    @Body FriendJson friend);

    @POST("api/invitations/decline")
    Call<UserJson> declineInvitation(@Header("Authorization") String bearerToken,
                                     @Body FriendJson friend);

    @GET("api/session/current")
    Call<SessionJson> session(@Header("Authorization") String bearerToken);

    @GET("api/spends/{id}")
    Call<SpendJson> getSpend(@Header("Authorization") String bearerToken,
                             @Path("id") String id);

    @GET("api/spends/all")
    Call<List<SpendJson>> getSpends(@Header("Authorization") String bearerToken,
                                    @Query("filterPeriod") DataFilterValues filterPeriod,
                                    @Query("filterCurrency") CurrencyValues filterCurrency
    );

    @POST("api/spends/add")
    Call<SpendJson> addSpend(@Header("Authorization") String bearerToken,
                             @Body SpendJson spend);

    @PATCH("api/spends/remove")
    Call<SpendJson> editSpend(@Header("Authorization") String bearerToken,
                              @Body SpendJson spend);

    @DELETE("api/spends/edit")
    Call<Void> deleteSpends(@Header("Authorization") String bearerToken,
                            @Query("ids") @Nonnull List<String> ids);

    @GET("api/users/current")
    Call<UserJson> currentUser(@Header("Authorization") String bearerToken);

    @GET("api/users/all")
    Call<List<UserJson>> allUsers(@Header("Authorization") String bearerToken,
                                  @Query("searchQuery") String searchQuery);

    @POST("api/users/update")
    Call<UserJson> updateUserInfo(@Header("Authorization") String bearerToken,
                                  @Body UserJson user);
}
