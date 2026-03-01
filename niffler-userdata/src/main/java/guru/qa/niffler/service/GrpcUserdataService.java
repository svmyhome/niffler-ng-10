package guru.qa.niffler.service;

import com.google.protobuf.Empty;
import guru.qa.niffler.grpc.FriendRequest;
import guru.qa.niffler.grpc.FriendshipRequest;
import guru.qa.niffler.grpc.NifflerUserdataServiceGrpc;
import guru.qa.niffler.grpc.UserRequest;
import guru.qa.niffler.grpc.UserResponse;
import guru.qa.niffler.grpc.UsernameRequest;
import guru.qa.niffler.grpc.UsersRequest;
import guru.qa.niffler.model.UserJson;
import io.grpc.stub.StreamObserver;
import java.util.Optional;
import java.util.UUID;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

        UserResponse.Builder builder = UserResponse.newBuilder()
                .setUsername(user.username());

        Optional.ofNullable(user.id())
                .map(UUID::toString)
                .ifPresent(builder::setId);

        Optional.ofNullable(user.firstname())
                .ifPresent(builder::setFirstname);

        Optional.ofNullable(user.surname())
                .ifPresent(builder::setSurname);

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getAllUser(UsersRequest request, StreamObserver<UserResponse> responseObserver) {
        super.getAllUser(request, responseObserver);
    }

    @Override
    public void getAllPage(UsersRequest request, StreamObserver<UserResponse> responseObserver) {
        super.getAllPage(request, responseObserver);
    }

    @Override
    public void updateUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        super.updateUser(request, responseObserver);
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
}
