package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.UserdataUserDAO;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.impl.SpendDaoJdbc;
import guru.qa.niffler.data.impl.UserdataUserDAOJdbc;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;

public class SpendDbClient {

    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final SpendDao spendDao = new SpendDaoJdbc();
    private final UserdataUserDAO userDAO = new UserdataUserDAOJdbc();

    public SpendJson createSpend(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
        if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = categoryDao.createCategory(
                    spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
        }
        return SpendJson.fromEntity(
                spendDao.createSpend(spendEntity)
        );
    }

    public UserJson createUser(UserJson user) {
        UserEntity userEntity = UserEntity.fromJson(user);
        return UserJson.fromEntity(userDAO.createUser(userEntity));
    }

//
//    public Optional<SpendJson> findSpendById(UUID id) {
//        return Optional.of(SpendJson.fromEntity(spendDao.findSpendById(id)));
//    }

}
