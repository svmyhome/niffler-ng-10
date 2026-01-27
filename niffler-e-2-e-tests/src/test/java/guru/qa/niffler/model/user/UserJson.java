package guru.qa.niffler.model.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.spend.CurrencyValues;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import javax.annotation.Nonnull;

public record UserJson(
        UUID id,
        String username,
        String firstname,
        String surname,
        String fullname,
        CurrencyValues currency,
        String photo,
        String photoSmall,
        @JsonIgnore
        FriendshipStatus friendshipStatus,
        @JsonIgnore
        TestData testData) {

    @Nonnull
    public static UserJson fromEntity(@Nonnull UserEntity entity) {
        return new UserJson(
                entity.getId(),
                entity.getUsername(),
                entity.getFirstname(),
                entity.getSurname(),
                entity.getFullname(),
                entity.getCurrency(),
                entity.getPhoto()!=null && entity.getPhoto().length > 0 ? new String(entity.getPhoto(),
                        StandardCharsets.UTF_8):null,
                entity.getPhotoSmall()!=null && entity.getPhotoSmall().length > 0 ? new String(
                        entity.getPhotoSmall(), StandardCharsets.UTF_8):null,
                null,
                null
        );
    }

    @Nonnull
    public UserJson addTestData(@Nonnull TestData testData) {
        return new UserJson(
                id,
                username,
                firstname,
                surname,
                fullname,
                currency,
                photo,
                photoSmall,
                friendshipStatus,
                testData);
    }
}
