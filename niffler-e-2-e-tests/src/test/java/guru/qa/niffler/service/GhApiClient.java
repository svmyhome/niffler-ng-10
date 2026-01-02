package guru.qa.niffler.service;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.GhApi;
import java.io.IOException;
import java.util.Objects;
import javax.annotation.ParametersAreNonnullByDefault;
import retrofit2.Response;

@ParametersAreNonnullByDefault
public class GhApiClient extends RestClient {

  private static final String GH_TOKEN_ENV = "GITHUB_TOKEN";
  private final GhApi ghApi;

  public GhApiClient() {
    super(CFG.ghUrl());
    this.ghApi = create(GhApi.class);
  }

  public String issueState(String issueNumber) {
    final Response<JsonNode> response;
    try {
      response = ghApi.issue(
          "Bearer " + System.getenv(GH_TOKEN_ENV),
          issueNumber
      ).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return Objects.requireNonNull(response.body()).get("state").asText();
  }
}
