package com.example.a9336_assignment;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by linxiaoran on 2016/10/20.
 */

public class DualTone extends AppCompatActivity {
    private final double sampleRate = 44100;
    private final double[] sample = new double[44100];
    private final byte[] wave = new byte[44100*2];
    private boolean play_sound = false;
    Button one;
    Button two;
    Button three;
    Button four;
    Button five;
    Button six;
    Button seven;
    Button eight;
    Button nine;
    Button stop;
    TextView dt_display;
    EditText edit_freq1;
    Button play;
    EditText edit_freq2;
    Thread thread;
    AudioTrack audioTrack;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dualtone);
        edit_freq1 = (EditText)findViewById(R.id.edit_freq1);
        edit_freq2 = (EditText)findViewById(R.id.edit_freq2);
        play = (Button)findViewById(R.id.play_dt);
        one = (Button)findViewById(R.id.one_dt);
        two = (Button)findViewById(R.id.two_dt);
        three = (Button)findViewById(R.id.three_dt);
        four = (Button)findViewById(R.id.four_dt);
        five = (Button)findViewById(R.id.five_dt);
        six = (Button)findViewById(R.id.six_dt);
        seven = (Button)findViewById(R.id.seven_dt);
        eight = (Button)findViewById(R.id.eight_dt);
        nine = (Button)findViewById(R.id.nine_dt);
        stop = (Button)findViewById(R.id.stop_dt);
        dt_display = (TextView)findViewById(R.id.dt_display);


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play_sound = false;
                if(audioTrack!= null){
                    audioTrack.stop();
                    audioTrack.release();
                    audioTrack = null;
                }
                dt_display.setText("Stoped.....");
            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToneGenerator dtmfGenerator = new ToneGenerator(0, ToneGenerator.MAX_VOLUME);
                dtmfGenerator.startTone(ToneGenerator.TONE_DTMF_1, 3000); // all types of tones are available...
                dt_display.setText("1209Hz and 697Hz");

            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToneGenerator dtmfGenerator = new ToneGenerator(0, ToneGenerator.MAX_VOLUME);
                dtmfGenerator.startTone(ToneGenerator.TONE_DTMF_2, 3000); // all types of tones are available...
                dt_display.setText("1336Hz and 697Hz");

            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToneGenerator dtmfGenerator = new ToneGenerator(0, ToneGenerator.MAX_VOLUME);
                dtmfGenerator.startTone(ToneGenerator.TONE_DTMF_3, 3000); // all types of tones are available...
                dt_display.setText("1477Hz and 697Hz");
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToneGenerator dtmfGenerator = new ToneGenerator(0, ToneGenerator.MAX_VOLUME);
                dtmfGenerator.startTone(ToneGenerator.TONE_DTMF_4, 3000); // all types of tones are available...
                dt_display.setText("1209Hz and 770Hz");
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToneGenerator dtmfGenerator = new ToneGenerator(0, ToneGenerator.MAX_VOLUME);
                dtmfGenerator.startTone(ToneGenerator.TONE_DTMF_5, 3000); // all types of tones are available...
                dt_display.setText("1336Hz and 770Hz");
            }
        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToneGenerator dtmfGenerator = new ToneGenerator(0, ToneGenerator.MAX_VOLUME);
                dtmfGenerator.startTone(ToneGenerator.TONE_DTMF_6, 3000); // all types of tones are available...
                dt_display.setText("1477Hz and 770Hz");
            }
        });
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToneGenerator dtmfGenerator = new ToneGenerator(0, ToneGenerator.MAX_VOLUME);
                dtmfGenerator.startTone(ToneGenerator.TONE_DTMF_7, 3000); // all types of tones are available...
                dt_display.setText("1209Hz and 852Hz");
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToneGenerator dtmfGenerator = new ToneGenerator(0, ToneGenerator.MAX_VOLUME);
                dtmfGenerator.startTone(ToneGenerator.TONE_DTMF_8, 3000); // all types of tones are available...
                dt_display.setText("1336Hz and 852Hz");
            }
        });
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToneGenerator dtmfGenerator = new ToneGenerator(0, ToneGenerator.MAX_VOLUME);
                dtmfGenerator.startTone(ToneGenerator.TONE_DTMF_9, 3000); // all types of tones are available...
                dt_display.setText("1477Hz and 852Hz");
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string1 =edit_freq1.getText().toString();
                String string2 = edit_freq2.getText().toString();
                int freqOfTon2 = Integer.parseInt(string2);
                int freqOfTon1 = Integer.parseInt(string1);
                play_sound = false;
                genTone(freqOfTon1,freqOfTon2);
                if (audioTrack != null) {
                    audioTrack.stop();
                    audioTrack.release();
                    audioTrack = null;
                }
                audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                        (int) sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT, wave.length,
                        AudioTrack.MODE_STREAM);
                thread = new Thread((new Runnable() {
                    @Override
                    public void run() {
                        playSound();
                    }
                }));
                play_sound = true;
                thread.start();
                dt_display.setText("Sending message on " + freqOfTon1 + "Hz "+"+ "+freqOfTon2+"Hz");
            }
        });
    }

    public void genTone(double freqOfTone1, double freqOfTone2){
        // fill out the array
        for (int i = 0; i < sampleRate; ++i) {
            double valueSum = 0;
            valueSum += Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone1));
            valueSum += Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone2));
            sample[i] = valueSum/2;
        }
        int ramp = ((int)(sampleRate) / 20);
        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        int i = 0;

        for (i = 0; i< sampleRate; ++i) {                        // Max amplitude for most of the samples
            double dVal = sample[i];
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            wave[idx++] = (byte) (val & 0x00ff);
            wave[idx++] = (byte) ((val & 0xff00) >>> 8);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void playSound(){
        while(play_sound) {
            try{
                if(audioTrack!=null) {
                    audioTrack.write(wave, 0, wave.length);
                }
                audioTrack.play();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
