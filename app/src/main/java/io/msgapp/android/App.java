package io.msgapp.android;

import android.app.Application;
import android.widget.Toast;

import io.msgapp.android.backend.Api;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by matheus on 11/8/15.
 */
public class App extends Application {
    public Api api;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl(BuildVars.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }

    public void somethingWentWrong() {
        Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
    }
}
