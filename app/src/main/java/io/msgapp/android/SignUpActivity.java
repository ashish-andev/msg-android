package io.msgapp.android;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static io.msgapp.android.BuildVars.LOG_TAG;

public class SignUpActivity extends AppCompatActivity {

    @Bind(R.id.et_name_wrapper)
    protected TextInputLayout eTNameWrapper;

    @Bind(R.id.et_username_wrapper)
    protected TextInputLayout eTUsernameWrapper;

    @Bind(R.id.et_email_wrapper)
    protected TextInputLayout eTEmailWrapper;

    @Bind(R.id.et_password_wrapper)
    protected TextInputLayout eTPasswordWrapper;

    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(myToolbar);


        // noinspection ConstantConditions
        eTUsernameWrapper.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // noinspection ConstantConditions
                    String username = eTUsernameWrapper.getEditText().getText().toString();
                    Log.d(LOG_TAG, username);
                    Call<Boolean> call = app.api.usernameIsAvailable(username);
                    call.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Response<Boolean> response, Retrofit retrofit) {
                            if (response.isSuccess()) {
                                if (response.body()) {
                                    eTUsernameWrapper.getEditText().setCompoundDrawablesWithIntrinsicBounds(0, 0,
                                            R.drawable.ic_done_green, 0);
                                } else {
                                    eTUsernameWrapper.getEditText().setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                    eTUsernameWrapper.setError(getString(R.string.username_taken));
                                }
                            } else {
                                // TODO: error reporting
                                try {
                                    Log.d(LOG_TAG, response.errorBody().string());
                                    app.somethingWentWrong();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    app.somethingWentWrong();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            // TODO: error reporting
                            t.printStackTrace();
                            app.somethingWentWrong();
                        }
                    });
                }
            }
        });

        this.app = (App) getApplication();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sign_up, menu);

        return true;
    }
}
