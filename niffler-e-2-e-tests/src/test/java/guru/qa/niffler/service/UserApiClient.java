package guru.qa.niffler.service;

import static guru.qa.niffler.jupiter.extension.UserExtension.DEFAULT_PASSWORD;
import com.google.common.base.Stopwatch;
import guru.qa.niffler.api.UserApi;
import guru.qa.niffler.model.user.UserJson;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import retrofit2.Response;
import utils.RandomDataUtils;

@ParametersAreNonnullByDefault
public final class UserApiClient extends RestClient implements UserClient {

  private final UserApi userApi;
  private final AuthApiClient authApiClient;

  public UserApiClient() {
    super(CFG.userdataUrl());
    this.userApi = create(UserApi.class);
    this.authApiClient = new AuthApiClient();
  }

  @Override
  @Step("Create user {username} via API")
  public @Nonnull UserJson createUser(String username, String password) {
    try {
      authApiClient.register(username, password);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Response<UserJson> response;
    Stopwatch sw = Stopwatch.createStarted();
    int maxWaitTime = 6000;
    while (sw.elapsed(TimeUnit.MILLISECONDS) < maxWaitTime) {
      try {
        response = userApi.getCurrentUser(username).execute();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      UserJson user = response.body();
      if (user != null && user.id() != null) {
        return user;
      } else {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    }
    throw new RuntimeException(
        String.format("User '%s' was not created within %d ms", username, maxWaitTime)
    );
  }

  @Override
  @Step("Create income invitation for user {targetUser.username} via API")
  public List<UserJson> createIncomeInvitations(UserJson targetUser, int count) {
    List<UserJson> resultList = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      try {
        UserJson friend = createUser(RandomDataUtils.randomUsername(), DEFAULT_PASSWORD);
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
        UserJson friend = createUser(RandomDataUtils.randomUsername(), DEFAULT_PASSWORD);
        resultList.add(userApi.sendInvitation(targetUser.username(), friend.username())
            .execute().body());
      } catch (IOException e) {
        throw new AssertionError(e);
      }
    }
    return resultList;
  }

  @Override
  @Step("Create {count} friends for user {targetUser.username} via API")
  public List<UserJson> createFriends(UserJson targetUser, int count) {
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

  @Step("Get all users")
  public List<UserJson> getAllUsers(String username, @Nullable String searchQuery) {
    List<UserJson> allUsers;
    try {
      allUsers = userApi.getAllUsers(username, searchQuery).execute().body();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return allUsers;
  }

  @Step("Get all users")
  public List<UserJson> getAllUsers(String username) {
    return getAllUsers(username, null);
  }

  @Step("Get all friends")
  public List<UserJson> getAllFriends(String username) {
    List<UserJson> allFriends;
    try {
      allFriends = userApi.getFriends(username).execute().body();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return allFriends;
  }
}
