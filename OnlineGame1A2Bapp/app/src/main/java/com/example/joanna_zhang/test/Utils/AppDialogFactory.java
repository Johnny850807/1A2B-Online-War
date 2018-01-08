package com.example.joanna_zhang.test.Utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.joanna_zhang.test.R;

import gamecore.entity.Player;

import static com.example.joanna_zhang.test.R.string.confirm;

public class AppDialogFactory {

    /**
     * @return the template alert dialog's builder with the default app icon with the app name as the title
     */
    public static AlertDialog.Builder templateBuilder(Context context) {
        return new AlertDialog.Builder(context)
                .setIcon(R.drawable.logo)
                .setTitle(R.string.app_name);
    }

    public static AlertDialog.Builder expiredDialogTemplateBuilder(Activity activity) {
        return templateBuilder(activity)
                .setTitle(R.string.expiredDetection)
                .setCancelable(false)
                .setPositiveButton(confirm, (d, p) -> activity.finish());
    }

    public static AlertDialog roomTimeExpiredDialog(Activity activity) {
        return expiredDialogTemplateBuilder(activity)
                .setMessage(activity.getString(R.string.roomClosedForExpired))
                .create();
    }

    public static AlertDialog playerTimeExpiredDialog(Activity activity) {
        return expiredDialogTemplateBuilder(activity)
                .setMessage(activity.getString(R.string.playerTimeExpired))
                .create();
    }


    /**
     * @return the template alert dialog for showing any error message with the specified action while the positive button is on clicked.
     */
    public static AlertDialog errorDialog(Context context, String errorMessage, DialogInterface.OnClickListener onClickListener) {
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
    public static AlertDialog errorDialog(Context context, String errorMessage) {
        return errorDialog(context, errorMessage, null);
    }

    public static AlertDialog internetConnectionErrorDialog(Context context) {
        return errorDialog(context, context.getString(R.string.internetError));
    }

    /**
     * @return the template alert dialog for notifying that the game will be closed because any player left.
     */
    public static AlertDialog playerLeftFromGameDialog(Activity activity, Player leftPlayer) {
        return templateBuilder(activity)
                .setTitle(R.string.gameClosed)
                .setMessage(activity.getString(R.string.playerIsAlreadyLeft, leftPlayer.getName()))
                .setPositiveButton(confirm, (d, i) -> activity.finish())
                .create();
    }

    /**
     * @return the template alert dialog for notifying that the game is waiting for other players entering. This dialog should be dismissed manually when the game started.
     */
    public static AlertDialog createWaitingForPlayersEnteringDialog(Context context) {
        RelativeLayout viewGroup = new RelativeLayout(context);
        viewGroup.setPadding(10, 10, 10, 10);

        RelativeLayout.LayoutParams txtParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        txtParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

        RelativeLayout.LayoutParams progressbarParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        progressbarParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);

        TextView msgTxt = new TextView(context);
        msgTxt.setText(context.getString(R.string.waitingForPlayersEntering));
        msgTxt.setLayoutParams(txtParams);

        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setLayoutParams(progressbarParams);

        viewGroup.addView(msgTxt);
        viewGroup.addView(progressBar);

        return templateBuilder(context)
                .setTitle(R.string.pleaseWait)
                .setCancelable(false)
                .setView(viewGroup)
                .create();
    }

    public static AlertDialog createGameoverResultDialogForWinner(Activity activity, String winner) {
        return templateBuilder(activity)
                .setTitle(R.string.gameOver)
                .setMessage(activity.getString(R.string.theWinnerIs, winner))
                .setPositiveButton(R.string.confirm, (dialog, which) -> activity.finish())
                .create();
    }

    public static AlertDialog simpleMessageEdittextDialog(Context context, OnMessageSendOnClickListener onclick){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        RelativeLayout viewGroup = new RelativeLayout(context);
        viewGroup.setPadding(40, 20, 40, 20);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        EditText msgEd = new EditText(context);
        msgEd.setHint(context.getString(R.string.inputChatMessage));
        msgEd.setLayoutParams(params);
        viewGroup.addView(msgEd);

        return templateBuilder(context)
                .setTitle(R.string.chat)
                .setView(viewGroup)
                .setPositiveButton(R.string.enter, (d,i)->{
                    onclick.onMessageSendOnClick(msgEd.getText().toString());
                    //hide the showing keyboard
                    imm.hideSoftInputFromWindow(msgEd.getWindowToken(), 0);
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    public interface OnMessageSendOnClickListener{
        void onMessageSendOnClick(String message);
    }
}
