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

    protected final int maxExpressionLength = 20;
    protected TextView resultTextView;
    protected String enteredExpression = "";
    protected String lastNumber = "";

    protected final List<Button> numberButtons = new ArrayList<>();
    protected final List<Button> simpleOperationButtons = new ArrayList<>();

    protected boolean CCE_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        this.resultTextView = findViewById(R.id.calc_expression);
        this.initButtons();
        this.assignSimpleListenersToViews();
    }

    protected void initButtons() {
        this.numberButtons.add(findViewById(R.id.calc_0));
        this.numberButtons.add(findViewById(R.id.calc_1));
        this.numberButtons.add(findViewById(R.id.calc_2));
        this.numberButtons.add(findViewById(R.id.calc_3));
        this.numberButtons.add(findViewById(R.id.calc_4));
        this.numberButtons.add(findViewById(R.id.calc_5));
        this.numberButtons.add(findViewById(R.id.calc_6));
        this.numberButtons.add(findViewById(R.id.calc_7));
        this.numberButtons.add(findViewById(R.id.calc_8));
        this.numberButtons.add(findViewById(R.id.calc_9));

        this.simpleOperationButtons.add(findViewById(R.id.calc_plus));
        this.simpleOperationButtons.add(findViewById(R.id.calc_minus));
        this.simpleOperationButtons.add(findViewById(R.id.calc_mult));
        this.simpleOperationButtons.add(findViewById(R.id.calc_div));
        this.simpleOperationButtons.add(findViewById(R.id.calc_equals));
        this.simpleOperationButtons.add(findViewById(R.id.calc_dot));
        this.simpleOperationButtons.add(findViewById(R.id.calc_cce));
        this.simpleOperationButtons.add(findViewById(R.id.calc_ac));
        this.simpleOperationButtons.add(findViewById(R.id.calc_negate));
        this.simpleOperationButtons.add(findViewById(R.id.calc_bksp));
    }

    protected void assignSimpleListenersToViews() {
        this.numberButtons.forEach(numberBtn -> {
            numberBtn.setOnClickListener(e -> {
                if(this.enteredExpression.length() + this.lastNumber.length() + numberBtn.getText().toString().length() > this.maxExpressionLength) {
                    Toast.makeText(this, "Entered expression is too long!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!this.lastNumber.equals("-") && this.lastNumber.length() > 0 && !(this.lastNumber.charAt(0) >= '0' && this.lastNumber.charAt(0) <= '9') || (this.lastNumber.length() > 1 && this.lastNumber.startsWith("-") && !(this.lastNumber.charAt(1) >= '0' && this.lastNumber.charAt(1) <= '9'))) {
                    this.enteredExpression += this.lastNumber;
                    this.lastNumber = "";
                }
                this.CCE_flag = false;
                this.lastNumber += numberBtn.getText().toString();
                this.redrawExpression();
            });
        });

        this.simpleOperationButtons.forEach(enteredOp -> {
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
                        if(this.enteredExpression.length() + this.lastNumber.length() > this.maxExpressionLength) {
                            Toast.makeText(this, "Entered expression is too long!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        this.lastNumber += ".";
                        redrawExpression();
                        break;
                    //works fine for +/-/+/*
                    default:
                        if(this.enteredExpression.length() + this.lastNumber.length() > this.maxExpressionLength) {
                            Toast.makeText(this, "Entered expression is too long!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        this.enteredExpression = this.enteredExpression + this.lastNumber + enteredOp.getText().toString();
                        this.lastNumber = "";
                        this.redrawExpression();
                }
            });
        });
    }

    protected void deleteCharacter() {
        if(this.lastNumber.length() > 0) {

            this.lastNumber = this.lastNumber.substring(0, this.lastNumber.length() - 1);
        } else {
            this.enteredExpression = this.enteredExpression.substring(0, this.enteredExpression.length() - 1);
        }

        this.redrawExpression();
    }

    protected void clearEnter() {
        if(this.CCE_flag || this.lastNumber.equals("")) {
            this.CCE_flag = false;
            this.allClear();
            return;
        }

        this.lastNumber = "";
        this.CCE_flag = true;

        this.redrawExpression();
    }

    protected void allClear() {
        this.CCE_flag = false;
        this.enteredExpression = "";
        this.lastNumber = "";
        this.resultTextView.setText("");
    }

    protected void equal() {
        try{
            String enteredExp = this.enteredExpression + this.lastNumber;
            if(enteredExp.contains("sqrt(-") || enteredExp.contains("log10(-") || enteredExp.contains("log(-")) {
                Toast.makeText(this, "Math expression built incorrectly", Toast.LENGTH_LONG).show();
                return;
            }
            double result = new ExpressionBuilder(this.enteredExpression + this.lastNumber)
                    .build()
                    .evaluate();
            //rounding to 4 decimal places
            this.enteredExpression = String.valueOf(Math.round(result * 10000.0) / 10000.0);
            this.resultTextView.setText(this.enteredExpression);
            this.lastNumber = this.enteredExpression;
            this.enteredExpression = "";
        } catch(ArithmeticException | IllegalArgumentException e) {
            Toast.makeText(this, "Math expression built incorrectly", Toast.LENGTH_LONG).show();
        }
    }

    protected void negateNumber() {
        if(this.lastNumber.equals("") && this.enteredExpression.length() > 0 && (this.enteredExpression.charAt(this.enteredExpression.length() - 1) >= '0' && this.enteredExpression.charAt(this.enteredExpression.length() - 1) <= '9')) {
            if(this.enteredExpression.startsWith("-")) {
                this.enteredExpression = this.enteredExpression.substring(1);
            }
            else {
                if(this.enteredExpression.length() + this.lastNumber.length() + "-".length() > this.maxExpressionLength) {
                    Toast.makeText(this, "Entered expression is too long!", Toast.LENGTH_LONG).show();
                    return;
                }
                this.enteredExpression = "-" + this.enteredExpression;
            }
        }
        else if(this.lastNumber.startsWith("-")) {
            this.lastNumber = this.lastNumber.substring(1);
        }
        else {
            if(this.enteredExpression.length() + this.lastNumber.length() + "-".length() > this.maxExpressionLength) {
                Toast.makeText(this, "Entered expression is too long!", Toast.LENGTH_LONG).show();
                return;
            }
            this.lastNumber = "-" + this.lastNumber;
        }
        this.redrawExpression();
    }

    protected void redrawExpression() {
        if(this.enteredExpression.length() + this.lastNumber.length() <= maxExpressionLength) {
            String textToShow = this.enteredExpression + this.lastNumber;
            this.resultTextView.setText(textToShow);
        } else {
            Toast.makeText(this, "Entered expression is too long!", Toast.LENGTH_LONG).show();
        }
    }

    protected void changeActivity(Class<?> className) {
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
