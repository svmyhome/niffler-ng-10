package guru.qa.niffler.config;

import javax.annotation.Nonnull;

public interface Config {


    @Nonnull
    static Config getInstance() {
        return "docker".equals(System.getProperty("test.env"))
                ? DockerConfig.INSTANCE
                :LocalConfig.INSTANCE;
    }

    @Nonnull
    String frontUrl();


    @Nonnull
    String spendUrl();

    @Nonnull
    String spendJdbcUrl();

    @Nonnull
    String ghUrl();

    @Nonnull
    String authUrl();

    @Nonnull
    String authJdbcUrl();

    @Nonnull
    String userdataUrl();

    @Nonnull
    String userdataJdbcUrl();

    @Nonnull
    String currencyUrl();

    @Nonnull
    String currencyGrpcAddress();

    default int currencyGrpcPort() {
        return 8092;
    }

    @Nonnull
    String userdataGrpcAddress();

    default int userdataGrpcPort() {
        return 8094;
    }

    @Nonnull
    String currencyJdbcUrl();

    @Nonnull
    String gatewayUrl();
}
