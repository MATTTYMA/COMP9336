package com.example.linxiaoran.sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;


public class task2 extends Activity implements SensorEventListener{
    private TextView text2;
    private SensorManager sensorManager;
    private Sensor acc_sensor;
    private float x;
    private float y;
    private float z;
    private float[] gravity = new float[3];
    private float [] filterG = new float[3];
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task2);
        text2 = (TextView)findViewById(R.id.text2);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        acc_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,acc_sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        x = sensorEvent.values[0];
        y = sensorEvent.values[1];
        z = sensorEvent.values[2];
        float x_g = (float) (x/9.9);
        float y_g = (float) (y/9.9);
        float z_g = (float) (z/9.9);

        float alpha = (float) 0.8;

        gravity[0] = alpha * gravity[0] + (1-alpha) * sensorEvent.values[0];
        gravity[1] = alpha * gravity[1] + (1-alpha) * sensorEvent.values[1];
        gravity[2] = alpha * gravity[2] + (1-alpha) * sensorEvent.values[2];

        filterG[0] = (sensorEvent.values[0] - gravity[0]);
        filterG[1] = (sensorEvent.values[1] - gravity[1]);
        filterG[2] = (sensorEvent.values[2] - gravity[2]);

        text2.setText("Acceleration with Gravity:\nx: "+x_g+"G\ny: "+y_g+"G\nz: "+z_g+"G\n");
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
