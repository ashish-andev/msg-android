package io.msgapp.android;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.transition.Fade;
import android.transition.Transition;
import android.widget.EditText;
import android.widget.Toast;

import io.msgapp.android.backend.Api;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by matheus on 11/8/15.
 */
public class App extends Application {
    private boolean inited;
    private Api api;
    private Retrofit retrofit;
    private SharedPreferences currentUser;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    // TODO: the following moethods must be moved to another class (e.g. Helper.java)

    public void somethingWentWrong() {
        Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
    }

    /**
     * If messageId == 0, a previuos error message will be removed.
     * If iconId == 0, a previous icon will be removed.
     */ // TODO: rename?
    @SuppressWarnings("ConstantConditions")
    public void showTextInputLayoutError(TextInputLayout v, int messageId, int iconId) {
        if (messageId != 0) {
            v.setError(getString(messageId));
        } else {
            v.setError("");
        }

        if (iconId != 0) {
            v.getEditText().setCompoundDrawablesWithIntrinsicBounds(0, 0, iconId, 0);
        } else {
            v.getEditText().setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }

    public boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    public String trim(EditText editText) {
        return editText.getText().toString().trim();
    }

    public App init() {
        if (this.inited) {
            return this;
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(BuildVars.BASE_URL + "/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);

        this.currentUser = getSharedPreferences(getString(R.string.prefs_current_user),
                MODE_PRIVATE);
        this.inited = true;
        return this;
    }

    public boolean signedIn() {
        return this.currentUser.getString("access_token", null) != null;
    }

    public Api getApi() { return api; }

    public SharedPreferences getCurrentUser() { return currentUser; }

    // Prevent navigation bar, status bar and toolbar from flashing
    // See http://stackoverflow.com/questions/26600263
    public void excludeFromTransition(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition fade = new Fade();
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);
            fade.excludeTarget(R.id.toolbar_wrapper, true);
            activity.getWindow().setExitTransition(fade);
            activity.getWindow().setEnterTransition(fade);
        }
    }
}
