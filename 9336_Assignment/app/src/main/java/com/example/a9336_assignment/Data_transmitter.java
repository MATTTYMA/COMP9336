package com.example.a9336_assignment;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by linxiaoran on 2016/10/21.
 */

public class Data_transmitter extends AppCompatActivity{
    private final double sampleRate = 44100;
    private final double duration =15;
    private final double numSamples = duration * sampleRate;
    private final double sample[] = new double[(int)(numSamples)];
    private byte[] wave = new byte[2*(int)(numSamples)];
    Button start;
    EditText editText;
    TextView tv;
    AudioTrack audioTrack;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_transmitter);
        start = (Button)findViewById(R.id.trans_start);
        editText = (EditText)findViewById(R.id.message_to_send);
        tv = (TextView)findViewById(R.id.tran_display);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string;
                string = editText.getText().toString();
                String encoding = "";
                encoding+="^";
                for(int i=0; i<string.length();++i){
                    char c = string.charAt(i);
                    int asc = (int)c;
                    String hex_string = Integer.toHexString(asc);
                    encoding+='(';
                    for(int j = 0; j<hex_string.length();++j){
                        encoding+=hex_string.charAt(j);
                        if(j<hex_string.length()-1){
                            encoding+='-';
                        }
                    }
                    encoding+=')';
                }
                encoding+="$$";
                tv.setText(encoding);
                for(int i=0;i<encoding.length();++i){
//                    int[] fre_group = find_freq(encoding.charAt(i));
                    int fre_group = find_freq(encoding.charAt(i));
                    wave = new byte[2*(int)(numSamples)];
//                    genTone(fre_group[0],fre_group[1]);
                    genTone(fre_group);
                    if(audioTrack!=null){
                        audioTrack.stop();
                        audioTrack.release();
                        audioTrack=null;
                    }
                    audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                            (int) sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                            AudioFormat.ENCODING_PCM_16BIT, wave.length,
                            AudioTrack.MODE_STATIC);
                    audioTrack.write(wave, 0, wave.length);
                    audioTrack.play();
                }
                if(audioTrack!=null){
                    audioTrack.stop();
                    audioTrack.release();
                    audioTrack=null;
                }

            }
        });
    }

//    public void genTone(double freqOfTone1, double freqOfTone2){
//        // fill out the array
//        for (int i = 0; i < numSamples; ++i) {
//            double valueSum = 0;
//            valueSum += Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone1));
//            valueSum += Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone2));
//            sample[i] = valueSum/2;
//        }
//        int ramp = ((int)(sampleRate) / 20);
//        // convert to 16 bit pcm sound array
//        // assumes the sample buffer is normalised.
//        int idx = 0;
//        int i = 0;
//        for (i = 0; i< numSamples; ++i) {                        // Max amplitude for most of the samples
//            double dVal = sample[i];
//            // scale to maximum amplitude
//            final short val = (short) ((dVal * 32767));
//            // in 16 bit wav PCM, first byte is the low order byte
//            wave[idx++] = (byte) (val & 0x00ff);
//            wave[idx++] = (byte) ((val & 0xff00) >>> 8);
//        }
//    }

    public void genTone(double freqOfTone){
        // fill out the array
        for (int i = 0; i < sampleRate; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            wave[idx++] = (byte) (val & 0x00ff);
            wave[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
    }

    public int find_freq(char c){
        int result  = 0;
        if(c == '0'){

            result=12000;
        }else if(c == '1'){

            result=12300;
        }else if(c == '2'){

            result=12600;
        }else if(c == '3'){

            result=12900;
        }else if(c == '4'){
            result=13200;
        }else if(c == '5'){
            result=13500;
        }else if(c == '6'){

            result=13800;
        }else if(c == '7'){
            result = 14100;
        }else if(c == '8'){

            result=14400;
        }else if(c == '9'){
            result=14700;
        }else if(c == 'a'){
            result=15000;
        }else if(c == 'b'){
            result=15300;
        }else if(c == 'c'){
            result = 15600;
        }else if(c == 'd'){

            result=15900;
        }else if(c == 'e'){

            result=16200;
        }else if(c == 'f'){
            result=16500;
        }else if(c == '-'){
            result=16800;
        }else if(c == '^'){
            result=17100;
        }else if(c == '$'){

            result=17400;
        }else if(c == '('){
            result=17700;
        }else if(c == ')'){
            result=18000;
        }
        return result;
    }

//    public int[] find_freq(char c){
//        int[] result  = new int[2];
//        if(c == '0'){
//            result[0]=5000;
//            result[1]=12000;
//        }else if(c == '1'){
//            result[0]=5300;
//            result[1]=12300;
//        }else if(c == '2'){
//            result[0]=5600;
//            result[1]=12600;
//        }else if(c == '3'){
//            result[0]=5900;
//            result[1]=12900;
//        }else if(c == '4'){
//            result[0]=6200;
//            result[1]=13200;
//        }else if(c == '5'){
//            result[0]=6500;
//            result[1]=13500;
//        }else if(c == '6'){
//            result[0]=6800;
//            result[1]=13800;
//        }else if(c == '7'){
//            result[0]=7100;
//            result[1]=14100;
//        }else if(c == '8'){
//            result[0]=7400;
//            result[1]=14400;
//        }else if(c == '9'){
//            result[0]=7700;
//            result[1]=14700;
//        }else if(c == 'a'){
//            result[0]=8000;
//            result[1]=15000;
//        }else if(c == 'b'){
//            result[0]=8300;
//            result[1]=15300;
//        }else if(c == 'c'){
//            result[0]=8600;
//            result[1]=15600;
//        }else if(c == 'd'){
//            result[0]=8900;
//            result[1]=15900;
//        }else if(c == 'e'){
//            result[0]=9200;
//            result[1]=16200;
//        }else if(c == 'f'){
//            result[0]=9500;
//            result[1]=16500;
//        }else if(c == '-'){
//            result[0]=9800;
//            result[1]=16800;
//        }else if(c == '^'){
//            result[0]=10100;
//            result[1]=17100;
//        }else if(c == '$'){
//            result[0]=10400;
//            result[1]=17400;
//        }else if(c == '('){
//            result[0]=10700;
//            result[1]=17700;
//        }else if(c == ')'){
//            result[0]=11000;
//            result[1]=18000;
//        }
//        return result;
//    }
}
