package guru.qa.niffler.test.grpc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.grpc.NifflerCurrencyServiceGrpc;
import guru.qa.niffler.grpc.NifflerUserdataServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import utils.GrpcConsoleInterceptor;

public class BaseGrpcTest {

    protected static final Config CFG = Config.getInstance();

    protected static final Channel channel = ManagedChannelBuilder
            .forAddress(CFG.currencyGrpcAddress(), CFG.currencyGrpcPort())
            .intercept(new GrpcConsoleInterceptor())
            .usePlaintext()
            .build();

    protected static final NifflerCurrencyServiceGrpc.NifflerCurrencyServiceBlockingStub blockingStub
            = NifflerCurrencyServiceGrpc.newBlockingStub(channel);

    protected static final Channel userdataChannel = ManagedChannelBuilder
            .forAddress(CFG.userdataGrpcAddress(),  CFG.userdataGrpcPort())
            .intercept(new GrpcConsoleInterceptor())
            .usePlaintext()
            .build();

    protected static final NifflerUserdataServiceGrpc.NifflerUserdataServiceBlockingStub userdataBlockingStub
            = NifflerUserdataServiceGrpc.newBlockingStub(userdataChannel);
}
