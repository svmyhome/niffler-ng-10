package guru.qa.niffler.service;

import com.google.protobuf.Empty;
import guru.qa.niffler.data.CurrencyValues;
import guru.qa.niffler.grpc.FriendRequest;
import guru.qa.niffler.grpc.FriendshipRequest;
import guru.qa.niffler.grpc.NifflerUserdataServiceGrpc;
import guru.qa.niffler.grpc.StreamUserRequest;
import guru.qa.niffler.grpc.UpdateUserRequest;
import guru.qa.niffler.grpc.UserRequest;
import guru.qa.niffler.grpc.UserResponse;
import guru.qa.niffler.grpc.UsernameRequest;
import guru.qa.niffler.grpc.UsersResponse;
import guru.qa.niffler.model.FriendshipStatus;
import guru.qa.niffler.model.IUserJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.UserJsonBulk;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@GrpcService
public class GrpcUserdataService extends NifflerUserdataServiceGrpc.NifflerUserdataServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcUserdataService.class);

    private final UserService userService;

    @Autowired
    public GrpcUserdataService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void getUser(UsernameRequest request, StreamObserver<UserResponse> responseObserver) {
        final UserJson user = userService.getCurrentUser(request.getUsername());
        responseObserver.onNext(fromJson(user));
        responseObserver.onCompleted();
    }

    @Override
    public void listAllUser(UserRequest request, StreamObserver<UsersResponse> responseObserver) {
        List<UserJsonBulk> users = userService.allUsers(request.getUsername(), request.getSearchQuery());

        UsersResponse.Builder responseBuilder = UsersResponse.newBuilder();

        for (UserJsonBulk user : users) {
            UserResponse.Builder userBuilder = UserResponse.newBuilder()
                    .setUsername(user.username());

            Optional.ofNullable(user.id())
                    .map(UUID::toString)
                    .ifPresent(userBuilder::setId);

            Optional.ofNullable(user.firstname())
                    .ifPresent(userBuilder::setFirstname);

            Optional.ofNullable(user.surname())
                    .ifPresent(userBuilder::setSurname);

            Optional.ofNullable(user.fullname())
                    .ifPresent(userBuilder::setFullname);

            Optional.ofNullable(user.currency())
                    .map(Enum::name)
                    .map(guru.qa.niffler.grpc.CurrencyValues::valueOf)
                    .ifPresent(userBuilder::setCurrency);

            Optional.ofNullable(user.photo())
                    .ifPresent(userBuilder::setPhoto);

            Optional.ofNullable(user.photoSmall())
                    .ifPresent(userBuilder::setPhotoSmall);

            Optional.ofNullable(user.friendshipStatus())
                    .map(Enum::name)
                    .map(guru.qa.niffler.grpc.FriendshipStatus::valueOf)
                    .ifPresent(userBuilder::setFriendshipStatus);

            responseBuilder.addUser(userBuilder.build());
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }


    @Override
    public void getAllPage(StreamUserRequest request, StreamObserver<UserResponse> responseObserver) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<UserJsonBulk> usersPage = userService.allUsers(
                request.getUsername(),
                pageable,
                request.getSearchQuery()
        );

        for (UserJsonBulk user : usersPage.getContent()) {
            UserResponse.Builder builder = UserResponse.newBuilder()
                    .setUsername(user.username());

            Optional.ofNullable(user.id())
                    .map(UUID::toString)
                    .ifPresent(builder::setId);

            Optional.ofNullable(user.firstname())
                    .ifPresent(builder::setFirstname);

            Optional.ofNullable(user.surname())
                    .ifPresent(builder::setSurname);

            Optional.ofNullable(user.fullname())
                    .ifPresent(builder::setFullname);

            Optional.ofNullable(user.currency())
                    .map(Enum::name)
                    .map(guru.qa.niffler.grpc.CurrencyValues::valueOf)
                    .ifPresent(builder::setCurrency);

            Optional.ofNullable(user.photo())
                    .ifPresent(builder::setPhoto);

            Optional.ofNullable(user.photoSmall())
                    .ifPresent(builder::setPhotoSmall);

            Optional.ofNullable(user.friendshipStatus())
                    .map(Enum::name)
                    .map(guru.qa.niffler.grpc.FriendshipStatus::valueOf)
                    .ifPresent(builder::setFriendshipStatus);

            responseObserver.onNext(builder.build());
        }

        responseObserver.onCompleted();
    }


    @Override
    @Transactional
    public void updateUser(UpdateUserRequest request, StreamObserver<UserResponse> responseObserver) {
        final UserJson user = userService.getCurrentUser(request.getUsername());

        UserJson updatedUser = new UserJson(
                user.id(),
                request.getUsername(),
                request.hasFirstname() ? request.getFirstname() : user.firstname(),
                request.hasSurname() ? request.getSurname() : user.surname(),
                request.hasFullname() ? request.getFullname() : user.fullname(),
                request.hasCurrency() ? CurrencyValues.valueOf(request.getCurrency().name()) : user.currency(),
                request.hasPhoto() ? request.getPhoto() : user.photo(),
                request.hasPhotoSmall() ? request.getPhotoSmall() : user.photoSmall(),
                request.hasFriendshipStatus() ? FriendshipStatus.valueOf(request.getFriendshipStatus().name()) : user.friendshipStatus()
        );

        userService.update(updatedUser);

        UserResponse.Builder builder = UserResponse.newBuilder()
                .setUsername(updatedUser.username());

        if (updatedUser.id() != null) {
            builder.setId(updatedUser.id().toString());
        }
        if (updatedUser.firstname() != null && !updatedUser.firstname().isEmpty()) {
            builder.setFirstname(updatedUser.firstname());
        }
        if (updatedUser.surname() != null && !updatedUser.surname().isEmpty()) {
            builder.setSurname(updatedUser.surname());
        }
        if (updatedUser.fullname() != null && !updatedUser.fullname().isEmpty()) {
            builder.setFullname(updatedUser.fullname());
        }
        if (updatedUser.currency() != null) {
            builder.setCurrency(guru.qa.niffler.grpc.CurrencyValues.valueOf(updatedUser.currency().name()));
        }
        if (updatedUser.photo() != null && !updatedUser.photo().isEmpty()) {
            builder.setPhoto(updatedUser.photo());
        }
        if (updatedUser.photoSmall() != null && !updatedUser.photoSmall().isEmpty()) {
            builder.setPhotoSmall(updatedUser.photoSmall());
        }
        if (updatedUser.friendshipStatus() != null) {
            builder.setFriendshipStatus(guru.qa.niffler.grpc.FriendshipStatus.valueOf(updatedUser.friendshipStatus().name()));
        }

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getAllFriends(FriendRequest request, StreamObserver<UserResponse> responseObserver) {
        super.getAllFriends(request, responseObserver);
    }

    @Override
    public void acceptFriendshipRequest(FriendshipRequest request, StreamObserver<UserResponse> responseObserver) {
        super.acceptFriendshipRequest(request, responseObserver);
    }

    @Override
    public void declineFriendshipRequest(FriendshipRequest request, StreamObserver<UserResponse> responseObserver) {
        super.declineFriendshipRequest(request, responseObserver);
    }

    @Override
    public void removeFriend(FriendshipRequest request, StreamObserver<Empty> responseObserver) {
        super.removeFriend(request, responseObserver);
    }

    private @Nonnull UserResponse fromJson(@Nonnull IUserJson userJson) {
        UserResponse.Builder user = UserResponse.newBuilder()
                .setId(userJson.id().toString())
                .setUsername(userJson.username());

        if (userJson.firstname()!=null && !userJson.firstname().isBlank()) {
            user.setFirstname(userJson.firstname());
        }

        if (userJson.surname()!=null && !userJson.surname().isBlank()) {
            user.setSurname(userJson.surname());
        }

        if (userJson.currency()!=null) {
            user.setCurrency(guru.qa.niffler.grpc.CurrencyValues.valueOf(userJson.currency().name()));
        }

        if (userJson.photo()!=null) {
            user.setPhoto(userJson.photo());
        }

        if (userJson.photoSmall()!=null) {
            user.setPhotoSmall(userJson.photoSmall());
        }

        if (userJson.friendshipStatus()!=null) {
            user.setFriendshipStatus(
                    guru.qa.niffler.grpc.FriendshipStatus.valueOf(userJson.friendshipStatus().name())
            );
        }

        if (userJson instanceof UserJsonBulk bulk) {
            if (bulk.fullname()!=null && !bulk.fullname().isBlank()) {
                user.setFullname(bulk.fullname());
            }
        }
        return user.build();
    }
}
