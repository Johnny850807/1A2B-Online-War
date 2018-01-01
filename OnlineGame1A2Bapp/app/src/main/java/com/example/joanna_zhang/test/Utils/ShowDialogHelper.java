package com.example.joanna_zhang.test.Utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

//todo delete this and make our applogodialogfactory more powerful
public class ShowDialogHelper {

    public static AlertDialog showComeBackActivityDialog(int icon, int title, int message, int confirm, int cancel, Activity activity, DialogInterface.OnClickListener onClickListener){
        return new AlertDialog.Builder(activity)
                .setIcon(icon)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(confirm, onClickListener)
                .setNegativeButton(cancel, null)
                .show();
    }

}
