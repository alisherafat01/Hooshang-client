package me.alisherafat.hooshang.games;

import org.json.JSONObject;

public interface GameInteractions {
    void onOpponentResigned();

    void onNewMove(JSONObject object);

    void onOpponentWon();
}
