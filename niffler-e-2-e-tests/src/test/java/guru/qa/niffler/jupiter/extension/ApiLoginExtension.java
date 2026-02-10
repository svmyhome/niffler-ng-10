package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.model.FriendshipStatus;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.service.AuthApiClient;
import guru.qa.niffler.service.SpendApiClient;
import guru.qa.niffler.service.UserApiClient;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;
import org.openqa.selenium.Cookie;

public class ApiLoginExtension implements BeforeEachCallback, ParameterResolver {

    public static final Config CFG = Config.getInstance();
    public final SpendApiClient spendApiClient = new SpendApiClient();
    public final UserApiClient userApiClient = new UserApiClient();

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
            ApiLoginExtension.class);

    public final AuthApiClient authApiClient = new AuthApiClient();

    private final Boolean setupBrowser;

    private ApiLoginExtension(Boolean setupBrowser) {
        this.setupBrowser = setupBrowser;
    }

    public ApiLoginExtension() {
        this.setupBrowser = true;
    }

    public static ApiLoginExtension rest() {
        return new ApiLoginExtension(false);
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), ApiLogin.class)
                .ifPresent(apiLogin -> {
                    final UserJson userToLogin;
                    final String username = apiLogin.username();
                    final String password = apiLogin.password();
                    final Optional<UserJson> userFromUserExtension = UserExtension.createdUser();
                    if ("".equals(username) || "".equals(password)) {
                        if (userFromUserExtension.isEmpty()) {
                            throw new IllegalStateException("@User must be present in case that @ApiLogin is empty!");
                        }
                        userToLogin = userFromUserExtension.get();
                    } else {
                        if (userFromUserExtension.isPresent()) {
                            throw new IllegalStateException("@User must not be present in case that @ApiLogin contains username or password!");
                        }
                        final List<CategoryJson> categories = spendApiClient.findAllCategories(username);
                        final List<SpendJson> spends = spendApiClient.findSpendsByUserName(username);
                        final List<UserJson> allFriends = userApiClient.getAllFriends(username);
                        final List<UserJson> friends = allFriends.stream()
                                .filter(f -> f.friendshipStatus()!=null && f.friendshipStatus().equals(FriendshipStatus.FRIEND))
                                .toList();
                        final List<UserJson> incomeInvitations = allFriends.stream()
                                .filter(f -> f.friendshipStatus()!=null && f.friendshipStatus().equals(FriendshipStatus.INVITE_RECEIVED))
                                .toList();

                        final List<UserJson> outcomeInvitations = userApiClient.getAllUsers(username).stream()
                                .filter(f -> f.friendshipStatus()!=null && f.friendshipStatus().equals(FriendshipStatus.INVITE_SENT))
                                .toList();
                        UserJson fakeUser = new UserJson(
                                username,
                                new TestData(
                                        password,
                                        incomeInvitations,
                                        outcomeInvitations,
                                        friends,
                                        categories,
                                        spends
                                )
                        );
                        UserExtension.setUser(fakeUser);
                        userToLogin = fakeUser;
                    }

                    final String token = authApiClient.login(
                            userToLogin.username(),
                            userToLogin.testData().password()
                    );

                    setToken(token);

                    if (setupBrowser) {
                        Selenide.open(CFG.frontUrl());
                        Selenide.localStorage().setItem("id_token", getToken());
                        WebDriverRunner.getWebDriver().manage().addCookie(
                                new Cookie(
                                        "JSESSIONID",
                                        ThreadSafeCookieStore.INSTANCE.cookieValue("JSESSIONID")
                                )
                        );
                        Selenide.open(MainPage.URL, MainPage.class).mainPageShouldBeDisplayed();
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(String.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), Token.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return "Bearer " + getToken();
    }

    public static void setToken(String token) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("token", token);
    }

    public static String getToken() {
        return TestMethodContextExtension.context().getStore(NAMESPACE)
                .get("token", String.class);
    }

    public static void setCode(String code) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("code", code);
    }

    public static String getCode() {
        return TestMethodContextExtension.context().getStore(NAMESPACE)
                .get("code", String.class);
    }

    public static Cookie getJsessionIdCookie() {
        return new Cookie(
                "JSESSIONID",
                ThreadSafeCookieStore.INSTANCE.cookieValue("JSESSIONID")
        );
    }
}
