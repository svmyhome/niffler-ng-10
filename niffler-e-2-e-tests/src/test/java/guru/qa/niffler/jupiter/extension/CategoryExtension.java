package guru.qa.niffler.jupiter.extension;

import static utils.RandomDataUtils.randomCategoryName;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.SpendApiClient;
import guru.qa.niffler.service.SpendClient;
import java.util.Optional;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements
    BeforeEachCallback,
    AfterTestExecutionCallback,
    ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
      CategoryExtension.class);
  private final SpendClient spendClient = new SpendApiClient();

  @Override
  public void beforeEach(ExtensionContext context) {
    Optional<User> user = AnnotationSupport.findAnnotation(context.getRequiredTestMethod(),
        User.class);

    if (user.isPresent() && user.get().categories().length > 0) {
      Category category = user.get().categories()[0];
      CategoryJson created = spendClient.createCategory(
          new CategoryJson(
              null,
              randomCategoryName(),
              user.get().username(),
              category.archived()
          )
      );
      if (category.archived()) {
        CategoryJson archivedCategory = new CategoryJson(
            created.id(),
            created.name(),
            user.get().username(),
            true
        );
        created = spendClient.updateCategory(archivedCategory);
      }

      context.getStore(NAMESPACE).put(
          context.getUniqueId(),
          created
      );
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
  }

  @Override
  public CategoryJson resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE)
        .get(extensionContext.getUniqueId(), CategoryJson.class);
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    try {
      CategoryJson category = context.getStore(NAMESPACE)
          .get(context.getUniqueId(), CategoryJson.class);
      if (category != null && !category.archived()) {
        CategoryJson archivedCategory = new CategoryJson(
            category.id(),
            category.name(),
            category.username(),
            true
        );
        spendClient.updateCategory(archivedCategory);
      }
    } catch (Exception e) {
      System.err.println("Failed to archive category:" + e.getMessage());
    }
  }
}
