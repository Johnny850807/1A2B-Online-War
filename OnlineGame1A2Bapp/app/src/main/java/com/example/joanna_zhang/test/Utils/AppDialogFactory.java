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

    public static AlertDialog timeExpiredDialog(Activity activity, String message){
        return templateBuilder(activity)
                .setTitle(R.string.expiredDetection)
                .setMessage(message)
                .setPositiveButton(R.string.confirm, (d,p)->activity.finish())
                .create();
    }

    public static AlertDialog errorDialog(Context context, String errorMessage){
        return templateBuilder(context)
                .setTitle(R.string.errorMessage)
                .setMessage(errorMessage)
                .setPositiveButton(R.string.confirm, null)
                .show();
    }

    public static AlertDialog internetErrorDialog(Context context){
        return errorDialog(context, context.getString(R.string.internetError));
    }
}
