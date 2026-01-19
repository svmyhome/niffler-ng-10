package guru.qa.niffler.test.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.service.UserApiClient;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

@Order(1)
@Epic("API")
@Feature("User management")
@Story("First execution test")
public class FirstExecutionTest {

  private final UserApiClient userApiClient = new UserApiClient();

  @User
  @Test
  @DisplayName("API: Should returns empty list when search query doesn't match any user")
  public void shouldReturnEmptyUserListWhenSearchQueryUnmatchedFromApi(UserJson user) {
    List<UserJson> allUsers = userApiClient.getAllUsers(user.username());
    assertThat(allUsers, empty());
  }
}
