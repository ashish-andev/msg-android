package io.msgapp.android.backend;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by matheus on 11/8/15.
 */
public interface Api {

    @GET("public/username/available")
    Call<Boolean> usernameIsAvailable(@Query("username") String username);

    @GET("public/email/available")
    Call<Boolean> emailIsAvailable(@Query("email") String username);
}
