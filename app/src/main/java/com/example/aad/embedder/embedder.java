package com.example.aad.embedder;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.time.LocalDateTime;
import java.util.*;
import com.example.aad.models.acceleration;

import java.lang.reflect.Array;

import static android.content.Context.SENSOR_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;


public class embedder implements SensorEventListener {
    private SensorManager manager;
    private  Sensor sensor ;
    Context context;
    int sensorType;
    long freq;
    Queue<float[]> values = new LinkedList<float[]>();
    long lastUpdate;
    public embedder(int sensorType,int sensorDelay,long freq,Context context){
        try{
            this.sensorType = sensorType;
            this.freq = freq;
            this.lastUpdate = System.currentTimeMillis();
            this.manager = (SensorManager) context.getSystemService(SENSOR_SERVICE);

            this.sensor = this.manager.getDefaultSensor(sensorType);
            this.manager.registerListener(this,this.sensor,sensorDelay);
            this.context = context;
            System.out.println("Sensor initialized");
        }catch(Exception e){
            System.out.println("Error Sensor issue sj " + e.toString());
        }


    }
//    void getAccelerationData(int sensorType,int sensorDelay){
//        this.lastUpdate = System.currentTimeMillis();
//        manager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
//        data = manager.getDefaultSensor(sensorType);
//        manager.registerListener(this,data,sensorDelay);
////        manager.registerListener(this.onSensorChanged, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
//
//    }
    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        System.out.println("change detected");
    if( event.sensor.getType() != sensorType) {
//        System.out.println("invalid sensor");
        return;
    }
//        System.out.println("adding");
    this.pushValue(event.values);
    }
    void pushValue(float val[]){
        if(this.sensorType==Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR){
//            System.out.println("rotation: "+ val[0]+val[1]+val[2]+"last update" + this.lastUpdate);
        }
        if(System.currentTimeMillis()-lastUpdate>=freq){
            this.values.add(val);
            lastUpdate = System.currentTimeMillis();
        }
    }
    public float [] getData(){
        float[] arr = {0,0,0,0};

        if(this.values.isEmpty()){
            arr[0]= -1;

        }else{
            float [] vals = this.values.poll();
            for(int i = 0;i<Math.min(vals.length,3);i++){
//                if(this.sensorType==Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR){
//                    System.out.println("rotation: "+ i);
//                }
                arr[i+1] = vals[i];
            }
        }


        return arr;
    }

}
