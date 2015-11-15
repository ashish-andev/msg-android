package io.msgapp.android;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static io.msgapp.android.BuildVars.LOG_TAG;

/**
 * Created by matheus on 11/11/15.
 */
@SuppressWarnings("FieldCanBeLocal")
public class SocketManager {
    private static SocketManager instance;
    private App app;
    private Socket socket;

    public final static String EVENT_AUTHENTICATED = "server.authentication.success";

    private SocketManager(App app) {
        this.app = app;
        try {
            IO.Options options = new IO.Options();
            options.reconnectionAttempts = 0;
            socket = IO.socket(BuildVars.BASE_URL, options);
            socket.on(Socket.EVENT_CONNECT, onConnect);
            socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            socket.on(Socket.EVENT_RECONNECT_ATTEMPT, onReconnectAttempt);
            socket.on(Socket.EVENT_RECONNECT, onReconnect);
            socket.on(EVENT_AUTHENTICATED, onAuthenticated);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static SocketManager getInstance(App app) {
        if (instance == null) {
            instance = new SocketManager(app);
        }

        return instance;
    }

    public SocketManager connect() {
        Log.d(LOG_TAG, "SocketManager#connect");
        this.socket.connect();

        return this;
    }

    public void send(String event, JSONObject data) {
        this.socket.emit(event, data);
    }

    public void send(String event, String data) {
        this.socket.emit(event, data);
    }

    public void addListener(String event, Emitter.Listener listener) {
        this.socket.on(event, listener);

    }

    private final Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(LOG_TAG, "socket event: connected");
            JSONObject obj = new JSONObject();
            try {
                obj.put("token", app.getCurrentUser().getString("access_token", null));
            } catch (JSONException e) {
                // TODO
            }
            socket.emit("authentication", obj);
            Log.d(LOG_TAG, "authentication emitted");
        }
    };

    private final Emitter.Listener onReconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(LOG_TAG, "socket event: reconnect");
            // TODO
        }
    };

    private final Emitter.Listener onAuthenticated = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(LOG_TAG, "socket event: authenticated");
            // TODO: ask the server what was lost
        }
    };

    private final Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(LOG_TAG, "socket event: disconnect");
        }
    };

    private Emitter.Listener onReconnectAttempt = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(LOG_TAG, "socket event: reconnect attempt");
        }
    };
}
