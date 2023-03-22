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
    private String enteredExpression = "";
    private String lastNumber = "";

    private final List<Button> numberButtons = new ArrayList<>();
    private final List<Button> operationButtons = new ArrayList<>();

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

        this.operationButtons.add(findViewById(R.id.simple_plus));
        this.operationButtons.add(findViewById(R.id.simple_minus));
        this.operationButtons.add(findViewById(R.id.simple_mult));
        this.operationButtons.add(findViewById(R.id.simple_div));
        this.operationButtons.add(findViewById(R.id.simple_equals));
        this.operationButtons.add(findViewById(R.id.simple_dot));
        this.operationButtons.add(findViewById(R.id.simple_cce));
        this.operationButtons.add(findViewById(R.id.simple_ac));
        this.operationButtons.add(findViewById(R.id.simple_negate));
        this.operationButtons.add(findViewById(R.id.simple_bksp));
    }

    private void assignListenersToViews() {
        this.numberButtons.forEach(numberBtn -> {
            numberBtn.setOnClickListener(e -> {
                this.lastNumber += numberBtn.getText().toString();
                this.redrawExpression();
            });
        });

        this.operationButtons.forEach(enteredOp -> {
            enteredOp.setOnClickListener(e -> {
                switch(enteredOp.getText().toString()) {
                    case "C/CE":
                        this.clearEnter();
                        break;
                    case "AC":
                        this.allClear();
                        break;
                    case "=":
                        this.CCE_flag = false;
                        this.equal();
                        break;
                    case "+/-":
                        this.CCE_flag = false;
                        this.negateNumber();
                        break;
                    case "DEL":
                        this.CCE_flag = false;
                        this.deleteCharacter();
                        break;
                    case ".":
                        this.lastNumber += ".";
                        redrawExpression();
                        break;
                    //works fine for +/-/+/*
                    default:
                        this.enteredExpression = this.enteredExpression + this.lastNumber + enteredOp.getText().toString();
                        this.lastNumber = "";
                        this.redrawExpression();
                }
            });
        });
    }

    private void deleteCharacter() {
        if(this.lastNumber.length() > 0) {
            this.lastNumber = this.lastNumber.substring(0, this.lastNumber.length() - 1);
        } else {
            this.enteredExpression = this.enteredExpression.substring(0, this.enteredExpression.length() - 1);
        }

        this.redrawExpression();
    }

    private void clearEnter() {
        if(this.CCE_flag || this.lastNumber.equals("")) {
            this.allClear();
            return;
        }

        this.lastNumber = "";
        this.CCE_flag = true;

        this.redrawExpression();
    }

    private void allClear() {
        this.CCE_flag = false;
        this.enteredExpression = "";
        this.lastNumber = "";
        this.resultTextView.setText("0");
    }

    private void equal() {
        try{
            double result = new ExpressionBuilder(this.enteredExpression + this.lastNumber)
                    .build()
                    .evaluate();
            //rounding to 4 decimal places
            this.enteredExpression = String.valueOf(Math.round(result * 10000.0) / 10000.0);
            this.resultTextView.setText(this.enteredExpression);
            this.lastNumber = "";
        } catch(ArithmeticException | IllegalArgumentException e) {
            Toast.makeText(this, "Math expression built incorrectly", Toast.LENGTH_LONG).show();
            this.redrawExpression();
        }
    }

    private void negateNumber() {
        if(this.lastNumber.equals("") && this.enteredExpression.length() > 0 && (this.enteredExpression.charAt(this.enteredExpression.length() - 1) >= '0' && this.enteredExpression.charAt(this.enteredExpression.length() - 1) <= '9')) {
            if(this.enteredExpression.startsWith("-")) {
                this.enteredExpression = this.enteredExpression.substring(1);
            }
            else {
                this.enteredExpression = "-" + this.enteredExpression;
            }
        }
        else if(this.lastNumber.startsWith("-")) {
            this.lastNumber = this.lastNumber.substring(1);
        }
        else {
            this.lastNumber = "-" + this.lastNumber;
        }
        this.redrawExpression();
    }

    private void redrawExpression() {
        if(this.enteredExpression.length() <= 15) {
            String textToShow = this.enteredExpression + this.lastNumber;
            this.resultTextView.setText(textToShow);
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
        outState.putString("LAST_NUMBER", this.lastNumber);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        this.enteredExpression = savedInstanceState.getString("ENTERED_EXPRESSION");
        this.lastNumber = savedInstanceState.getString("LAST_NUMBER");
        super.onRestoreInstanceState(savedInstanceState);
        this.redrawExpression();
    }
}
