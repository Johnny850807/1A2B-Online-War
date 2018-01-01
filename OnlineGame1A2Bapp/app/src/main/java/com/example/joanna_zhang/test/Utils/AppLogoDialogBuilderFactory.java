package com.example.joanna_zhang.test.Utils;


import android.app.AlertDialog;
import android.content.Context;

import com.example.joanna_zhang.test.R;

public class AppLogoDialogBuilderFactory {

    public static AlertDialog.Builder create(Context context){
        return new AlertDialog.Builder(context)
                .setIcon(R.drawable.logo)
                .setTitle(context.getString(R.string.app_name));
    }
}
