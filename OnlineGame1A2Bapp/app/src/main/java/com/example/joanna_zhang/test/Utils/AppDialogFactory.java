package com.example.joanna_zhang.test.Utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import com.example.joanna_zhang.test.R;

public class AppDialogFactory {

    public static AlertDialog.Builder templateBuilder(Context context){
        return new AlertDialog.Builder(context)
                .setIcon(R.drawable.logo)
                .setTitle(R.string.app_name);
    }

    public static AlertDialog roomTimeExpiredDialog(Activity activity){
        return templateBuilder(activity)
                .setTitle(R.string.expiredDetection)
                .setMessage(R.string.roomClosedForExpired)
                .setPositiveButton(R.string.confirm, (d,p)->activity.finish())
                .create();
    }
}
