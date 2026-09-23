package com.faiz00u.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView display, expression;
    private double stored = 0;
    private String operator = "";
    private boolean waiting = false;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
        display = findViewById(R.id.display);
        expression = findViewById(R.id.expression);
        View.OnClickListener listener = v -> {
            String value = ((Button) v).getText().toString();
            if (value.matches("[0-9]")) digit(value);
            else if (value.equals(".")) decimal();
            else if (value.equals("AC")) clear();
            else if (value.equals("⌫")) backspace();
            else if (value.equals("%")) percent();
            else if (value.equals("=")) equals();
            else operator(value);
        };
        int[] ids = {R.id.b0,R.id.b1,R.id.b2,R.id.b3,R.id.b4,R.id.b5,R.id.b6,R.id.b7,R.id.b8,R.id.b9,R.id.dot,R.id.ac,R.id.back,R.id.percent,R.id.div,R.id.mul,R.id.minus,R.id.plus,R.id.equals};
        for (int id : ids) findViewById(id).setOnClickListener(listener);
    }

    private void digit(String d) {
        if (waiting) { display.setText(d); waiting = false; }
        else if (display.getText().toString().equals("0")) display.setText(d);
        else display.append(d);
        updateExpression();
    }

    private void decimal() {
        if (waiting) { display.setText("0."); waiting = false; }
        else if (!display.getText().toString().contains(".")) display.append(".");
        updateExpression();
    }

    private void operator(String op) {
        if (!operator.isEmpty() && !waiting) calculatePending();
        stored = Double.parseDouble(display.getText().toString());
        operator = op;
        waiting = true;
        updateExpression();
    }

    private void equals() {
        if (operator.isEmpty()) return;
        calculatePending();
        expression.setText("");
        waiting = true;
    }

    private void calculatePending() {
        double b = Double.parseDouble(display.getText().toString());
        double r;
        switch (operator) {
            case "+": r = stored + b; break;
            case "−": r = stored - b; break;
            case "×": r = stored * b; break;
            case "÷":
                if (b == 0) { display.setText("Error"); operator = ""; return; }
                r = stored / b; break;
            default: r = b;
        }
        display.setText(format(r));
        stored = r;
    }

    private void percent() {
        double n = Double.parseDouble(display.getText().toString()) / 100;
        display.setText(format(n));
        updateExpression();
    }

    private void clear() {
        display.setText("0"); stored = 0; operator = ""; waiting = false; expression.setText("");
    }

    private void backspace() {
        String s = display.getText().toString();
        if (waiting) return;
        display.setText(s.length() > 1 ? s.substring(0, s.length() - 1) : "0");
        updateExpression();
    }

    private void updateExpression() {
        String current = display.getText().toString();
        if (!operator.isEmpty()) expression.setText(format(stored) + " " + operator + " " + (waiting ? "" : current));
        else expression.setText("");
    }

    private String format(double n) {
        if (n == Math.rint(n)) return String.valueOf((long) n);
        return String.valueOf(n);
    }
}
