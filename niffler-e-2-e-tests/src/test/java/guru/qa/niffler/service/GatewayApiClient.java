package guru.qa.niffler.service;

import static org.apache.hc.core5.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import guru.qa.niffler.api.GatewayApi;
import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.user.UserJson;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import retrofit2.Response;

@ParametersAreNonnullByDefault
public final class GatewayApiClient extends RestClient {

    private final GatewayApi gatewayApi;

    public GatewayApiClient() {
        super(CFG.gwUrl());
        this.gatewayApi = create(GatewayApi.class);
    }


    @Step("Get all friends and income invitation from gateway using endpoint api/friends/all")
    public List<UserJson> allFriends(String bearerToken,
                                     @Nullable String searchQuery) {
        final Response<List<UserJson>> response;
        try {
            response = gatewayApi.allFriends(bearerToken, searchQuery).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }

    @Step("Delete friend from gateway using endpoint api/friends/remove")
    public void removeFriend(String bearerToken,
                             @Nullable String username) {
        final Response<Void> response;
        try {
            response = gatewayApi.removeFriend(bearerToken, username).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
    }

    @Step("Accept income invitation from gateway using endpoint api/invitations/accept")
    public UserJson acceptInvitation(String token, FriendJson friend) {
        final Response<UserJson> response;
        try {
            response = gatewayApi.acceptInvitation(token, friend).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }

    @Step("Decline income invitation from gateway using endpoint api/invitations/decline")
    public UserJson declineInvitation(String token, FriendJson friend) {
        final Response<UserJson> response;

        try {
            response = gatewayApi.declineInvitation(token, friend).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }

    @Step("Send invitation from gateway using endpoint api/invitations/send")
    public UserJson sendInvitation(String token, FriendJson friend) {
        final Response<UserJson> response;
        try {
            response = gatewayApi.sendInvitation(token, friend).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }
}
