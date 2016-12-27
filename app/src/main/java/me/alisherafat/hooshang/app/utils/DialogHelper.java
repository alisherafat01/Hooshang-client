package me.alisherafat.hooshang.app.utils;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

public class DialogHelper {



    public static void showSimpleDialog(Context context, String content) {
        new MaterialDialog.Builder(context)
                .content(content)
                .positiveText(android.R.string.ok)
                .show();
    }

    public static void showConnectionFailed(Context context) {
        new MaterialDialog.Builder(context)
//                .iconRes()
                .limitIconToDefaultSize()
                .title("Connection Failed!")
                .content(" Check your internet connection and try again... ")
                .positiveText("ok")
                .show();
    }




    public static MaterialDialog showIndeterminateProgressDialog(Context context, String title, boolean isHorizontal) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(title)
                .content("Please wait...")
                .progress(true, 0)
                .progressIndeterminateStyle(isHorizontal).build();
        return dialog;
    }

}
