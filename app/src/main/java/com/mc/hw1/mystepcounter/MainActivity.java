package com.mc.hw1.mystepcounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startStop(View view) {
        Button button = (Button) findViewById(R.id.button);
        if (button.getText().toString().equals("Start")){
            button.setText("Stop");
            // TODO: Here the sensors and writing should be start
        } else {
            button.setText("Start");
            // TODO: Here the sensors and writing should be stop
        }
    }
}
