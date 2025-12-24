package guru.qa.niffler.service;

import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.UserApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.user.UserJson;
import io.qameta.allure.Step;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import utils.RandomDataUtils;

public class UserApiClient implements UserClient {

  private static final Config CFG = Config.getInstance();

  private static final CookieManager cm = new CookieManager(null, CookiePolicy.ACCEPT_ALL);

  private final Retrofit authRetrofit = new Retrofit.Builder()
      .baseUrl(CFG.authUrl())
      .addConverterFactory(JacksonConverterFactory.create())
      .client(new OkHttpClient.Builder()
          .cookieJar(new JavaNetCookieJar(
              cm
          ))
          .build())
      .build();

  private final Retrofit userRetrofit = new Retrofit.Builder()
      .baseUrl(CFG.userdataUrl())
      .addConverterFactory(JacksonConverterFactory.create())
      .build();

  private final UserApi userApi = userRetrofit.create(UserApi.class);
  private final AuthApi authApi = authRetrofit.create(AuthApi.class);

  @Override
  @Step("Create user {username} via API")
  public UserJson createUser(String username, String password) {
    try {
      authApi.requestRegisterForm().execute();
      authApi.register(
          username,
          password,
          password,
          cm.getCookieStore().getCookies()
              .stream()
              .filter(c -> c.getName().equals("XSRF-TOKEN"))
              .findFirst()
              .get()
              .getValue()
      ).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    final Response<UserJson> response;
    try {
      response = userApi.getCurrentUser(username).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    return response.body();
  }

  @Override
  @Step("Create income invitation for user {targetUser.username} via API")
  public List<UserJson> createIncomeInvitations(UserJson targetUser, int count) {
    List<UserJson> resultList = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      try {
        UserJson friend = createUser(RandomDataUtils.randomUsername(), "12345");
        resultList.add(userApi.sendInvitation(friend.username(), targetUser.username())
            .execute().body());
      } catch (IOException e) {
        throw new AssertionError(e);
      }
    }
    return resultList;
  }

  @Override
  @Step("Create outcome invitation for user {targetUser.username} via API")
  public List<UserJson> createOutcomeInvitations(UserJson targetUser, int count) {
    List<UserJson> resultList = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      try {
        UserJson friend = createUser(RandomDataUtils.randomUsername(), "12345");
        resultList.add(userApi.sendInvitation(targetUser.username(), friend.username())
            .execute().body());
      } catch (IOException e) {
        throw new AssertionError(e);
      }
    }
    return resultList;
  }

  @Override
  @Step("Create {count} friends for user {targetUser.username} via API")  public List<UserJson> createFriends(UserJson targetUser, int count) {
    List<UserJson> resultList = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      try {
        UserJson friend = createUser(RandomDataUtils.randomUsername(), "12345");
        resultList.add(userApi.sendInvitation(targetUser.username(), friend.username())
            .execute().body());
        resultList.add(userApi.acceptInvitation(friend.username(), targetUser.username())
            .execute().body());
      } catch (IOException e) {
        throw new AssertionError(e);
      }
    }
    return resultList;
  }

}
