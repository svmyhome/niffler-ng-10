package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.CurrenciesQuery;
import guru.qa.CurrenciesQuery.Currency;
import guru.qa.CurrenciesQuery.Data;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.spend.CurrencyValues;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CurrenciesGraphQlTest extends BaseGraphQlTest{

    @Test
    @User
    @ApiLogin
    void allCurrenciesShouldBeReturnedFromGateway(@Token String token) {
        ApolloCall<Data> currencyCall = apolloClient.query(new CurrenciesQuery()).addHttpHeader("authorization", token);
        ApolloResponse<Data> apolloResponse = Rx2Apollo.single(currencyCall).blockingGet();

        final Data data = apolloResponse.dataOrThrow();

        List<Currency> currencies = data.currencies;

        Assertions.assertEquals(
                CurrencyValues.RUB.name(), currencies.get(0).currency.rawValue
        );
        Assertions.assertEquals(
                CurrencyValues.KZT.name(), currencies.get(1).currency.rawValue
        );
        Assertions.assertEquals(
                CurrencyValues.EUR.name(), currencies.get(2).currency.rawValue
        );
        Assertions.assertEquals(
                CurrencyValues.USD.name(), currencies.get(3).currency.rawValue
        );

    }
}
