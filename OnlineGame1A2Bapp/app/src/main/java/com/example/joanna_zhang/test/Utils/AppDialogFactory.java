package com.example.joanna_zhang.test.Utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.joanna_zhang.test.R;

import gamecore.entity.Player;

import static com.example.joanna_zhang.test.R.string.confirm;

public class AppDialogFactory {

    /**
     * @return the template alert dialog's builder with the default app icon with the app name as the title
     */
    public static AlertDialog.Builder templateBuilder(Context context){
        return new AlertDialog.Builder(context)
                .setIcon(R.drawable.logo)
                .setTitle(R.string.app_name);
    }

    public static AlertDialog.Builder expiredDialogTemplateBuilder(Activity activity){
        return templateBuilder(activity)
                .setTitle(R.string.expiredDetection)
                .setCancelable(false)
                .setPositiveButton(confirm, (d, p)->activity.finish());
    }

    public static AlertDialog roomTimeExpiredDialog(Activity activity){
        return expiredDialogTemplateBuilder(activity)
                .setMessage(activity.getString(R.string.roomClosedForExpired))
                .create();
    }

    public static AlertDialog playerTimeExpiredDialog(Activity activity){
        return expiredDialogTemplateBuilder(activity)
                .setMessage(activity.getString(R.string.playerTimeExpired))
                .create();
    }

    /**
     * @return the template alert dialog for showing any error message with the specified action while the positive button is on clicked.
     */
    public static AlertDialog errorDialog(Context context, String errorMessage, DialogInterface.OnClickListener onClickListener){
        return templateBuilder(context)
                .setTitle(R.string.errorMessage)
                .setMessage(errorMessage)
                .setCancelable(false)
                .setPositiveButton(confirm, onClickListener)
                .create();
    }

    /**
     * @return the template alert dialog for showing any error message.
     */
    public static AlertDialog errorDialog(Context context, String errorMessage){
        return errorDialog(context, errorMessage, null);
    }

    public static AlertDialog internetConnectionErrorDialog(Context context){
        return errorDialog(context, context.getString(R.string.internetError));
    }

    /**
     * @return the template alert dialog for notifying that the game will be closed because any player left.
     */
    public static AlertDialog playerLeftFromGameDialog(Activity activity, Player leftPlayer){
            return templateBuilder(activity)
                .setTitle(R.string.gameClosed)
                .setMessage(activity.getString(R.string.playerIsAlreadyLeft, leftPlayer.getName()))
                .setPositiveButton(confirm, (d,i) -> activity.finish())
                .create();
    }

    /**
     * @return the template alert dialog for notifying that the game is waiting for other players entering. This dialog should be dismissed manually when the game started.
     */
    public static AlertDialog createWaitingForPlayersEnteringDialog(Context context){
        LinearLayout viewGroup = new LinearLayout(context);
        viewGroup.setOrientation(LinearLayout.HORIZONTAL);
        viewGroup.setPadding(10, 10, 10, 10);

        LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        txtParams.setMarginEnd(20);
        TextView msgTxt = new TextView(context);
        msgTxt.setText(context.getString(R.string.waitingForPlayersEntering));
        msgTxt.setLayoutParams(txtParams);

        ProgressBar progressBar = new ProgressBar(context);

        viewGroup.addView(msgTxt);
        viewGroup.addView(progressBar);

        return templateBuilder(context)
                .setTitle(R.string.pleaseWait)
                .setCancelable(false)
                .setView(viewGroup)
                .create();
    }

}
