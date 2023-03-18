package com.example.kalkulator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SimpleCalcActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

    }

    private void changeActivity(Class<?> className) {
        Intent intent = new Intent(this, className);
        startActivity(intent);
    }
}
