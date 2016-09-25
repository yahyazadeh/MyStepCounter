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

import com.mc.hw1.mystepcounter.entities.CustomEvent;
import com.mc.hw1.mystepcounter.services.SaveService;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private final BlockingQueue queue = new ArrayBlockingQueue(2048);
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

        // Start the SaveService thread
        new Thread(new SaveService(queue)).start();



    }

    public void startStop(View view) {
        Button button = (Button) findViewById(R.id.button);
        if (button.getText().toString().equals("Start")){
            button.setText("Stop");

            // Check to see if the accelerometer exists
            if (mAccelerometer != null) {
                mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Toast.makeText(this, "Accelerometer doesn't exist", Toast.LENGTH_SHORT).show();
            }
            // Check to see if the accelerometer exists
            if (mGyroscope != null) {
                mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Toast.makeText(this, "Gyroscope doesn't exist", Toast.LENGTH_SHORT).show();
            }


        } else {
            button.setText("Start");
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        //accelerometer sensor changed
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            long timestamp = event.timestamp;

            CustomEvent customEvent = new CustomEvent();
            customEvent.setX(x);
            customEvent.setY(y);
            customEvent.setZ(z);
            customEvent.setTimestamp(timestamp);
            customEvent.setSensorType("A");

            try {
                queue.put(customEvent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

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
            long timestamp = event.timestamp;

            CustomEvent customEvent = new CustomEvent();
            customEvent.setX(x);
            customEvent.setY(y);
            customEvent.setZ(z);
            customEvent.setTimestamp(timestamp);
            customEvent.setSensorType("G");

            try {
                queue.put(customEvent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

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
