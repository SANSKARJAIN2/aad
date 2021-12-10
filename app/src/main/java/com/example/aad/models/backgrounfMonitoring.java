package com.example.aad.models;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Queue;

public class backgrounfMonitoring extends Service {
    Queue<float[]> q;
    public backgrounfMonitoring(Queue<float[]> vals) {
        this.q = vals;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        onTaskRemoved(intent);
        Toast.makeText(getApplicationContext(),"Monitoring",
                Toast.LENGTH_SHORT).show();
        while(q.isEmpty()==false){
            System.out.println(q.poll());
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.out.println(e.toString());
            }
        }
        return START_STICKY;
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(),this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
