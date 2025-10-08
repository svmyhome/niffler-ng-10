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
  public void beforeEach(ExtensionContext context) throws Exception {
    Optional<User> user = AnnotationSupport.findAnnotation(context.getRequiredTestMethod(),
        User.class);

    Category category = null;
    if (user.isPresent() && user.get().categories().length > 0) {
      category = user.get().categories()[0];
    }
    if (category != null) {
      CategoryJson created = spendClient.createCategory(
          new CategoryJson(
              null,
              randomCategoryName(),
              category.username(),
              category.archived()
          )
      );
      if (category.archived()) {
        CategoryJson archivedCategory = new CategoryJson(
            created.id(),
            created.name(),
            created.username(),
            true
        );
        created = spendClient.updateCategory(archivedCategory);
      }

//    final SpendClient created = spendClient.createCategory();
//      AnnotationSupport.findAnnotation(
//        context.getRequiredTestMethod(),
//        Category.class
//    ).ifPresent(
//        anno -> {
//          CategoryJson created = spendClient.createCategory(
//              new CategoryJson(
//                  null,
//                  randomCategoryName(),
//                  anno.username(),
//                  anno.archived()
//              )
//          );
//          if (anno.archived()) {
//            CategoryJson archivedCategory = new CategoryJson(
//                created.id(),
//                created.name(),
//                created.username(),
//                true
//            );
//            created = spendClient.updateCategory(archivedCategory);
//          }
      context.getStore(NAMESPACE).put(
          context.getUniqueId(),
          created
//    );
//  }
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
  public void afterTestExecution(ExtensionContext context) throws Exception {
    try {
      CategoryJson category = context.getStore(NAMESPACE)
          .get(context.getUniqueId(), CategoryJson.class);
      if (category != null) {
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
    } catch (Exception e) {
      System.err.println("Failed to archive category:" + e.getMessage());
    }
  }
}
