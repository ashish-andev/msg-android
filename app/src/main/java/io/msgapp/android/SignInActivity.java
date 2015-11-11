package io.msgapp.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.Bind;

public class SignInActivity extends AppCompatActivity {
    private static final int SIGN_UP_CODE = 666;

    @Bind(R.id.et_username_sign_in)
    protected EditText eTUsername;

    @Bind(R.id.et_password_sign_in)
    protected EditText eTPassword;

    @Bind(R.id.progressBar_sign_in)
    protected ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(myToolbar);
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
                    this.signIn(data.getStringExtra("username"), data.getStringExtra("password"));
                }
        }
    }

    private void signIn(String username, String password) {
        // TODO
    }
}
