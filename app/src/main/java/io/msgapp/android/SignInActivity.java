package io.msgapp.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.okhttp.ResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SignInActivity extends AppCompatActivity {
    private static final int SIGN_UP_CODE = 666;

    @Bind(R.id.et_username_sign_in)
    protected EditText eTUsername;

    @Bind(R.id.et_password_sign_in)
    protected EditText eTPassword;

    @Bind(R.id.progressBar_sign_in)
    protected ProgressBar progressBar;

    @Bind(R.id.signInButton_sign_in)
    protected Button signInButton;

    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        this.app = ((App) getApplication()).init();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(myToolbar);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (app.isEmpty(eTUsername)) {
                    Toast.makeText(SignInActivity.this,
                            getString(R.string.username_email_or_phone_number) + " cannot be blank",
                            Toast.LENGTH_LONG).show();
                } else if (app.isEmpty(eTPassword)) {
                    Toast.makeText(SignInActivity.this,
                            getString(R.string.password_cannot_be_blank), Toast.LENGTH_LONG).show();
                } else if (app.trim(eTPassword).length() < 8) {
                    Toast.makeText(SignInActivity.this,
                            getString(R.string.password_min_length), Toast.LENGTH_LONG).show();
                } else {
                    signInButton.setEnabled(false);
                    doSignIn(app.trim(eTUsername), app.trim(eTPassword));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sign_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSignUp:
                startActivityForResult(new Intent(this, SignUpActivity.class), SIGN_UP_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SIGN_UP_CODE:
                if (resultCode == RESULT_OK) {
                    eTUsername.setVisibility(View.INVISIBLE);
                    eTUsername.setVisibility(View.INVISIBLE);
                    signInButton.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    this.doSignIn(data.getStringExtra("username"), data.getStringExtra("password"));
                }
        }
    }

    private void doSignIn(String username, String password) {
        app.getApi().getOAuthToken("password", username, password)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            try {
                                JSONObject obj = new JSONObject(response.body().string());
                                app.getCurrentUser().edit().putString("access_token",
                                        obj.getString("access_token")).apply();
                                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                finish();
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                String resp = response.errorBody().string();
                                if (resp.contains("invalid_grant")) {
                                    signInError(R.string.invalid_credentials);
                                } else {
                                    Log.d(BuildVars.LOG_TAG, resp);
                                    signInError(R.string.something_went_wrong);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                signInError(R.string.something_went_wrong);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                        signInError(R.string.something_went_wrong);
                    }
                });
    }

    private void signInError(int stringId) {
        eTUsername.setVisibility(View.VISIBLE);
        eTUsername.setVisibility(View.VISIBLE);
        signInButton.setVisibility(View.VISIBLE);
        signInButton.setEnabled(true);
        progressBar.setVisibility(View.INVISIBLE);

        Toast.makeText(this, getString(stringId), Toast.LENGTH_LONG).show();
    }
}
