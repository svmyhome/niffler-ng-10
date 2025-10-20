package guru.qa.niffler.service;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.GhApi;
import guru.qa.niffler.config.Config;
import java.io.IOException;
import java.util.Objects;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class GhApiClient {

  private static final String GH_TOKEN_ENV = "GITHUB_TOKEN";

  private final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(Config.getInstance().ghUrl())
      .addConverterFactory(JacksonConverterFactory.create())
      .build();

  private final GhApi ghApi = retrofit.create(GhApi.class);

  public String issueState(String issueNumber) {
    final JsonNode response;
    try {
      response = ghApi.issue(
              "Bearer " + System.getenv(GH_TOKEN_ENV),
              issueNumber
          )
          .execute()
          .body();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return Objects.requireNonNull(response).get("state").asText();
  }
}
