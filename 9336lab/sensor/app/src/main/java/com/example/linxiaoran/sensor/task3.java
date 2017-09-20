package com.example.linxiaoran.sensor;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;


public class task3 extends Activity implements SensorEventListener{
    private TextView text3;
    @SuppressWarnings("FieldCanBeLocal")
    private SensorManager sensorManager;
    private Sensor acc_sensor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task3);
        text3=(TextView) findViewById(R.id.text3);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        acc_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,acc_sensor, 10000);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int x = (int)sensorEvent.values[0];
        int y = (int)sensorEvent.values[1];
        int z = (int)sensorEvent.values[2];
        String flag = null;
        if(x>8 && flag!="Left"){
            flag = "Left";
            text3.setText("Left");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if(x<-8 && flag!="Right"){
            flag = "Right";
            text3.setText("Right");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        }
        if(y<-8 && flag!="Up Side Down"){
            flag = "Up Side Down";
            text3.setText("Up Side Down");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        }
        if(y>8 && flag!="Default"){
            flag = "Default";
            text3.setText("Default");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if(z>8 && flag!="On the table"){
            flag = "On the table";
            text3.setText("On the table");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}