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

import java.util.ArrayList;

/**
 * Created by linxiaoran on 2016/10/19.
 */

public class Task4 extends AppCompatActivity {
    Button low_freq;
    Button stop;
    TextView display;
    TextView display3;
    TextView display2;
    private boolean start = false;
    private boolean is_processing = false;
//    private final double sampleRate = 44100;
//    private final double duration = 30;
//    private final double numSamples = duration * sampleRate;
//    private final double sample[] = new double[(int)(numSamples)];
//    private final byte[] wave = new byte[2*(int)(numSamples)];
    int sampleRate = 44100;
    private int blockSize = 2048;
    Recorder recorder;
    private String recieved = "";
    private double current_fre1 = 0;
    private double current_fre2 = 0;
    private char current_message = ' ';
    private String temp = "";
    private int test = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task4);
        low_freq = (Button)findViewById(R.id.t4_start);
        stop = (Button)findViewById(R.id.t4_stop);
        display = (TextView)findViewById(R.id.t4_display);
        display2 = (TextView)findViewById(R.id.t4_display2);
        display3 = (TextView)findViewById(R.id.t4_display3);


        low_freq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(start){
                    start = false;
                }
                recieved = "";
                temp = "";
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


        for(double targetFre = freq-50; targetFre <= freq; targetFre+=50) {
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
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, 44100/2);
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
            double maxEnergy1 = 0;
            double maxFreq2 = 0;
            double maxEnergy2 = 0;
            double enough_to_show = 200;
            double max_fre_1 = 0;
            double max_fre_2 = 0;

            for(int i = 12000; i<=18000;i+=300){
                double[] result = Goertzel(i, toTransform[0]);
                if(result[1]>maxEnergy1){
                    maxEnergy1 = result[1];
                    maxFreq1 = result[0];
                }
                if(maxEnergy1>enough_to_show){
                    max_fre_1 = maxFreq1;
                }
            }
//            for(int i = 12000; i<=18000;i+=300){
//                double[] result = Goertzel(i, toTransform[0]);
//                if(result[1]>maxEnergy2){
//                    maxEnergy2 = result[1];
//                    maxFreq2 = result[0];
//                }
//                if(maxEnergy2>enough_to_show) {
//                    max_fre_2 = maxFreq2;
//                }
//            }
            test += 1;
//            if(max_fre_1!=0.0 && max_fre_2!=0.0){
            if(max_fre_1!=0.0){
//                if(max_fre_1!= current_fre1 || max_fre_2!= current_fre2){
                if(max_fre_1!= current_fre1){
                    temp+= "Recieved: "+ max_fre_1+" "+test+"\n";
                    current_fre1 = max_fre_1;

                }
//                char current_re = Decode(max_fre_1,max_fre_2)[0];
                char current_re = Decode(max_fre_1)[0];
                if(current_re!= current_message && current_re!='K'){
                    recieved+= current_re;
                    current_message = current_re;
                }
            }
            if(recieved.length()!=0) {
                if (recieved.charAt(recieved.length() - 1) != '$') {
                    display.setText("Processing.....");
                } else {
                    display.setText(process_data(recieved));
//                    display.setText(process_data("^(6-8)(7-4)(7-4)(7-0)$"));
                }
            }
//            display.setText(temp);
            display2.setText(recieved);

            is_processing = false;
        }
    }

    private String process_data(String recievd) {
        if(recievd.charAt(0)!='^'){
            return "Bad message, try again!";
        }
        char[] temp_dict = {'1', '2', '3', '4', '5','6','7', '8','9','0','a','b','c','d','e','f'};
        ArrayList<Character> dict = new ArrayList<Character>();
        for(int i = 0;i<temp_dict.length;++i){
            dict.add(temp_dict[i]);
        }
        String result = "";
        String temp = "";
        int i = 1;
        while(i<recievd.length()){
            display3.setText(result);
            if(recievd.charAt(i)=='('){
                temp = "";
                if(!dict.contains(recievd.charAt(i+1))){
                    return "Bad message, try again!";
                }
                i+=1;
            }else if(recievd.charAt(i)=='$'){
                break;
            }else {
                if(dict.contains(recievd.charAt(i))) {
                    temp += recievd.charAt(i);
                }
                if(dict.contains(recievd.charAt(i+2))){
                    temp+=recievd.charAt(i+2);
                    i+=2;
                    result += (char)Long.parseLong(temp,16);
                }else{
                    return "Bad message, try again!";
                }
                if(recievd.charAt(i+1)!=')'){
                    return "Bad message, try again!";
                }else {
                    i+=2;
                    temp = "";
                }
            }
        }
        return result;
    }

    private char[] Decode(double max_fre_2) {
        char[] current_char = new char[1];
        if ( (max_fre_2 > 11970 && max_fre_2 < 12030)) {
            current_char[0] = '0';
        }else if((max_fre_2 > 12270 && max_fre_2 < 12330)){
            current_char[0] = '1';
        }else if((max_fre_2 > 12570 && max_fre_2 < 12630)){
            current_char[0] = '2';
        }else if((max_fre_2 > 12870 && max_fre_2 < 12930)){
            current_char[0] = '3';
        }else if((max_fre_2 < 13230 && max_fre_2 > 13100)){
            current_char[0] = '4';
        }else if((max_fre_2 < 13530 && max_fre_2 > 13470)){
            current_char[0] = '5';
        }else if((max_fre_2 < 13830 && max_fre_2 > 13770)){
            current_char[0] = '6';
        }else if((max_fre_2 < 14130 && max_fre_2 > 14070)){
            current_char[0] = '7';
        }else if((max_fre_2 < 14430 && max_fre_2 > 14370)){
            current_char[0] = '8';
        }else if((max_fre_2 < 14730 && max_fre_2 > 14670)){
            current_char[0] = '9';
        }else if((max_fre_2 < 15030 && max_fre_2 > 14970)){
            current_char[0] = 'a';
        }else if((max_fre_2 < 15330 && max_fre_2 > 15270)){
            current_char[0] = 'b';
        }else if((max_fre_2 < 15630 && max_fre_2 > 15570)){
            current_char[0] = 'c';
        }else if((max_fre_2 < 15930 && max_fre_2 > 15870)){
            current_char[0] = 'd';
        }else if((max_fre_2 < 16230 && max_fre_2 > 16170)){
            current_char[0] = 'e';
        }else if((max_fre_2 < 16530 && max_fre_2 > 16470)){
            current_char[0] = 'f';
        }else if((max_fre_2 < 16830 && max_fre_2 > 16770)){
            current_char[0] = '-';
        }else if((max_fre_2 < 17130 && max_fre_2 > 17070)){
            current_char[0] = '^';
        }else if( (max_fre_2 < 17430 && max_fre_2 > 17370)){
            current_char[0] = '$';
        }else if((max_fre_2 < 17730 && max_fre_2 > 17670)){
            current_char[0] = '(';
        }else if((max_fre_2 < 18030 && max_fre_2 > 17900)){
            current_char[0] = ')';
        }else{
            current_char[0]='K';
        }
        return current_char;
    }

