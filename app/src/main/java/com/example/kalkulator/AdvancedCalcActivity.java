package com.example.kalkulator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class AdvancedCalcActivity extends SimpleCalcActivity {

    protected final List<Button> advancedOperationButtons = new ArrayList<>();

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
        this.resultTextView.setText("");
    }

    protected void assignAdvancedListenersToViews() {
        this.advancedOperationButtons.forEach(enteredOp -> {
            enteredOp.setOnClickListener(e -> {
                if(this.enteredExpression.length() + this.lastNumber.length() + enteredOp.getText().toString().length() > this.maxExpressionLength) {
                    Toast.makeText(this, "Entered expression is too long!", Toast.LENGTH_LONG).show();
                    return;
                }
                switch(enteredOp.getText().toString()) {
                    case "SIN":
                        this.enteredExpression = this.enteredExpression + this.lastNumber;
                        this.lastNumber = "sin(";
                        break;
                    case "COS":
                        this.enteredExpression = this.enteredExpression + this.lastNumber;
                        this.lastNumber = "cos(";
                        break;
                    case "TAN":
                        this.enteredExpression = this.enteredExpression + this.lastNumber;
                        this.lastNumber = "tan(";
                        break;
                    case "LN":
                        this.enteredExpression = this.enteredExpression + this.lastNumber;
                        this.lastNumber = "log(";
                        break;
                    case "LOG":
                        this.enteredExpression = this.enteredExpression + this.lastNumber;
                        this.lastNumber = "log10(";
                        break;
                    case "()":
                        if((this.enteredExpression + this.lastNumber).chars().filter(x -> x == '(').count() > (this.enteredExpression + this.lastNumber).chars().filter(x -> x == ')').count()) {
                            this.enteredExpression = this.enteredExpression + this.lastNumber + ")";
                        } else {
                            this.enteredExpression += this.lastNumber;
                            this.enteredExpression += "(";
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