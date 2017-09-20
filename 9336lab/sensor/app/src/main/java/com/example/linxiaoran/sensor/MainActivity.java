package com.example.linxiaoran.sensor;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button button1;
    private Button button2;
    private Button button3;
    private ListView listView;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findview();
        onclicklistener();
    }

    private void findview(){
        button1 = (Button)findViewById(R.id.but1);
        button2 = (Button)findViewById(R.id.but2);
        button3 = (Button)findViewById(R.id.but3);
        listView = (ListView)findViewById(R.id.listView);
        textView = (TextView)findViewById(R.id.text1);
    }

    private void onclicklistener(){
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                task1();
            }
        });
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent to_task2 = new Intent();
                to_task2.setClass(MainActivity.this, task2.class);
                startActivity(to_task2);
            }
        });
        button3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent to_task3 = new Intent();
                to_task3.setClass(MainActivity.this, task3.class);
                startActivity(to_task3);
            }
        });
    }

    private void task1(){
        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        List<Sensor> sensor_list = sensorManager.getSensorList(Sensor.TYPE_ALL);
        ArrayList<String> show_list = new ArrayList<>();
        for (int i=0; i<sensor_list.size(); ++i){
            show_list.add((i+1)+".Name: "+ sensor_list.get(i).getName()+"\n"+"  Vendor: "+sensor_list.get(i).getVendor()+"\n"+"  Version"+sensor_list.get(i).getVersion()+"\n"+"  MaximumRange: "+sensor_list.get(i).getMaximumRange()+"\n"+"  Mindelay: "+sensor_list.get(i).getMinDelay());
        }
        textView.setText("Avaliable Sensors in This Phone:");
        listView.setAdapter(new ArrayAdapter<>(this,R.layout.list_view,show_list));
    }
}
