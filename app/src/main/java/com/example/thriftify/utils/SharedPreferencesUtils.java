package com.example.thriftify.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.io.IOException;
import java.security.GeneralSecurityException;

import androidx.annotation.RequiresApi;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

public class SharedPreferencesUtils {
    private static SharedPreferencesUtils sInstance = null;
    private SharedPreferences sharedPreferences = null;
    String sharedPrefsFile = "my_sensitive_data.text";


    public SharedPreferencesUtils(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            createAuthenticationEncryptSharePref(context);
            //createAuthenticationSharePref(context);
        }else{
            createAuthenticationSharePref(context);
        }
    }

    public static SharedPreferencesUtils get(Context context) {
        if (sInstance == null) {
            sInstance = new SharedPreferencesUtils(context);
        }
        return sInstance;
    }

    public static void setDefaults(String value, boolean b, Context mContext) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(value, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(value, b);
        editor.apply();
    }

    public static boolean getDefaults(String value, Context mContext) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(value,Context.MODE_PRIVATE);
        return sharedPref.getBoolean(value, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createAuthenticationEncryptSharePref(Context context) {


        try {

            // this is equivalent to using deprecated MasterKeys.AES256_GCM_SPEC
/*            KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
                    Constants.MASTER_KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(Constants.KEY_SIZE)
                    .build();

            MasterKey masterKey = new MasterKey.Builder(context).setKeyGenParameterSpec(spec).build();*/

            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            sharedPreferences = EncryptedSharedPreferences.create(sharedPrefsFile,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }
private void createAuthenticationSharePref(Context context){
    sharedPreferences = context.getSharedPreferences(sharedPrefsFile, Context.MODE_PRIVATE);

}





    public String getTokenSharedPreferences() {
        return this.sharedPreferences.getString("token", null);
    }

    public void setTokenSharedPreferences(String value) {
        sharedPreferences.edit()
                .putString("token", value)
                .apply();
    }


}