//    private char[] Decode(double max_fre_1, double max_fre_2) {
//        char[] current_char = new char[1];
//        if ((max_fre_1 < 5030 && max_fre_1 > 4970) && (max_fre_2 > 11970 && max_fre_2 < 12030)) {
//            current_char[0] = '0';
//        }else if((max_fre_1 < 5330 && max_fre_1 > 5270) && (max_fre_2 > 12270 && max_fre_2 < 12330)){
//            current_char[0] = '1';
//        }else if((max_fre_1 < 5630 && max_fre_1 > 5570) && (max_fre_2 > 12570 && max_fre_2 < 12630)){
//            current_char[0] = '2';
//        }else if((max_fre_1 < 5930 && max_fre_1 > 5870) && (max_fre_2 > 12870 && max_fre_2 < 12930)){
//            current_char[0] = '3';
//        }else if((max_fre_1 < 6230 && max_fre_1 > 6170) && (max_fre_2 < 13230 && max_fre_2 > 13100)){
//            current_char[0] = '4';
//        }else if((max_fre_1 < 6530 && max_fre_1 > 6470) && (max_fre_2 < 13530 && max_fre_2 > 13470)){
//            current_char[0] = '5';
//        }else if((max_fre_1 < 6830 && max_fre_1 > 6770) && (max_fre_2 < 13830 && max_fre_2 > 13770)){
//            current_char[0] = '6';
//        }else if((max_fre_1 < 7130 && max_fre_1 > 7070) && (max_fre_2 < 14130 && max_fre_2 > 14070)){
//            current_char[0] = '7';
//        }else if((max_fre_1 < 7430 && max_fre_1 > 7370) && (max_fre_2 < 14430 && max_fre_2 > 14370)){
//            current_char[0] = '8';
//        }else if((max_fre_1 < 7730 && max_fre_1 > 7670) && (max_fre_2 < 14730 && max_fre_2 > 14670)){
//            current_char[0] = '9';
//        }else if((max_fre_1 < 8030 && max_fre_1 > 7970) && (max_fre_2 < 15030 && max_fre_2 > 14970)){
//            current_char[0] = 'a';
//        }else if((max_fre_1 < 8330 && max_fre_1 > 8270) && (max_fre_2 < 15330 && max_fre_2 > 15270)){
//            current_char[0] = 'b';
//        }else if((max_fre_1 < 8630 && max_fre_1 > 8570) && (max_fre_2 < 15630 && max_fre_2 > 15570)){
//            current_char[0] = 'c';
//        }else if((max_fre_1 < 8930 && max_fre_1 > 8870) && (max_fre_2 < 15930 && max_fre_2 > 15870)){
//            current_char[0] = 'd';
//        }else if((max_fre_1 < 9230 && max_fre_1 > 9170) && (max_fre_2 < 16230 && max_fre_2 > 16170)){
//            current_char[0] = 'e';
//        }else if((max_fre_1 < 9530 && max_fre_1 > 9470) && (max_fre_2 < 16530 && max_fre_2 > 16470)){
//            current_char[0] = 'f';
//        }else if((max_fre_1 < 9830 && max_fre_1 > 9770) && (max_fre_2 < 16830 && max_fre_2 > 16770)){
//            current_char[0] = '-';
//        }else if((max_fre_1 < 10130 && max_fre_1 > 10070) && (max_fre_2 < 17130 && max_fre_2 > 17070)){
//            current_char[0] = '^';
//        }else if((max_fre_1 < 10430 && max_fre_1 > 10370) && (max_fre_2 < 17430 && max_fre_2 > 17370)){
//            current_char[0] = '$';
//        }else if((max_fre_1 < 10730 && max_fre_1 > 10670) && (max_fre_2 < 17730 && max_fre_2 > 17670)){
//            current_char[0] = '(';
//        }else if((max_fre_1 < 11030 && max_fre_1 > 10970) && (max_fre_2 < 18030 && max_fre_2 > 17900)){
//            current_char[0] = ')';
//        }else{
//            current_char[0]='K';
//        }
//        return current_char;
//    }

}
