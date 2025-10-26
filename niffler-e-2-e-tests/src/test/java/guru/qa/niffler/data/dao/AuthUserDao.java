package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.AuthUserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserDao {

  AuthUserEntity create(AuthUserEntity user);

  AuthUserEntity update(AuthUserEntity user);

  Optional<AuthUserEntity> findByUsername(String username);

  Optional<AuthUserEntity> findById(UUID id);

  List<AuthUserEntity> findAllById(UUID id);

  void delete(AuthUserEntity user);
}
