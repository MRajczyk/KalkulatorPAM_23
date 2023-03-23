package com.example.kalkulator;

import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class AdvancedCalcActivity extends SimpleCalcActivity {

    protected final List<Button> advancedOperationButtons = new ArrayList<>();

    String lastParenthese = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced);

        this.resultTextView = findViewById(R.id.calc_expression);
        this.initButtons();
        this.assignSimpleListenersToViews();

        this.initAdvancedButtons();
        this.assignAdvancedListenersToViews();
    }

    protected void initAdvancedButtons() {
        this.advancedOperationButtons.add(findViewById(R.id.advanced_sin));
        this.advancedOperationButtons.add(findViewById(R.id.advanced_cos));
        this.advancedOperationButtons.add(findViewById(R.id.advanced_tan));
        this.advancedOperationButtons.add(findViewById(R.id.advanced_ln));
        this.advancedOperationButtons.add(findViewById(R.id.advanced_log));
        this.advancedOperationButtons.add(findViewById(R.id.advanced_percent));
        this.advancedOperationButtons.add(findViewById(R.id.advanced_parentheses));
        this.advancedOperationButtons.add(findViewById(R.id.advanced_sqrt));
        this.advancedOperationButtons.add(findViewById(R.id.advanced_square));
        this.advancedOperationButtons.add(findViewById(R.id.advanced_pow));
    }

    @Override
    protected void allClear() {
        this.CCE_flag = false;
        this.enteredExpression = "";
        this.lastNumber = "";
        this.lastParenthese = "";
        this.resultTextView.setText("");
    }

    protected void assignAdvancedListenersToViews() {
        this.advancedOperationButtons.forEach(enteredOp -> {
            enteredOp.setOnClickListener(e -> {
                switch(enteredOp.getText().toString()) {
                    case "SIN":
                        this.enteredExpression = this.enteredExpression + this.lastNumber;
                        this.lastNumber = "sin(";
                        this.lastParenthese += "(";
                        break;
                    case "COS":
                        this.enteredExpression = this.enteredExpression + this.lastNumber;
                        this.lastNumber = "cos(";
                        this.lastParenthese += "(";
                        break;
                    case "TAN":
                        this.enteredExpression = this.enteredExpression + this.lastNumber;
                        this.lastNumber = "tan(";
                        this.lastParenthese += "(";
                        break;
                    case "LN":
                        this.enteredExpression = this.enteredExpression + this.lastNumber;
                        this.lastNumber = "log(";
                        this.lastParenthese += "(";
                        break;
                    case "LOG":
                        this.enteredExpression = this.enteredExpression + this.lastNumber;
                        this.lastNumber = "log10(";
                        this.lastParenthese += "(";
                        break;
                    case "()":
                        if(lastParenthese.endsWith("(")) {
                            this.enteredExpression = this.enteredExpression + this.lastNumber + ")";
                            this.lastParenthese = this.lastParenthese.substring(0, this.lastParenthese.length() - 1);
                        }
                        else {
                            this.enteredExpression += this.lastNumber;
                            this.enteredExpression += "(";
                            this.lastParenthese += "(";
                        }
                        this.lastNumber = "";
                        break;
                    case "X^2":
                        this.enteredExpression = this.enteredExpression + this.lastNumber + "^";
                        this.lastNumber = "2";
                        break;
                    case "SQRT":
                        this.enteredExpression = this.enteredExpression + this.lastNumber;
                        this.lastNumber = "sqrt(";
                        this.lastParenthese += "(";
                        break;
                    case "%":
                        this.enteredExpression = this.enteredExpression + this.lastNumber + "%";
                        this.lastNumber = "";
                        break;
                    case "X^Y":
                        this.enteredExpression = this.enteredExpression + this.lastNumber;
                        this.lastNumber = "^";
                        break;
                }
                this.redrawExpression();
            });
        });
    }
}