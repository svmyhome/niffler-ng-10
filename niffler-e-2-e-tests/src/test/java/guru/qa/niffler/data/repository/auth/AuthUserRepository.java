package guru.qa.niffler.data.repository.auth;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.DataAccessException;

public interface AuthUserRepository {

  AuthUserEntity create(AuthUserEntity user);

  Optional<AuthUserEntity> findByUsername(String username);

  Optional<AuthUserEntity> findById(UUID id);

  void delete(AuthUserEntity user);

  List<AuthUserEntity> findAll();
}
