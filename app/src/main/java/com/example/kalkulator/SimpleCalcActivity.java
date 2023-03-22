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
    private String firstNumber = "";
    private String secondNumber = "";
    private String operation = "";

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
        this.simpleOperationButtons.add(findViewById(R.id.simple_bksp));
    }

    private void assignListenersToViews() {
        this.numberButtons.forEach(numberBtn -> {
            numberBtn.setOnClickListener(e -> {
                this.CCE_flag = false;
                if(this.operation.equals("")) {
                    this.firstNumber += numberBtn.getText().toString();
                    this.redrawExpression();
                } else {
                    this.secondNumber += numberBtn.getText().toString();
                    this.redrawExpression();
                }
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
                    case "BKSP":
                        this.CCE_flag = false;
                        this.deleteCharacter();
                        break;
                    case ".":
                        this.CCE_flag = false;
                        if((this.operation.equals(""))) {
                            if(this.firstNumber.equals("")) {
                                this.firstNumber += "0";
                            }
                            this.firstNumber += ".";
                        }
                        else if((this.secondNumber.equals(""))) {
                            this.secondNumber += "0" + ".";
                        }
                        else {
                            this.secondNumber += ".";
                        }
                        this.redrawExpression();
                        break;
                    //works fine for +/-/+/*/.
                    default:
                        this.CCE_flag = false;
                        if(this.firstNumber.equals("")) {
                            this.firstNumber = "0";
                            redrawExpression();
                            break;
                        }
                        if(!(this.operation.equals("")) && !(this.secondNumber.equals(""))) {
                            this.equal();
                            this.operation = enteredOp.getText().toString();
                            this.redrawExpression();
                            return;
                        }
                        this.operation = enteredOp.getText().toString();
                        this.redrawExpression();
                        break;
                }
            });
        });
    }

    private void deleteCharacter() {
        if(this.secondNumber.length() > 0) {
            this.secondNumber = this.secondNumber.substring(0, this.secondNumber.length() - 1);
        } else if(this.operation.length() > 0) {
            this.operation = "";
        } else {
            if(this.firstNumber.length() > 0) {
                this.firstNumber = this.firstNumber.substring(0, this.firstNumber.length() - 1);
            }
        }
        this.redrawExpression();
    }

    private void clearEnter() {
        if(this.CCE_flag || this.firstNumber.equals("")) {
            this.allClear();
            return;
        }

        if(!(this.secondNumber.equals(""))) {
            this.secondNumber = "";
            this.CCE_flag = true;
        } else {
            this.operation = "";
            this.firstNumber = "";
        }

        this.redrawExpression();
    }

    private void allClear() {
        this.CCE_flag = false;
        this.firstNumber = "";
        this.secondNumber = "";
        this.operation = "";
        this.enteredExpression = "";
        this.resultTextView.setText("0");
    }

    private void equal() {
        try{
            double result = new ExpressionBuilder(this.enteredExpression)
                    .build()
                    .evaluate();
            //rounding to 4 decimal places
            this.enteredExpression = String.valueOf(Math.round(result * 10000.0) / 10000.0);
            this.resultTextView.setText(this.enteredExpression);
            this.firstNumber = this.enteredExpression.equals("-") ? "-0" : this.enteredExpression;
            this.operation = "";
            this.secondNumber = "";
        } catch(ArithmeticException | IllegalArgumentException e) {
            Toast.makeText(this, "Math expression built incorrectly", Toast.LENGTH_LONG).show();
            this.redrawExpression();
        }
    }

    private void negateNumber() {
        if(this.secondNumber.equals("")) {
            if(this.firstNumber.startsWith("-")) {
                this.firstNumber = this.firstNumber.substring(1);
            } else if(this.firstNumber.equals("")) {
                this.firstNumber = "-";
            }
            else {
                this.firstNumber = "-" + this.firstNumber;
            }
        } else {
            if(this.secondNumber.startsWith("-")) {
                this.secondNumber = this.secondNumber.substring(1);
            } else {
                this.secondNumber = "-" + this.secondNumber;
            }
        }
        this.redrawExpression();
    }

    private void redrawExpression() {
        if(this.firstNumber.equals("-")) {
            this.enteredExpression = "-0" + this.operation + this.secondNumber;
        } else if(this.firstNumber.equals("")) {
            this.enteredExpression = 0 + this.operation + this.secondNumber;
        } else {
            this.enteredExpression = this.firstNumber + this.operation + this.secondNumber;
        }
        this.resultTextView.setText(this.enteredExpression);
        if(!(this.enteredExpression.length() <= 15)) {
            //TODO: maybe use StringBuilder?
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
