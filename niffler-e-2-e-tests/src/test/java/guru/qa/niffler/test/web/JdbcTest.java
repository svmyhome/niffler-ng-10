package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.impl.SpendDaoJdbc;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JdbcTest {

    @Test
    void daotest() {
        SpendDbClient spendDbClient = new SpendDbClient();
        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "test-cat-213",
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        100.0,
                        "test dsc",
                        "duck"
                )
        );
        System.out.println(spend);
    }

    @Test
    void spendIdDaotest() {
        SpendDaoJdbc spendDaoJdbc = new SpendDaoJdbc();
        Optional<SpendEntity> sp = spendDaoJdbc.findSpendById(UUID.fromString("b8e4f162-a8b9-11f0-ba80-6699f0f3bb28"));
        System.out.println(sp);

    }

    @Test
    void spendUserNameDaotest() {
        SpendDaoJdbc spendDaoJdbc = new SpendDaoJdbc();
        List<SpendEntity> sp = spendDaoJdbc.findAllByUsername("duck");
        System.out.println(sp);
        sp.stream().forEach(System.out::println);

    }

    @Test
    void spendDeleteSpendDaotest() {
        SpendDaoJdbc spendDaoJdbc = new SpendDaoJdbc();
        Optional<SpendEntity> sp = spendDaoJdbc.findSpendById(UUID.fromString("bd6de3e3-6c63-4fe6-8cf2-8fd648b5ca64"));
        if (sp.isPresent()) {
            spendDaoJdbc.deleteSpend(sp.get());
        }
    }

}
