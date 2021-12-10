package com.example.aad;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

public class monitorIntent extends IntentService {
    /**
     * @param name
     * @deprecated
     */
    public monitorIntent(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
