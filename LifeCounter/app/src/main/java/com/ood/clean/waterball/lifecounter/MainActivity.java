package com.ood.clean.waterball.lifecounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private final int BASE_AGE = 9;
    private String[] ages = new String[100];
    private Spinner nowAgeSpn;
    private Spinner targetAgeSpn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nowAgeSpn = (Spinner) findViewById(R.id.nowAgeSpn);
        targetAgeSpn = (Spinner) findViewById(R.id.targetAgeSpn);

        initAges();
        setupSpinners();
    }

    private void setupSpinners(){
        nowAgeSpn.setAdapter(createArrayAdapter(getString(R.string.nowAge), 10, 50));
        targetAgeSpn.setAdapter(createArrayAdapter(getString(R.string.targetAge), 20, 80));

        nowAgeSpn.setOnItemSelectedListener(this);
        targetAgeSpn.setOnItemSelectedListener(this);
    }

    private ArrayAdapter<String> createArrayAdapter(String firstOption, int nowAge, int targetAge){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.white_text_item, getAgeOptions(firstOption, nowAge, targetAge));
        adapter.setDropDownViewResource(R.layout.black_text_item);
        return adapter;
    }

    private String[] getAgeOptions(String firstOption, int fromAge, int toAge){
        String[] options = Arrays.copyOfRange(ages, fromAge - BASE_AGE, toAge - BASE_AGE);
        options[0] = firstOption;
        return options;
    }

    private void initAges(){
        for (int i = 0 ; i < ages.length ; i ++)
            ages[i] = String.valueOf(BASE_AGE + i);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (nowAgeSpn.getSelectedItemPosition() != 0 && targetAgeSpn.getSelectedItemPosition() != 0)
            saveInfoAndGoNextPage();
    }

    private void saveInfoAndGoNextPage(){
        Intent intent = new Intent(this, CountActivity.class);
        intent.putExtra("nowAge", nowAgeSpn.getSelectedItem().toString());
        intent.putExtra("targetAge", targetAgeSpn.getSelectedItem().toString());
        startActivity(intent);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}
