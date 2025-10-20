package guru.qa.niffler.model;


import guru.qa.niffler.data.entity.UserEntity;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record UserJson(
        UUID id,
        String username,
        String firstname,
        String surname,
        String fullname,
        CurrencyValues currency,
        String photo,
        String photoSmall) {

    public static UserJson fromEntity(UserEntity entity) {
        return new UserJson(
                entity.getId(),
                entity.getUsername(),
                entity.getFirstname(),
                entity.getSurname(),
                entity.getFullname(),
                entity.getCurrency(),
                entity.getPhoto() != null && entity.getPhoto().length > 0 ? new String(entity.getPhoto(),
                        StandardCharsets.UTF_8) : null,
                entity.getPhotoSmall() != null && entity.getPhotoSmall().length > 0 ? new String(
                        entity.getPhotoSmall(), StandardCharsets.UTF_8) : null
        );
    }

}
