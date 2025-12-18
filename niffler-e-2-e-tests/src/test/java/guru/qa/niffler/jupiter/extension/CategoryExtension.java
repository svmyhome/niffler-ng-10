package guru.qa.niffler.jupiter.extension;

import static guru.qa.niffler.jupiter.extension.TestMethodContextExtension.context;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.SpendDbClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang.ArrayUtils;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;
import utils.RandomDataUtils;

public class CategoryExtension implements
    BeforeEachCallback,
    AfterTestExecutionCallback,
    ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
      CategoryExtension.class);
  private final SpendClient spendClient = new SpendDbClient();

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
        .ifPresent(usersAnno -> {
          if (ArrayUtils.isNotEmpty(usersAnno.categories())) {
            Optional<UserJson> testUser = UserExtension.createUser();
            final String username =
                testUser.isPresent() ? testUser.get().username() : usersAnno.username();

            List<CategoryJson> result = new ArrayList<>();
            for (Category categoryAnno : usersAnno.categories()) {
              CategoryJson category = new CategoryJson(
                  null,
                  "".equals(categoryAnno.name()) ? RandomDataUtils.randomCategoryName()
                      : categoryAnno.name(),
                  username,
                  categoryAnno.archived()
              );
              CategoryJson created = spendClient.createCategory(category);
              if (categoryAnno.archived()) {
                CategoryJson archivedCategory = new CategoryJson(
                    created.id(),
                    created.name(),
                    created.username(),
                    true
                );
                created = spendClient.updateCategory(archivedCategory);
              }
              result.add(created);
            }
            if (testUser.isPresent()) {
              testUser.get().testData().categories().addAll(result);
            } else {
              context.getStore(NAMESPACE).put(
                  context.getUniqueId(),
                  result.stream().toArray(CategoryJson[]::new)
              );
            }
          }
        });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson[].class);
  }

  @Override
  public CategoryJson[] resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return createCategory();
  }

  public static CategoryJson[] createCategory() {
    final ExtensionContext methodContext = context();
    return methodContext.getStore(NAMESPACE)
        .get(methodContext.getUniqueId(), CategoryJson[].class);
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    CategoryJson[] categories = createCategory();
    if (categories != null && categories.length > 0) {
      for (CategoryJson category : categories) {
        if (!category.archived()) {
          CategoryJson archivedCategory = new CategoryJson(
              category.id(),
              category.name(),
              category.username(),
              true
          );
          spendClient.updateCategory(archivedCategory);
        }
      }
    }
  }
}
