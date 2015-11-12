package io.msgapp.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.msgapp.android.model.User;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static io.msgapp.android.BuildVars.LOG_TAG;

public class SignUpActivity extends AppCompatActivity {

    @Bind(R.id.et_name_wrapper_sign_up)
    protected TextInputLayout eTNameWrapper;
    private EditText eTName;

    @Bind(R.id.et_username_wrapper_sign_up)
    protected TextInputLayout eTUsernameWrapper;
    private EditText eTUsername;

    @Bind(R.id.et_email_wrapper_sign_up)
    protected TextInputLayout eTEmailWrapper;
    private EditText eTEmail;

    @Bind(R.id.et_password_wrapper_sign_up)
    protected TextInputLayout eTPasswordWrapper;
    private EditText eTPassword;

    @Bind(R.id.progressBar_sign_up)
    protected ProgressBar progressBar;

    private App app;

    private final long DELAY_VERIFY = 1000; // Delay for verify user input

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.actionBar);
        this.app          = ((App) getApplication()).init();
        eTName            = eTNameWrapper.getEditText();
        eTUsername        = eTUsernameWrapper.getEditText();
        eTEmail           = eTEmailWrapper.getEditText();
        eTPassword        = eTPasswordWrapper.getEditText();

        setSupportActionBar(myToolbar);

        eTName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            private Timer timer = new Timer();
            private String value;

            @Override
            public void afterTextChanged(Editable s) {
                timer.cancel();
                value = s.toString().trim();
                timer = new Timer();
                app.showTextInputLayoutError(eTNameWrapper, 0, 0);

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (value.isEmpty()) {
                                    app.showTextInputLayoutError(eTNameWrapper,
                                            R.string.name_cannot_be_blank, R.drawable.ic_error_red);
                                } else {
                                    app.showTextInputLayoutError(eTNameWrapper, 0,
                                            R.drawable.ic_done_green);
                                }
                            }
                        });
                    }
                }, DELAY_VERIFY);
            }
        });

        eTUsername.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            private Timer timer = new Timer();
            private String value;

            @Override
            public void afterTextChanged(Editable s) {
                timer.cancel();
                value = s.toString().trim();
                timer = new Timer();
                app.showTextInputLayoutError(eTUsernameWrapper, 0, 0);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (value.isEmpty()) {
                                    app.showTextInputLayoutError(eTUsernameWrapper,
                                            R.string.username_cannot_be_blank, R.drawable.ic_error_red);
                                } else {
                                    if (value.length() < 3) {
                                        app.showTextInputLayoutError(eTUsernameWrapper,
                                                R.string.username_min_length, R.drawable.ic_error_red);
                                        return;
                                    }
                                    Call<Boolean> call = app.getApi().usernameIsAvailable(value);
                                    call.enqueue(new Callback<Boolean>() {
                                        @Override
                                        public void onResponse(Response<Boolean> response, Retrofit retrofit) {
                                            if (response.isSuccess()) {
                                                if (response.body()) {
                                                    app.showTextInputLayoutError(eTUsernameWrapper,
                                                            0, R.drawable.ic_done_green);
                                                } else {
                                                    app.showTextInputLayoutError(eTUsernameWrapper,
                                                            R.string.username_taken, R.drawable.ic_error_red);
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
                    }
                }, DELAY_VERIFY);
            }
        });

        eTEmail.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            private Timer timer = new Timer();
            private String value;

            @Override
            public void afterTextChanged(Editable s) {
                timer.cancel();
                value = s.toString().trim();
                timer = new Timer();
                app.showTextInputLayoutError(eTEmailWrapper, 0, 0);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Log.d(LOG_TAG, "check");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (value.isEmpty()) {
                                    app.showTextInputLayoutError(eTEmailWrapper,
                                            R.string.email_cannot_be_blank, R.drawable.ic_error_red);
                                } else {
                                    Call<Boolean> call = app.getApi().emailIsAvailable(value);
                                    call.enqueue(new Callback<Boolean>() {
                                        @Override
                                        public void onResponse(Response<Boolean> response, Retrofit retrofit) {
                                            if (response.isSuccess()) {
                                                if (response.body()) {
                                                    app.showTextInputLayoutError(eTEmailWrapper,
                                                            0, R.drawable.ic_done_green);
                                                } else {
                                                    app.showTextInputLayoutError(eTEmailWrapper,
                                                            R.string.email_taken, R.drawable.ic_error_red);
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
                    }
                }, DELAY_VERIFY);
            }
        });

        eTPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            private Timer timer = new Timer();
            private String value;

            @Override
            public void afterTextChanged(Editable s) {
                timer.cancel();
                value = s.toString().trim();
                timer = new Timer();
                app.showTextInputLayoutError(eTPasswordWrapper, 0, 0);

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (value.isEmpty()) {
                                    app.showTextInputLayoutError(eTPasswordWrapper,
                                            R.string.password_cannot_be_blank, R.drawable.ic_error_red);
                                } else if (value.length() < 8) {
                                    app.showTextInputLayoutError(eTPasswordWrapper, R.string.password_min_length,
                                            R.drawable.ic_error_red);
                                } else {
                                    app.showTextInputLayoutError(eTPasswordWrapper, 0,
                                            R.drawable.ic_done_green);
                                }
                            }
                        });
                    }
                }, DELAY_VERIFY);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sign_up, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionDone:
                boolean ok = true;

                if (eTNameWrapper.getError() != null) {
                    ok = false;
                } else if (app.isEmpty(eTName)) {
                    app.showTextInputLayoutError(eTNameWrapper,
                            R.string.name_cannot_be_blank, R.drawable.ic_error_red);
                    ok = false;
                }

                if (eTUsernameWrapper.getError() != null) {
                    ok = false;
                } else if(app.isEmpty(eTUsername)) {
                    app.showTextInputLayoutError(eTUsernameWrapper,
                            R.string.username_cannot_be_blank, R.drawable.ic_error_red);
                    ok = false;
                }

                if (eTEmailWrapper.getError() != null) {
                    ok = false;
                } else if(app.isEmpty(eTEmail)) {
                    app.showTextInputLayoutError(eTEmailWrapper,
                            R.string.email_cannot_be_blank, R.drawable.ic_error_red);
                    ok = false;
                }

                if (eTPasswordWrapper.getError() != null) {
                    ok = false;
                } else if(app.isEmpty(eTPassword)) {
                    app.showTextInputLayoutError(eTPasswordWrapper,
                            R.string.password_cannot_be_blank, R.drawable.ic_error_red);
                    ok = false;
                }
                if (ok) {
                    eTNameWrapper.setVisibility(View.INVISIBLE);
                    eTUsernameWrapper.setVisibility(View.INVISIBLE);
                    eTEmailWrapper.setVisibility(View.INVISIBLE);
                    eTPasswordWrapper.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);

                    final User user = new User(app.trim(eTName), app.trim(eTUsername), app.trim(eTEmail),
                            app.trim(eTPassword));

                    app.getApi().createUser(user).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Response<User> response, Retrofit retrofit) {
                            if (response.isSuccess()) {
                                Log.d(LOG_TAG, response.body().toString());
                                app.getCurrentUser().edit().putLong("id", response.body().id)
                                        .putString("username", response.body().username)
                                        .putString("email", response.body().email)
                                        .apply();
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("username", user.username);
                                resultIntent.putExtra("password", user.password);
                                setResult(RESULT_OK, resultIntent);
                                finish();
                            } else {
                                eTNameWrapper.setVisibility(View.VISIBLE);
                                eTUsernameWrapper.setVisibility(View.VISIBLE);
                                eTEmailWrapper.setVisibility(View.VISIBLE);
                                eTPasswordWrapper.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
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
                            t.printStackTrace();
                        }
                    });

                }

                return ok;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
