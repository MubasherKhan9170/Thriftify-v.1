package com.example.thriftify.utils;

import android.graphics.Bitmap;
import android.os.Build;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPoolAdapter;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;

import java.util.Set;

public class GlideBitmapPool {

    private static final int DEFAULT_MAX_SIZE = 6 * 1024 * 1024;
    private BitmapPool bitmapPool;
    private static GlideBitmapPool sInstance = null;

    private GlideBitmapPool(int maxSize) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            bitmapPool = new LruBitmapPool(maxSize);
        } else {
            bitmapPool = new BitmapPoolAdapter();
        }
    }

    private GlideBitmapPool(int maxSize, Set<Bitmap.Config> allowedConfigs) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            bitmapPool = new LruBitmapPool(maxSize, allowedConfigs);
        } else {
            bitmapPool = new BitmapPoolAdapter();
        }
    }

    public static GlideBitmapPool getInstance(int max, Set<Bitmap.Config> allowedConfigs) {
        if (sInstance == null) {
            sInstance = new GlideBitmapPool(max, allowedConfigs);
        }
        return sInstance;
    }

    public static void initialize(int maxSize) {
        sInstance = new GlideBitmapPool(maxSize);
    }

    public static void initialize(int maxSize, Set<Bitmap.Config> allowedConfigs) {
        sInstance = new GlideBitmapPool(maxSize, allowedConfigs);
    }

    public static void putBitmap(Bitmap bitmap) {
        sInstance.bitmapPool.put(bitmap);
    }

    public static Bitmap getBitmap(int width, int height, Bitmap.Config config){
        return sInstance.bitmapPool.get(width, height, config);
    }

    public static Bitmap getDirtyBitmap(int width, int height, Bitmap.Config config) {
        return sInstance.bitmapPool.getDirty(width, height, config);
    }

    public static void clearMemory() {
        sInstance.bitmapPool.clearMemory();
    }

    public static void trimMemory(int level) {
        sInstance.bitmapPool.trimMemory(level);
    }

    public static void shutDown() {
        if (sInstance != null) {
            sInstance.bitmapPool.clearMemory();
            sInstance = null;
        }
    }
    public static  long getSize(){
        return sInstance.bitmapPool.getMaxSize();
    }

}
