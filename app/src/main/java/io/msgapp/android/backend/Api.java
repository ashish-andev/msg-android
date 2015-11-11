package io.msgapp.android.backend;

import com.squareup.okhttp.ResponseBody;

import io.msgapp.android.BuildVars;
import io.msgapp.android.model.User;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by matheus on 11/8/15.
 */
public interface Api {

    @GET("users/username/available")
    Call<Boolean> usernameIsAvailable(@Query("username") String username);

    @GET("users/email/available")
    Call<Boolean> emailIsAvailable(@Query("email") String username);

    @POST("users")
    Call<User> createUser(@Body User user);

    @Headers("Authorization: Basic " + BuildVars.CLIENT_ID_SECRET_BASE_64)
    @FormUrlEncoded
    @POST("oauth/token")
    Call<ResponseBody> getOAuthToken(@Field("grant_type") String grantType,
                                     @Field("username") String username,
                                     @Field("password") String password);
}
