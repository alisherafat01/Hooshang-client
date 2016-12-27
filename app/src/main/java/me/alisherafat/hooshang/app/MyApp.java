package me.alisherafat.hooshang.app;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.net.URI;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import me.alisherafat.hooshang.app.endpoints.EndPoints;
import me.alisherafat.hooshang.app.enums.PlayerStatus;
import me.alisherafat.hooshang.app.preferences.UserPrefs;

public class MyApp extends Application {

    public static PlayerStatus PlayerStatus;
    private RefWatcher refWatcher;
    private Socket mSocket;
    private UserPrefs userPrefs;
    public static Manager socketManager;
    private static MyApp instance;

    {
        /*
        IO.Options opts = new IO.Options();
        opts.query = "user=";
        try {
            //mSocket = IO.socket(EndPoints.SERVER_ADDRESS,opts);
            Manager manager = new Manager(new URI(EndPoints.SERVER_ADDRESS), opts);
            mSocket = manager.socket("/games/guess3x3");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

         */
    }

    public RefWatcher getRefWatcher() {
        return refWatcher;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        instance = this;
        userPrefs = UserPrefs.getInstance(this);
        //refWatcher = LeakCanary.install(this);

        if (!userPrefs.hasUsername()) {
            userPrefs.setRandomUsername();
        }

        IO.Options opts = new IO.Options();
        opts.query = "user=" + userPrefs.getUsername();
        try {
            socketManager = new Manager(new URI(EndPoints.SERVER_ADDRESS), opts);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
