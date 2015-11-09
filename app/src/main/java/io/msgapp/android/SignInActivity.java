package io.msgapp.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class SignInActivity extends AppCompatActivity {

    private static AppCompatActivity _this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(myToolbar);

        _this = this;

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
                startActivity(new Intent(this, SignUpActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void signUpSucceeded() {
        _this.finish();
    }
}
