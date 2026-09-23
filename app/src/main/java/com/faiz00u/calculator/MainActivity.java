package com.faiz00u.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView display;
    private double stored = 0;
    private String operator = "";
    private boolean waiting = false;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
        display = findViewById(R.id.display);
        View.OnClickListener listener = v -> {
            Button b = (Button)v;
            String value = b.getText().toString();
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
    private void digit(String d){ if(waiting){display.setText(d);waiting=false;} else if(display.getText().toString().equals("0"))display.setText(d); else display.append(d); }
    private void decimal(){ if(waiting){display.setText("0.");waiting=false;} else if(!display.getText().toString().contains("."))display.append("."); }
    private void operator(String op){ stored=Double.parseDouble(display.getText().toString()); operator=op; waiting=true; }
    private void equals(){ if(operator.isEmpty())return; double b=Double.parseDouble(display.getText().toString()); double r; switch(operator){case "+":r=stored+b;break;case "−":r=stored-b;break;case "×":r=stored*b;break;case "÷":if(b==0){display.setText("Error");operator="";return;}r=stored/b;break;default:r=b;} display.setText(format(r)); operator="";waiting=true; }
    private void percent(){double n=Double.parseDouble(display.getText().toString())/100;display.setText(format(n));}
    private void clear(){display.setText("0");stored=0;operator="";waiting=false;}
    private void backspace(){String s=display.getText().toString();if(s.length()>1)display.setText(s.substring(0,s.length()-1));else display.setText("0");}
    private String format(double n){if(n==Math.rint(n))return String.valueOf((long)n);return String.valueOf(n);}
}
