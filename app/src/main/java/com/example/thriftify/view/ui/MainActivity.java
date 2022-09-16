package com.example.thriftify.view.ui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.example.thriftify.R;
import com.example.thriftify.service.respository.ProductDetailRepository;
import com.example.thriftify.utils.ImageUtils;
import com.example.thriftify.utils.SharedPreferencesUtils;
import com.example.thriftify.viewmodel.ProductDetailViewModel;
import com.example.thriftify.viewmodel.SingletonViewModelFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    // tags
    private static final String TAG = "MainActivity";

    // constant
    public static final short CUTOUT_ACTIVITY_RESULT_ERROR_CODE = 3680;
    private static final int ACTIVITY_NUM = 1;
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    //Variables
    private final Context mContext = this;
    private ProductDetailViewModel model;
    @Inject
    ProductDetailRepository repository;
    private NavHostFragment navHostFragment;
    private NavController navController;
    private BottomNavigationView bottomNav;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        if(savedInstanceState == null){
            //Add Product Detail ViewModel
            this.model = new ViewModelProvider(this, SingletonViewModelFactory.getInstance(this.repository)).get(ProductDetailViewModel.class);
            setupBottomNavigationBar();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(MainActivity.TAG, "onStart: Started");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(MainActivity.TAG, "onResume: Resumed");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(MainActivity.TAG, "onRestoreInstanceState: Restored");
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar();
    }

    @Override
    protected void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(MainActivity.TAG, "onSaveInstanceState: Backup");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(MainActivity.TAG, "onPause: Paused");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(MainActivity.TAG, "onStop: Stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(MainActivity.TAG, "onDestroy: Destroyed");
    }

    /**
     * Called on first creation and when restoring state.
     */
    private void setupBottomNavigationBar() {
        navHostFragment = (NavHostFragment) this.getSupportFragmentManager().findFragmentById(R.id.nav_host_container);
        navController = navHostFragment.getNavController();
        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph);

        if (SharedPreferencesUtils.get(this).getTokenSharedPreferences() == null){
            navGraph.setStartDestination(R.id.authenticate);
        }else{
            navGraph.setStartDestination(R.id.dashboard);
        }
        navController.setGraph(navGraph);
        bottomNav = findViewById(R.id.bottomNavViewBar);
        bottomNav.setVisibility(View.INVISIBLE);
        NavigationUI.setupWithNavController(bottomNav, navController);



        // attached the destination changed listener with navController
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.lanuchFragment) {
                    bottomNav.setVisibility(View.GONE);
                }
                else if (destination.getId() == R.id.loginFragment){
                    bottomNav.setVisibility(View.GONE);
                }
                else{
                    bottomNav.setVisibility(View.VISIBLE);
                }
            }
        });
    }



    //provides up back navigation support
   @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }



    @Override
    public void onBackPressed() {

        if(navController.getCurrentDestination().getId() == R.id.dashboardFragment){
            this.moveTaskToBack(true);
        }
        else{
            super.onBackPressed();
        }
    }



    // this callback called when get the result from editor activity
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {

            switch (resultCode) {
                case Activity.RESULT_CANCELED:
                    this.model.newFile.delete();
                    break;
                case Activity.RESULT_OK:
                    final Uri imageUri = data.getData();
                    // Save the image using the returned Uri here
                    Log.e(MainActivity.TAG, "onActivityResult: URI " + imageUri);
                    try {
                        final Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        ImageUtils.SaveImage(mImageBitmap, this.model.newFile);
                        this.model.imageBitmap.setValue(mImageBitmap);
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case MainActivity.CUTOUT_ACTIVITY_RESULT_ERROR_CODE:
                    final Exception ex = data != null ? (Exception) data.getSerializableExtra("exception") : null;
                    break;
                default:
                    System.out.print("User cancelled the CutOut screen");
            }
        }else if(requestCode == 500){
            switch (resultCode){
                case Activity.RESULT_OK:
                    Log.e(MainActivity.TAG, "onActivityResult: success");
                    break;
            }
        }else{

        }
    }


}