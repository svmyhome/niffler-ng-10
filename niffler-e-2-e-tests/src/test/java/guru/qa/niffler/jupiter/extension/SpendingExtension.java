package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;
import java.util.Optional;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
            SpendingExtension.class);
    private final SpendClient spendClient = new SpendDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {

        Optional<User> user = AnnotationSupport.findAnnotation(context.getRequiredTestMethod(),
                User.class);

        if (user.isPresent() && user.get().spendings().length > 0) {
            Spending spending = user.get().spendings()[0];
            final SpendJson created = spendClient.createSpend(
                    new SpendJson(
                            null,
                            new Date(),
                            new CategoryJson(
                                    null,
                                    spending.category(),
                                    user.get().username(),
                                    false
                            ),
                            spending.currency(),
                            spending.amount(),
                            spending.description(),
                            user.get().username()
                    )
            );

            context.getStore(NAMESPACE).put(
                    context.getUniqueId(),
                    created
            );
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext,
                                      ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), SpendJson.class);
    }
}
