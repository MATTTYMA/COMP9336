package com.example.a9336_assignment;

import android.content.Intent;
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
 * Created by linxiaoran on 2016/10/19.
 */

public class Transmitter extends AppCompatActivity {
    private final double sampleRate = 44100;
    private final double[] sample = new double[44100];
    private final byte[] wave = new byte[44100*2];
    TextView send_fre_display;
    Button one;
    Button two;
    Button three;
    Button four;
    Button five;
    Button six;
    Button seven;
    Button eight;
    Button nigh;
    Button stop;
    EditText edit_fre;
    Button edit_play;
    Button tran_dt;
    private boolean play_sound = false;
    Thread thread;
    AudioTrack audioTrack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transmitter);
        send_fre_display = (TextView)findViewById(R.id.send_fre_display_dt);
        one = (Button)findViewById(R.id.one);
        two = (Button)findViewById(R.id.two);
        three = (Button)findViewById(R.id.three);
        four = (Button)findViewById(R.id.four);
        five = (Button)findViewById(R.id.five);
        six = (Button)findViewById(R.id.six);
        seven = (Button)findViewById(R.id.seven);
        eight = (Button)findViewById(R.id.eight);
        nigh = (Button)findViewById(R.id.nigh);
        tran_dt = (Button)findViewById(R.id.tran_dt);
        edit_play = (Button)findViewById(R.id.edit_fre_play);
        stop = (Button)findViewById(R.id.stop);
        edit_fre = (EditText)findViewById(R.id.edit_fre);

        tran_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent button_intent = new Intent();
                button_intent.setClass(Transmitter.this, DualTone.class);
                startActivity(button_intent);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play_sound = false;
                if(audioTrack!= null){
                    audioTrack.stop();
                    audioTrack.release();
                    audioTrack = null;
                }
                send_fre_display.setText("Stoped.....");
            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play_sound = false;
                double freqOfTon = 100;
                genTone(freqOfTon);
                if(audioTrack!=null){
                    audioTrack.stop();
                    audioTrack.release();
                    audioTrack=null;
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
                send_fre_display.setText("Sending message on "+freqOfTon+"Hz......");
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play_sound = false;
                int freqOfTon = 200;
                genTone(freqOfTon);
                if(audioTrack!=null){
                    audioTrack.stop();
                    audioTrack.release();
                    audioTrack=null;
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
                send_fre_display.setText("Sending message on "+freqOfTon+"Hz......");
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play_sound = false;
                int freqOfTon = 300;
                genTone(freqOfTon);
                if(audioTrack!=null){
                    audioTrack.stop();
                    audioTrack.release();
                    audioTrack=null;
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
                send_fre_display.setText("Sending message on "+freqOfTon+"Hz......");
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play_sound = false;
                int freqOfTon = 400;
                genTone(freqOfTon);
                if(audioTrack!=null){
                    audioTrack.stop();
                    audioTrack.release();
                    audioTrack=null;
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
                send_fre_display.setText("Sending message on "+freqOfTon+"Hz......");
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play_sound = false;
                int freqOfTon = 500;
                genTone(freqOfTon);
                if(audioTrack!=null){
                    audioTrack.stop();
                    audioTrack.release();
                    audioTrack=null;
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
                send_fre_display.setText("Sending message on "+freqOfTon+"Hz......");
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play_sound = false;
                int freqOfTon = 600;
                genTone(freqOfTon);
                if(audioTrack!=null){
                    audioTrack.stop();
                    audioTrack.release();
                    audioTrack=null;
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
                send_fre_display.setText("Sending message on "+freqOfTon+"Hz......");
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play_sound = false;
                int freqOfTon = 700;
                genTone(freqOfTon);
                if(audioTrack!=null){
                    audioTrack.stop();
                    audioTrack.release();
                    audioTrack=null;
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
                send_fre_display.setText("Sending message on "+freqOfTon+"Hz......");
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play_sound = false;
                int freqOfTon = 800;
                genTone(freqOfTon);
                if(audioTrack!=null){
                    audioTrack.stop();
                    audioTrack.release();
                    audioTrack=null;
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
                send_fre_display.setText("Sending message on "+freqOfTon+"Hz......");
            }
        });

        nigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play_sound = false;
                int freqOfTon = 900;
                genTone(freqOfTon);
                if(audioTrack!=null){
                    audioTrack.stop();
                    audioTrack.release();
                    audioTrack=null;
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
                send_fre_display.setText("Sending message on "+freqOfTon+"Hz......");
            }
        });

        edit_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string = edit_fre.getText().toString();
                int freqOfTon = Integer.parseInt(string);
                play_sound = false;
                genTone(freqOfTon);
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
                send_fre_display.setText("Sending message on " + freqOfTon + "Hz......");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Use a new tread as this can take a while
    }

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
