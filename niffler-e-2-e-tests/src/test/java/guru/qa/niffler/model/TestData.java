package guru.qa.niffler.model;

import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.user.UserJson;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record TestData(String password,
                       @Nonnull List<UserJson> incomeInvitations,
                       @Nonnull List<UserJson> outcomeInvitations,
                       @Nonnull List<UserJson> friends,
                       @Nonnull List<CategoryJson> categories,
                       @Nonnull List<SpendJson> spendings) {

}
