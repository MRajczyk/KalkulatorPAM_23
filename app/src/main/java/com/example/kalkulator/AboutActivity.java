package com.example.kalkulator;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

    }

    private void changeActivity(Class<?> className) {
        Intent intent = new Intent(this, className);
        intent.setFlags(FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}
