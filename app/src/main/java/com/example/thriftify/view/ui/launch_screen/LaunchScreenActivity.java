package com.example.thriftify.view.ui.launch_screen;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.thriftify.R;
import com.example.thriftify.utils.SharedPreferencesUtils;
import com.example.thriftify.view.ui.MainActivity;
import com.example.thriftify.view.ui.user_authentication.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;


public class LaunchScreenActivity extends AppCompatActivity {
    // tags
    private static final String TAG = "LaunchScreenActivity";
    //Variables
    private Context mContext = this;
    Animation animation;
    //views
    TextView logo_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.activity_launch_screen);

        //get the animations
        animation = AnimationUtils.loadAnimation(this, R.anim.top_splash_animation);

        // get the view and hooks
        logo_text = findViewById(R.id.logo_textView);
        logo_text.setAnimation(animation);

        // adding the handler thread to set up the post delay for required time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(SharedPreferencesUtils.get(mContext).getTokenSharedPreferences() == null){
                    // This method will be executed once the timer is over
                    Intent i = new Intent(LaunchScreenActivity.this, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
     /*             When you have share multiples elements
                    Pair[] pairs = new Pair[1];
                    pairs[0] = new Pair<View, String>(logo_text, "logo_text");*/
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(LaunchScreenActivity.this,logo_text, logo_text.getTransitionName()).toBundle();
                    LaunchScreenActivity.this.startActivity(i, bundle);
                    LaunchScreenActivity.this.finishAfterTransition();
                }else{
                    Log.e(TAG, "run: "+ SharedPreferencesUtils.get(mContext).getTokenSharedPreferences() );

                    Intent i = new Intent(LaunchScreenActivity.this, MainActivity.class);
                    LaunchScreenActivity.this.startActivity(i);
                    LaunchScreenActivity.this.finish();
                }
            }
        }, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}