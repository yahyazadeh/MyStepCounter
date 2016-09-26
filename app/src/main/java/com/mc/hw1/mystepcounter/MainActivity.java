package com.mc.hw1.mystepcounter;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mc.hw1.mystepcounter.entities.CustomEvent;
import com.mc.hw1.mystepcounter.services.SaveService;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private final BlockingQueue queue = new LinkedBlockingQueue();
    private final BlockingQueue pQueue = new LinkedBlockingQueue();
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    private TextView consoleTextView;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mGyroscope;
    private Thread saveThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate the power manager and create a WakeLock
        mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "myStepCounterTag");

        // Instantiate the consoleTextView
        consoleTextView = (TextView) findViewById(R.id.consoleText);

        //sensor variables
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    public void startStop(View view) {
        Button button = (Button) findViewById(R.id.button);
        if (button.getText().toString().equals("Start")) {
            button.setText("Stop");

            // Initialize pQueue
            print("Please wait for initialization...");
            initialize_pQueue(32768);

            // Check to see if the accelerometer exists
            if (mAccelerometer != null) {
                mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                print("Accelerometer is now sending the data...");
            } else {
                print("Accelerometer doesn't exist!");
            }
            // Check to see if the accelerometer exists
            if (mGyroscope != null) {
                mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
                print("Gyroscope is now sending the data...");
            } else {
                print("Gyroscope doesn't exist!");
            }

            // Start the SaveService thread
            saveThread = new Thread(new SaveService(queue, pQueue, getBaseContext()));
            saveThread.start();
            print("Saving Data to disk...");


        } else {
            button.setText("Finished");
            button.setEnabled(false);
            mSensorManager.unregisterListener(this);
            print("Sensors have been stopped!");
            print("Wait for until your data gets ready...");
            print("Data is ready.");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        //accelerometer sensor changed
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            try {
                CustomEvent customEvent = (CustomEvent) pQueue.take();
                customEvent.setX(event.values[0]);
                customEvent.setY(event.values[1]);
                customEvent.setZ(event.values[2]);
                customEvent.setTimestamp(event.timestamp);
                customEvent.setSensorType("A");
                queue.put(customEvent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //gyroscope sensor changed
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            try {
                CustomEvent customEvent = (CustomEvent) pQueue.take();
                customEvent.setX(event.values[0]);
                customEvent.setY(event.values[1]);
                customEvent.setZ(event.values[2]);
                customEvent.setTimestamp(event.timestamp);
                customEvent.setSensorType("G");
                queue.put(customEvent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void print(String msg) {
        consoleTextView.setText(consoleTextView.getText().toString() + "\n " + msg);
    }

    // This is for keeping the CPU alive while the app is running
    @Override
    public void onStart() {
        mWakeLock.acquire();
        super.onStart();
    }

    // The app will release the WakeLock when it gets closed
    @Override
    public void onStop() {
        mWakeLock.release();
        super.onStop();
    }

    private void initialize_pQueue(int numberOfElement) {
        for (int i = 0; i <= numberOfElement; i++) {
            CustomEvent c = new CustomEvent();
            try {
                pQueue.put(c);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
