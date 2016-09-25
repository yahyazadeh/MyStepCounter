package com.mc.hw1.mystepcounter.services;

/**
 * Created by daniel on 9/24/16.
 * updated by shanu on 9/24/16
 */


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

import com.mc.hw1.mystepcounter.entities.CustomEvent;


public class SaveService implements Runnable {

    private Context mContext;
    private final BlockingQueue queue;
    private CustomEvent customEvent = new CustomEvent();
    private FileWriter fWriter;
    private File myFile;

    public SaveService(BlockingQueue q, Context mContext) {
        this.queue = q;
        this.mContext = mContext;
    }

    @Override
    public void run() {

        String dir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = generateFileName();

        // Check to see if external memory is Ok
        if (isExternalStorageWritable() && dir != null) {
            dir += File.separator;
        }
        myFile = new File(dir + fileName);
        Log.e("*****TAG: ", dir);

        try {
            fWriter = new FileWriter(myFile);

            while (true) {
                customEvent = (CustomEvent) queue.take();
                if (customEvent != null) {
                    long timestamp = customEvent.getTimestamp();
                    String sensorType = customEvent.getSensorType();
                    float x = customEvent.getX();
                    float y = customEvent.getY();
                    float z = customEvent.getZ();

                    String tuple = String.valueOf(timestamp) + ","
                            + sensorType + ","
                            + String.valueOf(x) + ","
                            + String.valueOf(y) + ","
                            + String.valueOf(z) + "\n";

                    fWriter.write(tuple);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            terminate();
        }

    }

    private boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(
                Environment.getExternalStorageState());
    }

    private String generateFileName() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        return "sample" + dateFormat.format(date);
    }

    private void terminate() {
        try {
            fWriter.close();
            fWriter.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}