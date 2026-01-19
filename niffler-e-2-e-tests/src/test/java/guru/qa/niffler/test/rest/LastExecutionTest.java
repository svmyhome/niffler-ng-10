package guru.qa.niffler.test.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.service.UserApiClient;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.Isolated;


@Isolated
@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(OrderAnnotation.class)
@Epic("API")
@Feature("User management")
@Story("Last execution test")
public class LastExecutionTest {

  private final UserApiClient userApiClient = new UserApiClient();

  @User
  @Test
  @DisplayName("API: Should return all users")
  public void shouldReturnAllExistingUsersFromApi(UserJson user) {
    List<UserJson> allUsers = userApiClient.getAllUsers(user.username());
    assertThat(allUsers.size(), greaterThan(1));
  }

}
