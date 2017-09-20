package com.example.linxiaoran.my_first_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button clickMeBtn	=	(Button)	findViewById(R.id.button1);
        clickMeBtn.setOnClickListener(new View.OnClickListener()	{
            public void onClick(View v)	{
                myClick(v);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu	menu)	{
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void myClick(View v){
        count +=1;
        TextView txCounter = (TextView) findViewById(R.id.textView1);
        txCounter.setText("Number is: \n"+count);
    }
}
