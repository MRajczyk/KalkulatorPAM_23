package com.example.kalkulator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button button_simple;
    private Button button_advanced;
    private Button button_about;
    private Button button_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_simple = findViewById(R.id.button1);
        button_simple.setOnClickListener(e -> button_simple.setText("testowo!"));
        //onSaveInstanceState
        //w onCreate mozna sprawdzic czy to wyzej nie jest nullem i wtedy tam lezy jakies info
        //trzeba dalej sprawdzac
        // jest jeszcze onRestoreInstanceState poprzez get<T> i podac klucz czy nie zwroci nulla

        //jak uruchomic inna aktywnosc z menu:
        //klasa Intent <wiadomosc(mozna podac w sposob jawny)>
        // w onClick tworze obiekt intencja i klasa z ktorej zostanie uruchomiona nowa aktywnosc
        // i klase nowej aktywnosci
        //metoda startActivityByIntent
    }
}