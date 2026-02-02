package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApi {

    @GET("register")
    Call<Void> requestRegisterForm();

    @POST("register")
    @FormUrlEncoded
    Call<Void> register(
            @Field("username") String username,
            @Field("password") String password,
            @Field("passwordSubmit") String passwordSubmit,
            @Field("_csrf") String csrf);

    @GET("oauth2/authorize")
    Call<Void> authorize(
            @Query("response_type") String response_type,
            @Query("clientId") String clientId,
            @Query("scope") String scope,
            @Query(value = "redirectUri", encoded = true) String redirectUri,
            @Query("codeChallenge") String codeChallenge,
            @Query("codeChallengeMethod") String codeChallengeMethod
    );

    @POST("login")
    @FormUrlEncoded
    Call<Void> login(
            @Field("username") String username,
            @Field("password") String password,
            @Field("_csrf") String csrf
    );

    @POST("oauth2/token")
    @FormUrlEncoded
    Call<JsonNode> token(
            @Field("code") String code,
            @Field(value = "redirectUri", encoded = true) String redirectUri,
            @Field("codeVerifier") String codeVerifier,
            @Field("grantType") String grantType,
            @Field("clientId") String clientId
    );
}
