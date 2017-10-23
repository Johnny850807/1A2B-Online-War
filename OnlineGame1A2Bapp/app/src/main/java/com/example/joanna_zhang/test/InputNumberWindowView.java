package com.example.joanna_zhang.test;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class InputNumberWindowView extends Dialog implements View.OnClickListener {
    private Context context;
    private Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button0, cancelBtn, confirmBtn;
    private EditText answerEd;
    private Dialog dialogBuilder;
    private List<OnClickListener> onClickListeners = new ArrayList<>();

    public InputNumberWindowView(Context context) {
        super(context);
        this.context = context;
        setUpView();
    }

    public void setUpView() {
        dialogBuilder = new Dialog(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.input_number_window, null);
        dialogBuilder.setContentView(dialogView);
        dialogBuilder.show();
        findAllViewById(dialogView);
        whenButtonOnClick();
    }

    public void findAllViewById(View view) {
        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);
        button3 = view.findViewById(R.id.button3);
        button4 = view.findViewById(R.id.button4);
        button5 = view.findViewById(R.id.button5);
        button6 = view.findViewById(R.id.button6);
        button7 = view.findViewById(R.id.button7);
        button8 = view.findViewById(R.id.button8);
        button9 = view.findViewById(R.id.button9);
        cancelBtn = view.findViewById(R.id.cancelBtn);
        button0 = view.findViewById(R.id.button0);
        confirmBtn = view.findViewById(R.id.confirmBtn);
        answerEd = view.findViewById(R.id.inputNumberEd);
    }

    public void whenButtonOnClick() {
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button0.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
    }

    public void update(String guessNumber) {
        for (OnClickListener onClickListener : onClickListeners)
            onClickListener.onEnterClick(guessNumber);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelBtn:
                String str = answerEd.getText().toString();
                if (str.length() > 0)
                    str = str.substring(0, str.length() - 1);
                answerEd.setText(str);
                break;
            case R.id.confirmBtn:
                update(answerEd.getText().toString());
                dialogBuilder.dismiss();
                break;
            default:
                Button button = (Button) v;
                display(button.getText().toString());
                break;
        }
    }

    private void display(String number) {
        String str = answerEd.getText().toString();
        if (!str.contains(number))
            answerEd.setText(str + number);
    }

    public static class Builder {
        private InputNumberWindowView inputNumberWindowView;

        public Builder(Activity activity) {
            inputNumberWindowView = new InputNumberWindowView(activity);
        }

        public Builder addOnEnterClickListener(OnClickListener onClickListener) {
            inputNumberWindowView.onClickListeners.add(onClickListener);
            return this;
        }

        public InputNumberWindowView build() {
            return inputNumberWindowView;
        }

    }

    public interface OnClickListener {
        void onEnterClick(String guessName);
    }

}
