package com.example.thriftify.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
public class PermissionUtility {
    private static final String TAG = "PermissionUtility";
    private Context context;
    private String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };
    private static final String CAMERA = "CameraPermission";
    public static boolean permanentDenied = false;
    public static PermissionUtility mInstance = null;

    public static PermissionUtility getInstance(Context context){
        if(mInstance == null){
            mInstance =new PermissionUtility(context);
        }
        return mInstance;
    }

    private PermissionUtility(Context context) {
        this.context = context;
    }
    public boolean arePermissionsEnabled(){
        for(String permission : PERMISSIONS){
            if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
    public void requestMultiplePermissions(){
        List<String> remainingPermissions = new ArrayList<>();
        for (String permission : PERMISSIONS) {
            Log.e(TAG, "requestMultiplePermissions: "+ permission );
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "requestMultiplePermissions: "+ permission );
                remainingPermissions.add(permission);
            }
        }
        ActivityCompat.requestPermissions((Activity) context, remainingPermissions.toArray(new String[remainingPermissions.size()]), 101);
    }

    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == 101){
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    permanentDenied = false;
                    //return false;
                }else{
                    //Permission Requests were denied.
                    if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permissions[i])){
                        permanentDenied = false;
                    }else{
                        permanentDenied = true;

                    }
                }
            }
        }
        return permanentDenied;
    }
}
