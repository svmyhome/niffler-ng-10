package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.service.AuthApiClient;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import utils.RandomDataUtils;

@Epic("API")
@Feature("User management")
@Story("Registration")
public class RegistrationTest {

  private final AuthApiClient authApiClient = new AuthApiClient();

  @User
  @Test
  @Disabled
  @DisplayName("API: User registration should return 201 Created")
  void newUserShouldRegisteredByApiCall() throws IOException {
    final Response<Void> response = authApiClient.register(RandomDataUtils.randomUsername(), "12345");
    Assertions.assertEquals(201, response.code());
  }
}
