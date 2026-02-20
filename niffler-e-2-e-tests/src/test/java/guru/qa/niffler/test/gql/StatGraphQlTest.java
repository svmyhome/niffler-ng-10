package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.StatQuery;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.meta.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StatGraphQlTest extends BaseGraphQlTest {

    @Test
    @User
    @ApiLogin
    void allStatShouldBeReturnedFromGateway(@Token String token) {
        ApolloCall<StatQuery.Data> statCall = apolloClient.query(StatQuery.builder()
                .filterCurrency(null)
                .statCurrency(null)
                .filterPeriod(null)
                .build()).addHttpHeader("authorization", token);
        ApolloResponse<StatQuery.Data> apolloResponse = Rx2Apollo.single(statCall).blockingGet();

        final StatQuery.Data data = apolloResponse.dataOrThrow();

        StatQuery.Stat stat = data.stat;

        Assertions.assertEquals(
                0.0, stat.total
        );
    }
}
