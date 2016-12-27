package me.alisherafat.hooshang.games;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import me.alisherafat.hooshang.app.MyApp;
import me.alisherafat.hooshang.app.preferences.UserPrefs;

import static me.alisherafat.hooshang.app.enums.PlayerStatus.FINDING_OPPONENT;
import static me.alisherafat.hooshang.app.enums.PlayerStatus.PLAYING;

public class GamesSocket {
    private Socket socket;
    private GameInteractions listener;
    private IPreGame preGameListener;
    private static GamesSocket instance;

    private Context context;
    private UserPrefs userPrefs;

    private GamesSocket(Context context) {
        this.context = context;
        userPrefs = UserPrefs.getInstance(context);
        init();
    }

    public static GamesSocket getInstance(Context context) {
        if (instance == null) {
            instance = new GamesSocket(context);
        }
        return instance;
    }

    private GamesSocket init() {
        socket = MyApp.socketManager.socket("/games");
        socket.on(Socket.EVENT_CONNECT, onConnect);
        socket.on("start-game", onStartGame);
        socket.on("new-move", onNewMove);
        socket.on("win", onWin);
        socket.on("player-resigned", onResign);
        socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.connect();
        return this;
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("app", "connected to games");
        }
    };

    public void newMove(JSONObject object) {
        socket.emit("new-move", object);
    }

    public void resign(String room) {
        JSONObject object = new JSONObject();
        try {
            object.put("room", room);
            socket.emit("resign", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void win(String room) {
        JSONObject object = new JSONObject();
        try {
            object.put("room", room);
            socket.emit("win", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private Emitter.Listener onNewMove = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject object = (JSONObject) args[0];
            listener.onNewMove(object);
        }
    };

    private Emitter.Listener onResign = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            listener.onOpponentResigned();
        }
    };

    private Emitter.Listener onWin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            listener.onOpponentWon();
        }
    };


    public void findOpponent(int game) {
        MyApp.PlayerStatus = FINDING_OPPONENT;
        JSONObject object = new JSONObject();
        try {
            object.put("name", userPrefs.getUsername());
            object.put("game", game);
            socket.emit("find-opponent", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void cancelFindingOpponent(int gameId) {
        if (MyApp.PlayerStatus == FINDING_OPPONENT) {
            JSONObject object = new JSONObject();
            try {
                object.put("game", gameId);
                socket.emit("find-opponent-cancelled", object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setListener(GameInteractions listener) {
        this.listener = listener;
    }

    public void setPreGameListener(IPreGame preGameListener) {
        this.preGameListener = preGameListener;
    }


    private Emitter.Listener onStartGame = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject object = (JSONObject) args[0];
            MyApp.PlayerStatus = PLAYING;
            preGameListener.onStartGame(object);
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("app", "disconnected from games");
        }
    };

}
