package com.example.a9336_assignment;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by linxiaoran on 2016/10/19.
 */

public class Task1 extends AppCompatActivity {
    Button low_freq;
    Button stop;
    TextView display;
    private boolean low_freq_flag = true;
    private boolean start = false;
    private boolean is_processing = false;
    int sampleRate = 44100;
    private int blockSize = 2048;
    Recorder recorder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task1);
        low_freq = (Button)findViewById(R.id.task1_low_freq_but);
        stop = (Button)findViewById(R.id.stop_task1);
        display = (TextView)findViewById(R.id.display_task1);

        low_freq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(start){
                    start = false;
                }
                start = true;
                recorder = new Recorder();
                recorder.execute();

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start = false;
                recorder.cancel(true);
            }
        });
    }

    public double[] Goertzel(double freq, double[] data) {
        int k;
        double omega;
        double coeff;
        double floatN = (double)blockSize;
        double MagnitudeSquared;
        double maxFreq = 0;
        double maxEnerg = 0;

        double Q0, Q1, Q2;


        for(double targetFre = freq-50; targetFre < freq+50; targetFre+=10) {
            k = (int) (0.5 + ((floatN * targetFre) / (double)sampleRate));
            omega = (2.0 * Math.PI * k) / floatN;
            coeff = 2.0 * Math.cos(omega);
            Q1 = 0;
            Q2 = 0;
            for(int i=0; i<blockSize; i++) {
                Q0 = coeff * Q1 - Q2 + data[i];
                Q2 = Q1;
                Q1 = Q0;
            }
            MagnitudeSquared = Q1 * Q1 + Q2 * Q2 - Q1 * Q2 * coeff;
            if(MagnitudeSquared>maxEnerg) {
                maxEnerg = MagnitudeSquared;
                maxFreq = targetFre;
            }
        }

        double[] result = {maxFreq,maxEnerg};
        return result;

    }


    public class Recorder extends AsyncTask<Void, double[], Void>{

        @Override
        protected Void doInBackground(Void... arg0) {
            int bufferSize = AudioRecord.getMinBufferSize(sampleRate,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            AudioRecord audioRecord = new AudioRecord(
                    MediaRecorder.AudioSource.MIC, sampleRate,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
            short[] buffer = new short[blockSize];
            double[] toTransform = new double[blockSize];
            while(start){
                audioRecord.startRecording();
                int buffer_reading = audioRecord.read(buffer,0,blockSize);
                for (int i = 0; i < blockSize && i < buffer_reading; i++) {
                    toTransform[i] = (double) buffer[i] / 32767.0;
                }
                if(!is_processing){publishProgress(toTransform);}
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(double[]... toTransform){
            is_processing = true;
            double maxFreq = 0;
            double maxEnergy = 0;
            double enough_to_show = 100;
            for(int i = 100; i<22000;i+=100){
                double[] result = Goertzel(i, toTransform[0]);
                if(result[1]>maxEnergy){
                    maxEnergy = result[1];
                    maxFreq = result[0];
                }
                if(maxEnergy>enough_to_show){
                    display.setText("Current Frequency Recorded: "+maxFreq);
                }else{
                    display.setText("No Significant Frequency Recorded......");
                }
            }
            is_processing = false;
        }
    }
}
