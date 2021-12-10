package com.example.aad.models;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.example.aad.utils.Constants;

import android.icu.text.SymbolTable;
import android.os.Handler;
import java.lang.Math;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
//import java.util.logging.Handler;

import com.example.aad.embedder.embedder;

import javax.security.auth.callback.Callback;

class accelerations{
    Boolean issue = false;
    double x,y,z;
    accelerations(float values []){
        issue = values[0]==-1;
        if(!issue){
            x = values[1];y=values[2];z= values[3];
        }else{

        }

    }
    private boolean roadIssue(double value){
        return (value>1&&value<2.5);
    }
    public int getPriority(){
        if(x>2.5||y>2.5||(z-9.5)>2.5){
            return 3;
        }
        if( (x>1||y>1||z>1)&& (roadIssue(z))){
            return 2;
        }
        return 1;

    }
}
class orientation{
    double x,y,z;
    orientation(float values[]){
        System.out.println("rotation len: "+ values.length);
        for(int i = 0;i<values.length;i = i+1){

            System.out.println("i: "+ i + " value: "+values[i]);
        }
        if(values[0]==-1){
            x = 0;y=0;z=0;
            return ;
        }
        x = values[1]*100;y = values[2]*100;z = values[3]*100;
        System.out.println("x: " + x + "y: "+y+" z: "+z);
    }
    public boolean rollover(){
        return (Math.abs(this.x)>50 ||Math.abs(this.y)>50);
    }

}
class gps{
double longitude,latitude,altitude,speed;
int freq;
gps(){
    longitude = 0;latitude = 0;altitude = 0;speed = 0;
    freq = Constants.gpsFreq;
    }

    void copyObject(gps cpy){
        longitude = cpy.longitude;latitude = cpy.latitude;altitude = cpy.altitude;
    }

    double square(double x){
        return x*x;
    }
void update(){
    gps newCords = new gps();
    double squareSum = square(newCords.latitude - latitude) + square(newCords.longitude - longitude) + square(newCords.altitude - altitude);
    speed = Math.sqrt(squareSum);
    copyObject(newCords);
}
}


public class monitoring {
    private Context context;
    private accelerations acc;
    private orientation orn;
    private gps gpsCords;
    embedder accelSensor, orientationSensor;

    public monitoring(Context context) {
        this.context = context;
        embedder sensorAcc = new embedder(Sensor.TYPE_ACCELEROMETER, SensorManager.SENSOR_DELAY_NORMAL, 20, this.context);
        embedder sensorOrn = new embedder(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR, SensorManager.SENSOR_DELAY_NORMAL, 20, this.context);
        this.accelSensor = sensorAcc;
        this.orientationSensor = sensorOrn;
//      embedder sensorgps = new embedder(Sensor.TYPE_ACCELEROMETER, SensorManager.SENSOR_DELAY_NORMAL,1000);

    }

    public int start() {
        this.acc = new accelerations(this.accelSensor.getData());
        this.orn = new orientation(this.orientationSensor.getData());
        System.out.println("checking" + this.acc.issue);
        int priority = 0;
        System.out.println("rollover: " + orn.rollover());
        if (this.acc.getPriority() > 1 || this.orn.rollover()) {

            priority = Math.max(this.acc.getPriority(), orn.rollover()?1:0);
        }
        return priority;
        //    int p = -1;
//    try{
////        p = priority.get();
//        System.out.println("priority: " + p);
//
//    }catch (Exception e){
//        System.out.println(e.toString());
//    }
//    System.out.println("system shutdown");
//
//    monitorThread.shutdown();
//    return p;
    }
}
//class startMonitoring {
//    private embedder accelSensor;
//    private  accelerations acc;
////    private Context context;
//    public startMonitoring(embedder sensor){
//        this.accelSensor = sensor;
//    }
//
//    public Integer call() {
//        int priority = 0;
//        int i = 0;
////        while(true){
////            if(i==10){
////                break;
////            }
//            i+=1;
//
////                break;
////             return this.acc.getPriority();
//            }
////            try {
////                Thread.sleep(1200);
////            }catch (Exception e){
////                System.out.println(e.toString());
////            }
//
////        }
//        return priority;
////        return null;
//    }
//}
