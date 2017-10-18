package com.example.joanna_zhang.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputNumberWindowView {

    public InputNumberWindowView(Context context){};
    private EditText answerEd;


    public void findAllViewById(View view){
        //View view = LayoutInflater.from(context).inflate(R.layout.input_number_window, null);
        Button button1 = (Button) view.findViewById(R.id.button1);
        Button button2 = (Button) view.findViewById(R.id.button2);
        Button button3 = (Button) view.findViewById(R.id.button3);
        Button button4 = (Button) view.findViewById(R.id.button4);
        Button button5 = (Button) view.findViewById(R.id.button5);
        Button button6 = (Button) view.findViewById(R.id.button6);
        Button button7 = (Button) view.findViewById(R.id.button7);
        Button button8 = (Button) view.findViewById(R.id.button8);
        Button button9 = (Button) view.findViewById(R.id.button9);
        Button cancelBtn = (Button) view.findViewById(R.id.cancelBtn);
        Button button0 = (Button) view.findViewById(R.id.button0);
        Button confirmBtn = (Button) view.findViewById(R.id.confirmBtn);
        EditText answer = (EditText) view.findViewById(R.id.inputNumberEd);
    }



    public String getAnswer(){
        return answerEd.getText().toString();
    }

    private static class Builder{
        private InputNumberWindowView inputNumberWindowView;

        public Builder (Context context){

        }

        public Builder setNumberColor(int id){
            return this;
        }

        public Builder setBackgroundColor(int id){
            return this;
        }

        public InputNumberWindowView build(){
            return inputNumberWindowView;
        }

    }

    public interface onClickListener{
        void onClick();
    }

}
