package guru.qa.niffler.service;

import guru.qa.niffler.model.user.UserJson;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UserClient {

    @Nonnull
    UserJson createUser(String username, String password);

    @Nonnull
    List<UserJson> addIncomeInvitation(UserJson targetUser, int count);

    @Nonnull
    List<UserJson> addOutcomeInvitation(UserJson targetUser, int count);

    @Nonnull
    List<UserJson> addFriend(UserJson targetUser, int count);
}
