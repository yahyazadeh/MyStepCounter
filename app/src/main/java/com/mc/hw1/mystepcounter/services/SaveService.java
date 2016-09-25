package com.mc.hw1.mystepcounter.services;

/**
 * Created by daniel on 9/24/16.
 * updated by shanu on 9/24/16
 */


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import com.mc.hw1.mystepcounter.MainActivity;
import com.mc.hw1.mystepcounter.entities.CustomEvent;
import com.opencsv.CSVWriter;
import com.opencsv.CSVReader;

public class SaveService implements Runnable {
    private final BlockingQueue queue;
    CustomEvent customEvent = new CustomEvent();
    public SaveService(BlockingQueue q) { queue = q; }

    @Override
    public void run() {
        while (true) {


            String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
            String fileName = "Sensordata.csv";
            String filePath = baseDir + File.separator + fileName;
            File f = new File(filePath);
          // please check here, CSV writer was not working without initialisation
            CSVWriter writer=null;

            if (f.exists() && !f.isDirectory()) {
                FileWriter mFileWriter = null;
                //may be an unnecessary try catch block
                try {
                    mFileWriter = new FileWriter(filePath, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                writer = new CSVWriter(mFileWriter);
                }


             else {
                try {
                    writer = new CSVWriter(new FileWriter(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                customEvent = (CustomEvent) queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            float data1 = customEvent.getX();
            float data2 = customEvent.getY();
            float data3 = customEvent.getZ();

            String data[] ={new String(String.valueOf(data1)),new String (String.valueOf(data2)),new String (String.valueOf(data3))};
            writer.writeNext(data);


            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}