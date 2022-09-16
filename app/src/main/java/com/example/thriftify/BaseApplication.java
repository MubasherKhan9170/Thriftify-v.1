package com.example.thriftify;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.thriftify.utils.GlideBitmapPool;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";
    private static int maxMemory;
    private static Set<Bitmap.Config> configSet;
    @Override
    public void onCreate() {
        super.onCreate();
         maxMemory= (int) (Runtime.getRuntime().maxMemory() / (1024*1024));
        Log.e(TAG, "getMemorySize: "+ maxMemory);
        configSet = new HashSet<Bitmap.Config>(Arrays.asList(Bitmap.Config.values()));
        GlideBitmapPool.initialize(maxMemory,configSet);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        GlideBitmapPool.shutDown();
    }
}

