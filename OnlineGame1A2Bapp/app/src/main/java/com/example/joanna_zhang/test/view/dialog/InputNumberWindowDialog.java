package com.example.joanna_zhang.test.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.joanna_zhang.test.R;


public class InputNumberWindowDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView titleTxt;
    private Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button0, cancelBtn, confirmBtn;
    private EditText answerEd;
    private OnClickListener OnClickListener;

    public InputNumberWindowDialog(Context context) {
        super(context);
        this.context = context;
        setUpView();
    }

    public void setUpView() {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.input_number_window, null);
        this.setContentView(dialogView);
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
        titleTxt = view.findViewById(R.id.titleTxt);
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

    @Override
    public void show() {
        super.show();
        answerEd.setText("");
    }

    public void update(String guessNumber) {
        OnClickListener.onEnterClick(guessNumber);
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
                String answer = answerEd.getText().toString();
                if (answer.length() == 4) {
                    update(answer);
                    this.dismiss();
                }
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
        private InputNumberWindowDialog inputNumberWindowDialog;

        public Builder(Activity activity) {
            inputNumberWindowDialog = new InputNumberWindowDialog(activity);
        }

        public Builder setOnEnterClickListener(OnClickListener onClickListener) {
            inputNumberWindowDialog.OnClickListener = onClickListener;
            return this;
        }

        public Builder setTitle(String title) {
            inputNumberWindowDialog.titleTxt.setText(title);
            return this;
        }

        public Builder setCanceledOnTouchOutside(Boolean b) {
            inputNumberWindowDialog.setCanceledOnTouchOutside(b);
            return this;
        }

        public Builder setCancelable(Boolean b) {
            inputNumberWindowDialog.setCancelable(b);
            return this;
        }

        public InputNumberWindowDialog build() {
            return inputNumberWindowDialog;
        }

        public InputNumberWindowDialog show() {
            inputNumberWindowDialog.show();
            return inputNumberWindowDialog;
        }

    }

    public interface OnClickListener {
        void onEnterClick(String guessNumber);
    }

}