package guru.qa.niffler.service;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.core.CodeInterceptor;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import java.io.IOException;
import javax.annotation.ParametersAreNonnullByDefault;
import lombok.SneakyThrows;
import retrofit2.Response;
import utils.OAuthUtils;

@ParametersAreNonnullByDefault
public final class AuthApiClient extends RestClient {

    private final AuthApi authApi;

    private static final String RESPONSE_TYPE = "code";
    private static final String CLIENT_ID = "client";
    private static final String SCOPE = "openid";
    private static final String CODE_CHALLENGE_METHOD = "S256";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String REDIRECT_URL = CFG.frontUrl() + "authorized";

    public AuthApiClient() {
        super(CFG.authUrl(), true, new CodeInterceptor());
        this.authApi = create(AuthApi.class);
    }

    public Response<Void> register(String username, String password) throws IOException {
        authApi.requestRegisterForm().execute();
        return authApi.register(
                username,
                password,
                password,
                ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
        ).execute();
    }

    final String codeVerifier = OAuthUtils.generateCodeVerifier();
    final String codeChallenge = OAuthUtils.generateCodeChallenge(codeVerifier);

    @SneakyThrows
    public String login(String username, String password){
        authApi.authorize(
                RESPONSE_TYPE,
                CLIENT_ID,
                SCOPE,
                REDIRECT_URL,
                codeChallenge,
                CODE_CHALLENGE_METHOD
        ).execute();

        authApi.login(
                username,
                password,
                ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
        ).execute();

        Response<JsonNode> tokenResponse = authApi.token(
                ApiLoginExtension.getCode(),
                REDIRECT_URL,
                CLIENT_ID,
                codeVerifier,
                GRANT_TYPE
        ).execute();

        return tokenResponse.body().get("id_token").asText();
    }

//    public void authorize(String codeChallenge) throws IOException {
//        authApi.authorize(
//                RESPONSE_TYPE,
//                CLIENT_ID,
//                SCOPE,
//                REDIRECT_URL,
//                codeChallenge,
//                CODE_CHALLENGE_METHOD
//        ).execute();
//    }
//
//    public String login(String username, String password) throws IOException {
//        var response = authApi.login(username, password, ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")).execute();
//        return StringUtils.substringAfter(response.raw().request().url().toString(), "code=");
//    }
//
//    public String token(String code, String codeVerifier) throws IOException {
//        var response = authApi.token(
//                code,
//                REDIRECT_URL,
//                CLIENT_ID,
//                codeVerifier,
//                GRANT_TYPE
//        ).execute();
//        if (response.body()!=null) {
//            return response.body().path("id_token").asText();
//        }
//        return "";
//    }
}
