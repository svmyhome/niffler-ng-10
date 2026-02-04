package guru.qa.niffler.jupiter.extension;

import static guru.qa.niffler.jupiter.extension.TestMethodContextExtension.context;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.service.UserClient;
import guru.qa.niffler.service.UserDbClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;
import utils.RandomDataUtils;

public class UserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
            UserExtension.class);
    public static final String DEFAULT_PASSWORD = "12345";
    private final UserClient userClient = new UserDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {

        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(usersAnno -> {
                    if ("".equals(usersAnno.username())) {
                        final String username = RandomDataUtils.randomUsername();
                        final UserJson user = userClient.createUser(username, DEFAULT_PASSWORD);
                        List<UserJson> incomeInvitations = userClient.createIncomeInvitations(user,
                                usersAnno.incomeInvitations());
                        List<UserJson> outcomeInvitations = userClient.createOutcomeInvitations(user,
                                usersAnno.outcomeInvitations());
                        List<UserJson> friends = userClient.createFriends(user, usersAnno.friends());

                        final TestData testData = new TestData(
                                DEFAULT_PASSWORD,
                                incomeInvitations,
                                outcomeInvitations,
                                friends,
                                new ArrayList<>(),
                                new ArrayList<>()
                        );
                        setUser(user.addTestData(testData));
                    }
                });
    }

    public static void setUser(UserJson user) {
        final ExtensionContext context = TestMethodContextExtension.context();
        context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                user
        );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    @Nonnull
    public UserJson resolveParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return createdUser().orElseThrow();
    }


    public static Optional<UserJson> createdUser() {
        final ExtensionContext methodContext = context();
        return getUserJson();
    }

    public static Optional<UserJson> getUserJson() {
        final ExtensionContext context = TestMethodContextExtension.context();
        return Optional.ofNullable(context.getStore(NAMESPACE)
                .get(context.getUniqueId(), UserJson.class));
    }
}
