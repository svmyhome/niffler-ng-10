package guru.qa.niffler.service;

import static org.apache.hc.core5.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import guru.qa.niffler.api.GatewayV2Api;
import guru.qa.niffler.model.page.RestResponsePage;
import guru.qa.niffler.model.user.UserJson;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Response;

@ParametersAreNonnullByDefault
public final class GatewayV2ApiClient extends RestClient {

    private final GatewayV2Api gatewayV2Api;

    public GatewayV2ApiClient() {
        super(CFG.gatewayUrl(), Level.BODY);
        this.gatewayV2Api = create(GatewayV2Api.class);
    }


    @Step("Get all friends and income invitation from gateway  using endpoint api/friends/all")
    public RestResponsePage<UserJson> allFriends(String bearerToken,
                                                 int page,
                                                 int size,
                                                 List<String> sort,
                                                 @Nullable String searchQuery) {
        final Response<RestResponsePage<UserJson>> response;
        try {
            response = gatewayV2Api.allFriends(bearerToken, page, size, sort, searchQuery).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }
}
