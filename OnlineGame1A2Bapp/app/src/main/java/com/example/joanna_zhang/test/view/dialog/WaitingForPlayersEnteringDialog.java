package com.example.joanna_zhang.test.view.dialog;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.joanna_zhang.test.R;

public class WaitingForPlayersEnteringDialog extends AlertDialog{
    private String message;

    public WaitingForPlayersEnteringDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setIcon(R.drawable.logo);
        setTitle(R.string.pleaseWait);
        setView(createView());
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    private View createView(){
        LinearLayout viewGroup = new LinearLayout(getContext());
        viewGroup.setOrientation(LinearLayout.HORIZONTAL);
        viewGroup.setPadding(10, 10, 10, 10);

        LinearLayout.LayoutParams txtParams = createWrapContentLayoutParams();
        txtParams.setMarginEnd(20);
        TextView msgTxt = new TextView(getContext());
        msgTxt.setText(message);
        msgTxt.setLayoutParams(txtParams);

        ProgressBar progressBar = new ProgressBar(getContext());
        progressBar.setLayoutParams(createWrapContentLayoutParams());

        viewGroup.addView(msgTxt);
        viewGroup.addView(progressBar);

        return viewGroup;
    }

    private LinearLayout.LayoutParams createWrapContentLayoutParams(){
        return  new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
    }

    public void setMessage(String msg){
        this.message = msg;
    }
}
