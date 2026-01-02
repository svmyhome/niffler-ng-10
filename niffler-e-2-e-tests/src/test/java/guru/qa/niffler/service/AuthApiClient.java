package guru.qa.niffler.service;

import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import java.io.IOException;
import javax.annotation.ParametersAreNonnullByDefault;
import retrofit2.Response;

@ParametersAreNonnullByDefault
public class AuthApiClient extends RestClient {

  private static final Config CFG = Config.getInstance();

  private final AuthApi authApi;

  public AuthApiClient() {
    super(CFG.authUrl(), true);
    this.authApi = create(AuthApi.class);
  }

  public Response<Void> register(String username, String password) throws IOException {
    authApi.requestRegisterForm().execute();
    return authApi.register(
        username,
        password,
        password,
        ThreadSafeCookieStore.INSTANCE.xsrfCookie()
    ).execute();
  }
}
