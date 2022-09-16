package com.example.thriftify.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.example.thriftify.view.ui.EditorActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class SaveDrawingTask extends AsyncTask<Bitmap, Void, Pair<File, Exception>> {
    private static final String TAG = "SaveDrawingTask";

    private static final String SAVED_IMAGE_FORMAT = "jpeg";
    private static final String SAVED_IMAGE_NAME = "cutout_tmp";
    private  static final int CUTOUT_ACTIVITY_RESULT_ERROR_CODE = 200;
    private static Context context;



    private final WeakReference<EditorActivity> activityWeakReference;

    public SaveDrawingTask(EditorActivity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
        context = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //activityWeakReference.get().loadingModal.setVisibility(VISIBLE);
    }

    @Override
    protected Pair<File, Exception> doInBackground(Bitmap... bitmaps) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            File file = File.createTempFile(SAVED_IMAGE_NAME, SAVED_IMAGE_FORMAT, activityWeakReference.get().getApplicationContext().getCacheDir());
            Bitmap.createScaledBitmap(bitmaps[0], 1282, 1920, true).compress(Bitmap.CompressFormat.JPEG, 80, out);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                byte[] data = out.toByteArray();
                fos.write(data);
                fos.flush();
                fos.close();
                return new Pair<>(file, null);
            }
        } catch (IOException e) {
            return new Pair<>(null, e);
        }
    }

    @Override
    protected void onPostExecute(Pair<File, Exception> result) {
        super.onPostExecute(result);

        Intent resultIntent = new Intent();

        if (result.first != null) {
            Uri uri = Uri.fromFile(result.first);
            Log.e(TAG, "onPostExecute: "+ activityWeakReference.get());
            resultIntent.setData(uri);
            resultIntent.putExtra("CUTOUT_EXTRA_RESULT", uri);
            activityWeakReference.get().setResult(Activity.RESULT_OK, resultIntent);
            activityWeakReference.get().finish();

        } else {
            Intent intent = new Intent();
            intent.putExtra("exception", result.second);
            activityWeakReference.get().setResult(CUTOUT_ACTIVITY_RESULT_ERROR_CODE, intent);
            activityWeakReference.get().finish();


        }
    }
}
