package guru.qa.niffler.service;

import guru.qa.niffler.model.user.UserJson;
import java.util.List;

public interface UserClient {

    UserJson createUser(String username, String password);

    List<UserJson> createIncomeInvitations(UserJson targetUser, int count);

    List<UserJson> createOutcomeInvitations(UserJson targetUser, int count);

    List<UserJson> createFriends(UserJson targetUser, int count);
}
