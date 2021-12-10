package com.example.aad;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.example.aad.embedder.embedder;
import com.example.aad.models.backgrounfMonitoring;
import com.example.aad.models.monitoring;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.ActionProvider;
import android.view.TextureView;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity implements startMonitoring {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView)findViewById(R.id.textViewVal)).setText("Press Button to start crash monitoring");
        final Button clickButton = (Button)findViewById(R.id.button2);
        clickButton.setText("Start");
        final Handler mHandler = new Handler() {
            public void handleMessage(Message msg) {
                System.out.println("message: " +  msg);
                ((TextView)findViewById(R.id.textViewVal)).setText("\bCALLING EMERGENCY CONTACTS");
                ((TextView)findViewById(R.id.textViewVal)).setTypeface(null, Typeface.BOLD);
                clickButton.setText("Start");
            }
        };


        clickButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(final View view) {
                        ((TextView)findViewById(R.id.textViewVal)).setText("\bMonitoring real time Values");
                        clickButton.setText("Stop");
                        System.out.println("button");
                        final monitoring monitor = new monitoring(getApplicationContext());
                        final Timer timer = new Timer();
                        timer.scheduleAtFixedRate(new TimerTask() {
                            @RequiresApi(api = Build.VERSION_CODES.P)
                            @Override
                            public void run() {
                                int priority = monitor.start();
                                final Boolean[] cancel = {false};
                                System.out.println("Crash: " + priority);
                                if(priority>1){
                                    timer.purge();
                                    timer.cancel();
                                    mHandler.obtainMessage(priority).sendToTarget();
                                }
                            }
                        },20,1000);


                    }
                }

        );
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            boolean cancel = false;
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getApplicationContext()).setTitle("Crash Detected").setMessage("Priority of Crash: " + 1).setPositiveButton(
                        "Cancel Alert", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                cancel = true;
                            }
                        }
                )
                        .create().show();
//                                            .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        System.out.println("button");
        monitoring monitor = new monitoring(getApplicationContext());
        monitor.start();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}