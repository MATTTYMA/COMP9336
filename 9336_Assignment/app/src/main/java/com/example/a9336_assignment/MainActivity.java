package com.example.a9336_assignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button transmitter;
    Button task1;
    Button task2;
    Button task3;
    Button task4;
    Button data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        transmitter = (Button)findViewById(R.id.transmiter);
        task1 = (Button)findViewById(R.id.task1);
        task2 = (Button)findViewById(R.id.task2);
        task3 = (Button)findViewById(R.id.task3);
        task4 = (Button)findViewById(R.id.task4);
        data = (Button)findViewById(R.id.data_tran);

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent button_intent = new Intent();
                button_intent.setClass(MainActivity.this, Data_transmitter.class);
                startActivity(button_intent);
            }
        });

        transmitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent button_intent = new Intent();
                button_intent.setClass(MainActivity.this, Transmitter.class);
                startActivity(button_intent);
            }
        });

        task1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_transmitter = new Intent();
                to_transmitter.setClass(MainActivity.this, Task1.class);
                startActivity(to_transmitter);
            }
        });

        task2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_transmitter = new Intent();
                to_transmitter.setClass(MainActivity.this, Task2.class);
                startActivity(to_transmitter);
            }
        });

        task3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_transmitter = new Intent();
                to_transmitter.setClass(MainActivity.this, Task3.class);
                startActivity(to_transmitter);
            }
        });

        task4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_transmitter = new Intent();
                to_transmitter.setClass(MainActivity.this, Task4.class);
                startActivity(to_transmitter);
            }
        });

    }
}
