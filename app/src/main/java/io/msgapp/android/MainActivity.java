package io.msgapp.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.msgapp.android.model.User;
import io.socket.emitter.Emitter;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static io.msgapp.android.BuildVars.LOG_TAG;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.app = ((App) getApplication()).init();

        if (!this.app.signedIn()) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        this.handleIntent();

        SocketManager.getInstance(app).connect();
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        Log.d(LOG_TAG, "MainActivity onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "MainActivity onStart");
        this.handleIntent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void handleIntent() {
        Intent intent = this.getIntent();

        if (intent.getAction().equals(Intent.ACTION_VIEW)) {
            Uri uri = intent.getData();

            if (Objects.equals(uri.getHost(), "msgapp.me")) {
                String username = uri.getPathSegments().get(0);

                app.getApi().getUserByUsername("Bearer " + app.getCurrentUser().getString("access_token", ""), username)
                        .enqueue(new Callback<List<User>>() {
                            @Override
                            public void onResponse(Response<List<User>> response, Retrofit retrofit) {
                                if (response.isSuccess()) {
                                    if (response.body().size() != 0) {


                                        User user = response.body().get(0);

                                        SocketManager.getInstance(app).
                                                addListener(SocketManager.EVENT_SERVER_CONVERSATION_CREATED,
                                                        onConversationCreated);
                                        SocketManager.getInstance(app)
                                                .send(SocketManager.EVENT_CONVERSATION_CREATE,
                                                        "{\"userId\": " + user.id + "}");
                                    } else {
                                        app.somethingWentWrong(); // TODO user does not exist
                                    }
                                } else {
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

                            }
                        });
            }
        }
    }

    private final Emitter.Listener onConversationCreated = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(LOG_TAG, "conversation created");
            Log.d(LOG_TAG, args[0].toString());

        }
    };

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//
//        return true;
//    }
//
//    private void test() {
//        try {
//            JSONObject obj = new JSONObject();
//            obj.put("to", "2");
//            obj.put("text", "^~^");
//            obj.put("type", "text");
//            String now = String.valueOf(System.currentTimeMillis());
//            String hash = app.getCurrentUser().getString("access_token", "");
//            hash += now + "^~^";
//            Log.d(LOG_TAG, hash);
//            hash = Util.sha1(hash);
//            Log.d(LOG_TAG, hash);
//            obj.put("hash", hash);
//            obj.put("when", now);
//            SocketManager.getInstance(app).send("message", obj);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}
