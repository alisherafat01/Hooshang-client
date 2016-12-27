package me.alisherafat.hooshang.games;

import org.json.JSONObject;

import java.io.Serializable;

public class GuessMove extends GameMove implements Serializable{
    public int number;

    public static GuessMove from(JSONObject object) {
        GuessMove move = new GuessMove();
        try {
            move.number = object.getInt("number");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return move;
    }
}
