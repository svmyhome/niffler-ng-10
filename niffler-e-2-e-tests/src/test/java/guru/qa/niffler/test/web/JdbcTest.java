package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.impl.SpendDaoJdbc;
import guru.qa.niffler.data.impl.UserdataUserDAOJdbc;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
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
    void userDAOtest() {
        SpendDbClient spendDbClient = new SpendDbClient();
        UserJson user = spendDbClient.createUser(
                new UserJson(
                        null,
                        "bird",
                        "First",
                        "Sure",
                        "Full",
                        CurrencyValues.RUB,
                        "123467890",
                        "123467890"
                )
        );
        System.out.println(user);
    }

    @Test
    void spendIdDaotest() {
        SpendDaoJdbc spendDaoJdbc = new SpendDaoJdbc();
        Optional<SpendEntity> sp = spendDaoJdbc.findSpendById(UUID.fromString("b8e4f162-a8b9-11f0-ba80-6699f0f3bb28"));
        System.out.println(sp.get().getId());
        System.out.println(sp.get().getAmount());
        System.out.println(sp.get().getSpendDate());
        System.out.println(sp.get().getCategory().getName());
        System.out.println(sp.get().getCurrency());
        System.out.println(sp.get().getDescription());
        System.out.println(sp.get().getUsername());

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

    @Test
    void categoryByNameCategoryDaotest() {
        CategoryDaoJdbc categoryDaoJdbc = new CategoryDaoJdbc();
        Optional<CategoryEntity> ce = categoryDaoJdbc.findCategoryByUsernameAndCategoryName("duck", "111");
        System.out.println(ce);
    }

    @Test
    void lstCategory() {
        CategoryDaoJdbc categoryDaoJdbc = new CategoryDaoJdbc();
        List<CategoryEntity> ca = categoryDaoJdbc.findAllByUsername("duck");
        System.out.println(ca);
    }

    @Test
    void CategoryDeleteDaotest() {
        CategoryDaoJdbc categoryDaoJdbc = new CategoryDaoJdbc();
        Optional<CategoryEntity> sp = categoryDaoJdbc.findCategoryById(UUID.fromString("1d924dc9-e735-496f-9dfd-c21ee154454e"));
        if (sp.isPresent()) {
            categoryDaoJdbc.deleteCategory(sp.get());
        }
    }

    @Test
    void userIdDaotest() {
        UserdataUserDAOJdbc userdataUserDAOJdbc = new UserdataUserDAOJdbc();
        Optional<UserEntity> sp = userdataUserDAOJdbc.findById(UUID.fromString("0e5f972a-a9f3-11f0-a5b1-ce53de3c691f"));
        System.out.println(sp.get().getId());
        System.out.println(sp.get().getCurrency());
        System.out.println(sp.get().getUsername());

    }

    @Test
    void userNameDaotest() {
        UserdataUserDAOJdbc userdataUserDAOJdbc = new UserdataUserDAOJdbc();
        Optional<UserEntity> sp = userdataUserDAOJdbc.findByUsername("bird");
        System.out.println(sp.get().getId());
        System.out.println(sp.get().getCurrency());
        System.out.println(sp.get().getUsername());

    }

    @Test
    void UserDeleteDaotest() {
        UserdataUserDAOJdbc userdataUserDAOJdbc = new UserdataUserDAOJdbc();
        Optional<UserEntity> sp = userdataUserDAOJdbc.findById(UUID.fromString("fb6699ce-1d2a-4929-b4d1-f33b92854cb4"));

        if (sp.isPresent()) {
            userdataUserDAOJdbc.delete(sp.get());
        }
    }


}
