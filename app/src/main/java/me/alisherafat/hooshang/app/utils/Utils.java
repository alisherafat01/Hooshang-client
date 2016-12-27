package me.alisherafat.hooshang.app.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.security.SecureRandom;
import java.util.Calendar;

public class Utils {
    private Context context;
    private Toast toaster;

    private Utils(Context context) {
        this.context = context;
    }

    private static WeakReference<Utils> myWeakInstance;

    public static Utils getInstance(Context context) {
        if (myWeakInstance == null || myWeakInstance.get() == null) {
            myWeakInstance = new WeakReference<>(new Utils(context));
        }
        return myWeakInstance.get();
    }

    public void toast(String message) {
        if (toaster == null) {
            toaster = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toaster.setText(message);
        }
        toaster.show();
    }

    public void log(String log) {
        Log.d("app", log);
    }

    public String generateRandomString(int length) {
        String included = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(included.charAt(rnd.nextInt(included.length())));
        return sb.toString();
    }

    public String getTimestampedName() {
        Calendar calendar = Calendar.getInstance();
        String name = calendar.get(Calendar.YEAR) + "_" +
                calendar.get(Calendar.MONTH) + "_" +
                calendar.get(Calendar.DAY_OF_MONTH) + "_" + generateRandomString(4);
        return name;
    }
}
