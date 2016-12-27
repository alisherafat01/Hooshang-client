package me.alisherafat.hooshang.games.guess;

import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import me.alisherafat.hooshang.app.models.GameModel;
import me.alisherafat.hooshang.games.GuessMove;

public class GuessGame extends GameModel {

    @Nullable
    public static JSONObject getNewMoveData(String room,GuessMove move) {

        JSONObject object = new JSONObject();
        try {
            object.put("room", room);
            object.put("number", move.number);
            return object;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



}
