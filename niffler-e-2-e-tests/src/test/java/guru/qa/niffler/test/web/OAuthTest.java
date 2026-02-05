package guru.qa.niffler.test.web;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.service.AuthApiClient;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;
import utils.OauthUtils;

public class OAuthTest {

    private final AuthApiClient authApiClient = new AuthApiClient();

    @Test
    @User
    public void oauthTest(UserJson user) throws IOException, NoSuchAlgorithmException {
        String codeVerifier = OauthUtils.generateCodeVerifier();
        String codeChallenge = OauthUtils.generateCodeChallenge(codeVerifier);
        authApiClient.authorize(codeChallenge);
        var code = authApiClient.login(user.username(), user.testData().password());
        String token = authApiClient.token(code, codeVerifier);
        assertNotNull(token);
        System.out.println("Final Token: " + token);
    }
}
