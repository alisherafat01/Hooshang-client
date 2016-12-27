package me.alisherafat.hooshang.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import me.alisherafat.hooshang.R;
import me.alisherafat.hooshang.app.interfaces.Loggable;
import me.alisherafat.hooshang.app.models.GameModel;
import me.alisherafat.hooshang.app.models.Player;
import me.alisherafat.hooshang.games.GamesSocket;
import me.alisherafat.hooshang.games.IPreGame;
import me.alisherafat.hooshang.games.guess.GuessActivity;
import me.alisherafat.hooshang.games.guess.GuessGame;
import me.alisherafat.hooshang.games.pictures.PicturesGame;
import me.alisherafat.hooshang.games.pictures.PicturesGameActivity;

import static me.alisherafat.hooshang.app.models.GameModel.TYPE_GUES3X3;
import static me.alisherafat.hooshang.app.models.GameModel.TYPE_PICTURE;

public class MainActivity extends AppCompatActivity implements Loggable, IPreGame {


    private MaterialDialog findingDialog;
    private GamesSocket gamesSocket;
    private int currentFindingGameId;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        gamesSocket = GamesSocket.getInstance(this);
        gamesSocket.setPreGameListener(this);
        findingDialog = new MaterialDialog.Builder(this)
                .content("Finding Opponent")
                .progress(true, 0)
                .cancelable(false)
                .progressIndeterminateStyle(true)
                .build();

    }


    @OnClick({R.id.btnGuess3x3, R.id.btnJoin})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGuess3x3:
                findOpponent(1);
                break;
            case R.id.btnJoin:
                findOpponent(2);
                break;
        }
    }

    private void findOpponent(int game) {
        gamesSocket.findOpponent(game);
        currentFindingGameId = game;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findingDialog.show();
                handler.postDelayed(runnable, 8000);
            }
        });
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cancelFindingOpponent();
                }
            });
        }
    };

    private void cancelFindingOpponent() {
        findingDialog.dismiss();
        gamesSocket.cancelFindingOpponent(currentFindingGameId);
    }

    @Override
    public void log(String m) {
        Log.d("app", m);
    }


    @Override
    public void onStartGame(final JSONObject object) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findingDialog.dismiss();
                try {
                    JSONArray array = object.getJSONArray("players");
                    List<Player> players = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        Player player = new Player();
                        player.name = obj.getString("name");
                        players.add(player);
                    }


                    GameModel game = null;
                    Intent intent = null;

                    switch (object.getInt("game")) {
                        case TYPE_GUES3X3:
                            intent = new Intent(MainActivity.this, GuessActivity.class);
                            game = new GuessGame();
                            break;
                        case TYPE_PICTURE:
                            intent = new Intent(MainActivity.this, PicturesGameActivity.class);
                            game = new PicturesGame();
                            break;
                    }

                    if (intent == null) {
                        return;
                    }
                    game.room = object.getString("room");
                    game.gameType = object.getInt("game");
                    game.players = new Player[players.size()];
                    players.toArray(game.players);

                    game.jsonData = object.getJSONObject("data").toString();
                    intent.putExtra("game", game);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
