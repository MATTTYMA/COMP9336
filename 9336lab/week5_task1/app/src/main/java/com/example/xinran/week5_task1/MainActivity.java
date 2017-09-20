package com.example.xinran.week5_task1;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
    TextView textView1, textView2,textView3, textView4,textView5;
    float GYR1, GYR2, GYR3, MAG1, MAG2, MAG3;
    double globalAngle;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private float timestamp;
    private float angle[] = new float[3];
    private boolean flag = false;
    private double angle_rotation;
    private float[] accelerometerValues = new float[3];
    private float[] magneticFieldValues = new float[3];
    private static final String TAG = "---MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button myButton1=(Button)findViewById(R.id.button1);
        Button myButton2=(Button)findViewById(R.id.button2);
        textView1 = (TextView) this.findViewById(R.id.textView);
        textView2 = (TextView) this.findViewById(R.id.textView2);
        textView3 = (TextView) this.findViewById(R.id.textView3);
        textView4 = (TextView) this.findViewById(R.id.textView4);
        textView5 = (TextView) this.findViewById(R.id.textView5);


        myButton1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                myClick(v); /* my method to call new intent or activity */
                flag = true;
            }

        } );

        myButton2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                textView1.setText("stoped now"); /* my method to call new intent or activity */
                flag = false;
            }

        } );
    }

    public void myClick(View v){
        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        Sensor sensor1 = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor sensor2 = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensor1, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensor2, SensorManager.SENSOR_DELAY_NORMAL);

        //globalAngle = ORI;

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_GYROSCOPE){

            // assign directions

            textView1.setText("x: " + event.values[0] + "\n" +
                    "y: " + event.values[1] + "\n" +
                    "z: " + event.values[2] + "\n");

            if (timestamp != 0) {
                final float dT = (event.timestamp - timestamp) * NS2S;
                angle[2] += event.values[2] * dT;
                angle_rotation = (double) Math.toDegrees(angle[2]);
                if (flag) {
                    textView2.setText("Rotation: " + angle_rotation);
                    textView2.setVisibility(View.VISIBLE);
                } else {
                    angle[2] = 0;
                    textView2.setText("Rotation: " + 0);
                    textView2.setVisibility(View.VISIBLE);
                    //Rotation.setVisibility(View.INVISIBLE);
                }
            }
            timestamp = event.timestamp;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerValues = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticFieldValues = event.values;
        }

        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            MAG1 = event.values[0];
            MAG2 = event.values[1];
            MAG3 = event.values[2];

            float[] valuesa = new float[3];
            float[] R = new float[9];
            SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
            SensorManager.getOrientation(R, valuesa);
            valuesa[0] = (float) Math.toDegrees(valuesa[0]);

            Log.i(TAG, valuesa[0] + "");
            textView3.setText("Aheading: " + valuesa[0]);
            textView4.setText("Round value: " + Math.rint(valuesa[0]));
            textView5.setText("the angle between ahead and truth north: " +(valuesa[0] - MAG1));


        }
    }
}
