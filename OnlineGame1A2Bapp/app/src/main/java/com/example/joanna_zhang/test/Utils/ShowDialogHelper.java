package com.example.joanna_zhang.test.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class ShowDialogHelper {

    public static Dialog showComeBackActivityDialog(String title, String message, String confirm, String cancel, Activity activity, DialogInterface.OnClickListener onClickListener){
        return new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(confirm, onClickListener)
                .setNegativeButton(cancel,null)
                .show();
    }

}
