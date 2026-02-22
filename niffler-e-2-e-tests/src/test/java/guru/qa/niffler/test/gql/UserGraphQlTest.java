package guru.qa.niffler.test.gql;

import static io.qameta.allure.Allure.step;
import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.AllPeopleQuery;
import guru.qa.FriendsCategoriesQuery;
import guru.qa.RecursiveFriendsQuery;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.meta.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserGraphQlTest extends BaseGraphQlTest {

    @Test
    @ApiLogin
    @User(friends = 5)
    @DisplayName("GQL: Should not show category for another user")
    public void shouldNotQueryForAnotherUserCategories(@Token String bearerToken) {
        final String errorMessage = "Can`t query categories for another user";
        ApolloCall<FriendsCategoriesQuery.Data> categoriesCall = apolloClient.query(new FriendsCategoriesQuery())
                .addHttpHeader("authorization", bearerToken);

        ApolloResponse<FriendsCategoriesQuery.Data> response = Rx2Apollo.single(categoriesCall).blockingGet();

        step("Verify that error message equals expected text", () ->
                Assertions.assertEquals(
                        errorMessage, response.errors.getFirst().getMessage()
                )
        );
    }

    @Test
    @User(
            friends = 5
    )
    @ApiLogin
    @DisplayName("GQL: Should not allow fetching more than 2 friends sub-queries")
    void shouldNotFetchSubQuriesFromGateway(@Token String token) {
        final String errorMessage = "Can`t fetch over 2 friends sub-queries";

        ApolloCall<RecursiveFriendsQuery.Data> statCall = apolloClient.query(
                new RecursiveFriendsQuery()).addHttpHeader("authorization", token);
        ApolloResponse<RecursiveFriendsQuery.Data> response = Rx2Apollo.single(statCall).blockingGet();

        step("Verify that error message equals expected text", () ->
                Assertions.assertEquals(
                        errorMessage, response.errors.getFirst().getMessage()
                )
        );
    }

    @Test
    @User(
            outcomeInvitations = 1
    )
    @ApiLogin
    @DisplayName("GQL: Should return 5 people")
    void shouldQueryReturnFivePeopleFromGateway(@Token String token) {

        ApolloCall<AllPeopleQuery.Data> statCall = apolloClient.query(
                new AllPeopleQuery()).addHttpHeader("authorization", token);
        ApolloResponse<AllPeopleQuery.Data> response = Rx2Apollo.single(statCall).blockingGet();

        step("Verify total people count equals 5", () ->
                Assertions.assertEquals(
                        5, response.data.allPeople.edges.size()
                )
        );

        step("Verify that error message equals expected text", () ->
                Assertions.assertEquals(
                        "INVITE_SENT", response.data.allPeople.edges.getFirst().node.friendshipStatus.rawValue
                )
        );
    }
}
