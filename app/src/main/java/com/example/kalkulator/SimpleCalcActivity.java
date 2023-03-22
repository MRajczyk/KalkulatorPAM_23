package com.example.kalkulator;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;
import java.util.List;

public class SimpleCalcActivity extends AppCompatActivity {

    private TextView resultTextView;
    protected String enteredExpression = "";

    private final List<Button> numberButtons = new ArrayList<>();
    private final List<Button> simpleOperationButtons = new ArrayList<>();

    private boolean CCE_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        this.resultTextView = findViewById(R.id.simple_expression);
        this.initButtons();
        assignListenersToViews();
    }

    private void initButtons() {
        this.numberButtons.add(findViewById(R.id.simple_0));
        this.numberButtons.add(findViewById(R.id.simple_1));
        this.numberButtons.add(findViewById(R.id.simple_2));
        this.numberButtons.add(findViewById(R.id.simple_3));
        this.numberButtons.add(findViewById(R.id.simple_4));
        this.numberButtons.add(findViewById(R.id.simple_5));
        this.numberButtons.add(findViewById(R.id.simple_6));
        this.numberButtons.add(findViewById(R.id.simple_7));
        this.numberButtons.add(findViewById(R.id.simple_8));
        this.numberButtons.add(findViewById(R.id.simple_9));

        this.simpleOperationButtons.add(findViewById(R.id.simple_plus));
        this.simpleOperationButtons.add(findViewById(R.id.simple_minus));
        this.simpleOperationButtons.add(findViewById(R.id.simple_mult));
        this.simpleOperationButtons.add(findViewById(R.id.simple_div));
        this.simpleOperationButtons.add(findViewById(R.id.simple_equals));
        this.simpleOperationButtons.add(findViewById(R.id.simple_dot));
        this.simpleOperationButtons.add(findViewById(R.id.simple_cce));
        this.simpleOperationButtons.add(findViewById(R.id.simple_ac));
        this.simpleOperationButtons.add(findViewById(R.id.simple_negate));
    }

    private void assignListenersToViews() {
        this.numberButtons.forEach(numberBtn -> {
            numberBtn.setOnClickListener(e -> {
                this.appendToEnteredExpression(numberBtn.getText().toString());
            });
        });

        this.simpleOperationButtons.forEach(operation -> {
            operation.setOnClickListener(e -> {
                switch(operation.getText().toString()) {
                    case "C/CE":
                        this.clearEnter();
                        break;
                    case "AC":
                        this.allClear();
                        break;
                    case "=":
                        this.equal();
                        break;
                    case "+/-":
                        this.negateNumber();
                        break;
                    //works fine for +/-/+/*/.
                    default:
                        this.appendToEnteredExpression(operation.getText().toString());
                        break;
                }
            });
        });
    }

    private void clearEnter() {

    }

    private void allClear() {
        this.enteredExpression = "";
        this.resultTextView.setText("0");
    }

    private void equal() {
        try{
            double result = new ExpressionBuilder(this.enteredExpression)
                    .build()
                    .evaluate();
            this.enteredExpression = String.valueOf(Math.round(result * 10000.0) / 10000.0);
            this.resultTextView.setText(this.enteredExpression);
        } catch(ArithmeticException | IllegalArgumentException e) {
            Toast.makeText(this, "Math expression built incorrectly", Toast.LENGTH_LONG).show();
        }
    }

    private void negateNumber() {

    }

    private void appendToEnteredExpression(String operation){
        if(this.enteredExpression.length() <= 15) {
            //TODO: maybe use StringBuilder?
            this.enteredExpression += operation;
            this.resultTextView.setText(this.enteredExpression);
        } else {
            Toast.makeText(this, "Entered expression is too long!", Toast.LENGTH_LONG).show();
        }
    }

    private void changeActivity(Class<?> className) {
        Intent intent = new Intent(this, className);
        intent.setFlags(FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("ENTERED_EXPRESSION", this.enteredExpression);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        this.enteredExpression = savedInstanceState.getString("ENTERED_EXPRESSION");
        super.onRestoreInstanceState(savedInstanceState);
    }
}
