package me.alisherafat.hooshang.app.preferences;

import android.content.Context;

import java.io.Serializable;
import java.lang.ref.WeakReference;

import me.alisherafat.hooshang.R;

public class UserPrefs implements Serializable{
    private Context context;
    private TinyDB tinyDB;

    private UserPrefs(Context context) {
        this.context = context;
        tinyDB = TinyDB.getInstance(context);
    }

    private static WeakReference<UserPrefs> instance;

    public static UserPrefs getInstance(Context context) {
        if (instance == null || instance.get() == null) {
            instance = new WeakReference<>(new UserPrefs(context));
        }
        return instance.get();
    }

    public void setUsername(String username) {
        tinyDB.putString(context.getString(R.string.pref_username), username);
    }

    public String getUsername() {
        return tinyDB.getString(context.getString(R.string.pref_username));
    }

    public void setRandomUsername() {
        setUsername("user " + (int) (Math.random() * 20));
    }

    public boolean hasUsername() {
        if (getUsername().equals("")) {
            return false;
        }
        return true;
    }

}
