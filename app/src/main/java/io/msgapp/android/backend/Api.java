package io.msgapp.android.backend;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by matheus on 11/8/15.
 */
public interface Api {

    @GET("users/username/available")
    Call<Boolean> usernameIsAvailable(@Query("username") String username);

    @GET("users/email/available")
    Call<Boolean> emailIsAvailable(@Query("email") String username);
}
