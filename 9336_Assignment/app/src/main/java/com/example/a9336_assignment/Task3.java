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

public class Task3 extends AppCompatActivity {
    Button low_freq;
    Button stop;
    TextView display;
    TextView display2;
    TextView display3;
    private boolean start = false;
    private boolean is_processing = false;
    int sampleRate = 44100;
    private int blockSize = 2048;
    Recorder recorder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task3);
        low_freq = (Button)findViewById(R.id.t3_start);
        stop = (Button)findViewById(R.id.t3_stop);
        display = (TextView)findViewById(R.id.freq1_display);
        display2 = (TextView)findViewById(R.id.freq2_display);
        display3 = (TextView)findViewById(R.id.fre_to_message);

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
            double maxFreq1 = 0;
            double max_freq_1 = 0;
            double max_freq_2 = 0;
            double maxEnergy1 = 0;
            double maxFreq2 = 0;
            double maxEnergy2 = 0;
            double enough_to_show = 100;

            for(int i = 100; i<1000;i+=100){
                double[] result = Goertzel(i, toTransform[0]);
                if(result[1]>maxEnergy1){
                    maxEnergy1 = result[1];
                    maxFreq1 = result[0];
                }
                if(maxEnergy1>enough_to_show){
                    display.setText("Current Frequency Recorded: "+maxFreq1);
                    max_freq_1 = maxFreq1;
                }else{
                    display.setText("No Significant Frequency Recorded......");
                }
            }
            enough_to_show = 100;
            for(int i = 1100; i<22000;i+=100){
                double[] result = Goertzel(i, toTransform[0]);
                if(result[1]>maxEnergy2){
                    maxEnergy2 = result[1];
                    maxFreq2 = result[0];
                }
                if(maxEnergy2>enough_to_show){
                    display2.setText("Current Frequency Recorded: "+maxFreq2);
                    max_freq_2 = maxFreq2;
                }else{
                    display2.setText("No Significant Frequency Recorded......");
                }
            }
            display3.setText("Current Dual Tone is: "+ process_dual_tone(max_freq_1,max_freq_2));
            is_processing = false;
        }
    }

    private int process_dual_tone(double max_fre_1, double max_fre_2) {
        int result = 0;
        if ((max_fre_1 < 710 && max_fre_1 > 650) && (max_fre_2 > 1170 && max_fre_2 < 1240)) {
            result = 1;
        }else if((max_fre_1 < 710 && max_fre_1 > 650) && (max_fre_2 > 1300 && max_fre_2 < 1360)){
            result = 2;
        }else if((max_fre_1 < 710 && max_fre_1 > 650) && (max_fre_2 > 1430 && max_fre_2 < 1500)){
            result = 3;
        }else if((max_fre_1 < 810 && max_fre_1 > 750) && (max_fre_2 > 1170 && max_fre_2 < 1240)){
            result = 4;
        }else if((max_fre_1 < 810 && max_fre_1 > 750) && (max_fre_2 > 1300 && max_fre_2 < 1360)){
            result = 5;
        }else if((max_fre_1 < 810 && max_fre_1 > 750) && (max_fre_2 > 1430 && max_fre_2 < 1500)){
            result = 6;
        }else if((max_fre_1 < 870 && max_fre_1 > 800) && (max_fre_2 > 1170 && max_fre_2 < 1240)){
            result = 7;
        }else if((max_fre_1 < 870 && max_fre_1 > 800) && (max_fre_2 > 1300 && max_fre_2 < 1360)){
            result = 8;
        }else if((max_fre_1 < 870 && max_fre_1 > 800) && (max_fre_2 > 1430 && max_fre_2 < 1500)){
            result = 9;
        }
        return result;
    }
}
