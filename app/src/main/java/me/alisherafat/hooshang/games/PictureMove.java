package me.alisherafat.hooshang.games;

import org.json.JSONException;
import org.json.JSONObject;

public class PictureMove extends GameMove {
    public int index;
    public boolean righChoose;

    public static PictureMove from(JSONObject object) {
        PictureMove move = new PictureMove();
        try {
            move.index = object.getInt("index");
            move.righChoose = object.getBoolean("right");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return move;
    }

}
