package me.alisherafat.hooshang.app.models;

import java.io.Serializable;

public class GameModel implements Serializable {
    public static final int TYPE_GUES3X3 = 1;
    public static final int TYPE_PICTURE = 2;


    public int gameType;
    public String room;
    public String jsonData;
    public Player[] players;
/*
    public boolean starter() {
        return players[0].name.equals(userPrefs.getUsername());
    }
*/
}
