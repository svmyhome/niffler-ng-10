package guru.qa.niffler.service;

import guru.qa.niffler.api.UserdataSoapApi;
import guru.qa.niffler.api.core.converter.SoapConverterFactory;
import io.qameta.allure.Step;
import java.io.IOException;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import jaxb.userdata.CurrentUserRequest;
import jaxb.userdata.UserResponse;
import okhttp3.logging.HttpLoggingInterceptor;

@ParametersAreNonnullByDefault
public final class UserdataSoapClient extends RestClient {

    private final UserdataSoapApi userdataSoapApi;

    public UserdataSoapClient() {
        super(CFG.userdataUrl(), SoapConverterFactory.create("niffler-userdata"), false, HttpLoggingInterceptor.Level.BODY);
        this.userdataSoapApi = create(UserdataSoapApi.class);
    }

    @Step("Get current user info using SOAP")
    public @Nonnull UserResponse currentUser(CurrentUserRequest request) throws IOException {
        return userdataSoapApi.getCurrentUser(request).execute().body();
    }


}
