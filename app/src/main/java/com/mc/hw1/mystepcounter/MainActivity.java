package com.mc.hw1.mystepcounter;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mGyroscope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //sensor variables
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    public void startStop(View view) {
        Button button = (Button) findViewById(R.id.button);
        if (button.getText().toString().equals("Start")){
            button.setText("Stop");
            // TODO: Here the sensors and writing should be start
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            button.setText("Start");
            // TODO: Here the sensors and writing should be stop
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        //accelerometer sensor changed
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            //showing data to view
            TextView accX = (TextView) findViewById(R.id.accX);
            TextView accY = (TextView) findViewById(R.id.accY);
            TextView accZ = (TextView) findViewById(R.id.accZ);
            accX.setText(Float.toString(x));
            accY.setText(Float.toString(y));
            accZ.setText(Float.toString(z));
        }
        //gyroscope sensor changed
        
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            //showing data to view
            TextView gyrX = (TextView) findViewById(R.id.gyrX);
            TextView gyrY = (TextView) findViewById(R.id.gyrY);
            TextView gyrZ = (TextView) findViewById(R.id.gyrZ);
            gyrX.setText(Float.toString(x));
            gyrY.setText(Float.toString(y));
            gyrZ.setText(Float.toString(z));
        }
    }


}
