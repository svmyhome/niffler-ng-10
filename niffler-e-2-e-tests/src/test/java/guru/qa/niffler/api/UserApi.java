package guru.qa.niffler.api;

import guru.qa.niffler.model.user.UserJson;
import java.util.List;
import javax.annotation.Nullable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {

  @GET("internal/users/current")
  Call<UserJson> getCurrentUser(
      @Query("username") String username
  );

  @GET("internal/users/all")
  Call<List<UserJson>> getAllUsers(
      @Query("username") String username,
      @Nullable @Query("searchQuery") String searchQuery
  );

  default Call<List<UserJson>> getAllUsers(String username) {
    return getAllUsers(username, null);
  }

  @POST("internal/users/update")
  Call<UserJson> updateUser(@Body UserJson user);

  @GET("internal/friends/all")
  Call<List<UserJson>> getFriends(
      @Query("username") String username,
      @Nullable @Query("searchQuery") String searchQuery
  );

  default Call<List<UserJson>> getFriends(String username) {
    return getFriends(username, null);
  }

  @DELETE("internal/friends/all")
  Call<Void> removeFriends(
      @Query("username") String username,
      @Query("targetUsername") String targetUsername
  );

  @POST("internal/invitations/send")
  Call<UserJson> sendInvitation(
      @Query("username") String username,
      @Query("targetUsername") String targetUsername
  );

  @POST("internal/invitations/accept")
  Call<UserJson> acceptInvitation(
      @Query("username") String username,
      @Query("targetUsername") String targetUsername
  );

  @POST("internal/invitations/decline")
  Call<UserJson> declineInvitation(
      @Query("username") String username,
      @Query("targetUsername") String targetUsername
  );
}
