package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.UserType;
import guru.qa.niffler.jupiter.annotation.UserType.FriendType;
import guru.qa.niffler.model.user.StaticUser;
import io.qameta.allure.Allure;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

public class UserQueueExtension implements
        BeforeEachCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
            UserQueueExtension.class);

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIENDS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUESTS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUESTS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser("mouse", "12345", null, null, null));
        WITH_FRIENDS.add(new StaticUser("dog", "12345", "horse", null, null));
        WITH_INCOME_REQUESTS.add(new StaticUser("elefant", "12345", null, "cat", null));
        WITH_OUTCOME_REQUESTS.add(new StaticUser("cat", "12345", null, null, "elefant"));
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        Map<FriendType, StaticUser> users = new HashMap<>();

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .forEach(p -> {
                    UserType ut = p.getAnnotation(UserType.class);
                    Optional<StaticUser> user = Optional.empty();
                    StopWatch sw = StopWatch.createStarted();
                    while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                        user = Optional.ofNullable(queueByType(ut.value()).poll());
                    }
                    Allure.getLifecycle().updateTestCase(tc ->
                            tc.setStart(new Date().getTime())
                    );
                    user.ifPresentOrElse(
                            u -> users.put(ut.value(), u),
                            () -> {
                                throw new IllegalStateException("Can't obtain user after 30s.");
                            }
                    );
                });
        context.getStore(NAMESPACE).put(context.getUniqueId(), users);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        Map<UserType.FriendType, StaticUser> users = context.getStore(NAMESPACE)
                .get(context.getUniqueId(), Map.class);

        if (users!=null) {
            for (Map.Entry<UserType.FriendType, StaticUser> entry : users.entrySet()) {
                queueByType(entry.getKey()).add(entry.getValue());
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    @Nonnull
    public StaticUser resolveParameter(ParameterContext parameterContext,
                                       ExtensionContext extensionContext) throws ParameterResolutionException {
        UserType ut = parameterContext.findAnnotation(UserType.class)
                .orElseThrow();

        Map<UserType.FriendType, StaticUser> users = extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class);

        if (users==null) {
            throw new ParameterResolutionException("No users map found in store");
        }

        StaticUser user = users.get(ut.value());
        if (user==null) {
            throw new ParameterResolutionException("No user found for annotation: " + ut.value());
        }
        return user;
    }

    @Nonnull
    private Queue<StaticUser> queueByType(UserType.FriendType type) {
        return switch (type) {
            case EMPTY -> EMPTY_USERS;
            case WITH_FRIEND -> WITH_FRIENDS;
            case WITH_INCOME_REQUEST -> WITH_INCOME_REQUESTS;
            case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUESTS;
        };
    }
}