package com.example.thriftify.view.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.alexvasilkov.gestures.views.interfaces.GestureView;
import com.example.thriftify.R;
import com.example.thriftify.utils.DrawView;
import com.example.thriftify.utils.GlideBitmapPool;
import com.example.thriftify.utils.ImageUtils;
import com.example.thriftify.utils.SaveDrawingTask;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class EditorActivity extends AppCompatActivity {
    private static final String TAG = "EditorActivity";

    private static final int INTRO_REQUEST_CODE = 4;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;
    private static final int IMAGE_CHOOSER_REQUEST_CODE = 2;
    private static final int CAMERA_REQUEST_CODE = 3;

    private static final String INTRO_SHOWN = "INTRO_SHOWN";
    private static final int COLOR_TOLERANCE = 20;
    private static final int CUTOUT_ACTIVITY_RESULT_ERROR_CODE = 200;
    FrameLayout loadingModal;
    private GestureView gestureView;
    private DrawView drawView;
    private LinearLayout manualClearSettingsLayout;
 /*   private static int maxMemory;
    private static Set<Bitmap.Config> configSet;*/

    private static final short MAX_ERASER_SIZE = 150;
    private static final short BORDER_SIZE = 45;
    private static final float MAX_ZOOM = 4F;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editor);
       /* maxMemory= (int) (Runtime.getRuntime().maxMemory() / (1024*1024));
        Log.e(TAG, "getMemorySize: "+ maxMemory);
        configSet = new HashSet<Bitmap.Config>(Arrays.asList(Bitmap.Config.values()));
        if(GlideBitmapPool.getInstance(maxMemory,configSet) == null){
            GlideBitmapPool.initialize(maxMemory,configSet);
        }*/


        Toolbar myToolbar = findViewById(R.id.photo_edit_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        FrameLayout drawViewLayout = findViewById(R.id.drawViewLayout);

        int sdk = android.os.Build.VERSION.SDK_INT;

        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            drawViewLayout.setBackgroundColor(Color.WHITE);
        } else {
            drawViewLayout.setBackgroundColor(Color.WHITE);
        }

        SeekBar strokeBar = findViewById(R.id.strokeBar);
        strokeBar.setMax(MAX_ERASER_SIZE);
        strokeBar.setProgress(50);

        gestureView = findViewById(R.id.gestureView);

        drawView = findViewById(R.id.drawView);
        drawView.setDrawingCacheEnabled(true);
        drawView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        drawView.setStrokeWidth(strokeBar.getProgress());
        drawView.setContext(this);

        strokeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                drawView.setStrokeWidth(seekBar.getProgress());
            }
        });
        loadingModal = findViewById(R.id.loadingModal);
        loadingModal.setVisibility(INVISIBLE);



        manualClearSettingsLayout = findViewById(R.id.manual_clear_settings_layout);

        initializeActionButtons();



        drawView.setLoadingModal(loadingModal);

        setDrawViewBitmap(getIntent().getData());
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editor_activity_menu, menu);
        MenuItem undoMenuItem = menu.findItem(R.id.app_bar_undo).setEnabled(false);
        MenuItem redoMenuItem = menu.findItem(R.id.app_bar_redo).setEnabled(false);
        drawView.setMenuButtons(undoMenuItem,redoMenuItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Handle item selection */
        final int itemId = item.getItemId();
        if(android.R.id.home == itemId){
            setResult(Activity.RESULT_CANCELED);
            finish();
            return true;
        }
        if (R.id.app_bar_undo == itemId) {
            this.undo();
            return true;
        }
        if (itemId == R.id.app_bar_redo) {
            this.redo();
            return true;
        }
        if (itemId == R.id.app_bar_done) {
            if(drawView.getbitmapPoolingStatus()){
                //  GlideBitmapPool.clearMemory();
                //  this.undo();
                //  this.undo();
                //  this.undo();
            }
            this.startSaveDrawingTask();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
/*    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {

    }*/

    private void startSaveDrawingTask() {
        SaveDrawingTask task = new SaveDrawingTask(this);
        int borderColor;
        if ((borderColor = getIntent().getIntExtra("CUTOUT_EXTRA_BORDER_COLOR", -1)) != -1) {
            Bitmap image = ImageUtils.getBorderedBitmap(this.drawView.getDrawingCache(), borderColor, BORDER_SIZE);
            task.execute(image);
        } else {
            drawView.clearStacks();
            task.execute(whiteBackground(this.drawView.getDrawingCache(true)));
        }
    }
    public Bitmap whiteBackground(Bitmap oldBitmap){

        int width = oldBitmap.getWidth();
        int height = oldBitmap.getHeight();
        Log.i(TAG, "doInBackground: " + String.valueOf(width));
        int[] pixels = new int[width * height];
        oldBitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int rA = Color.alpha(0);
        int rR = Color.red(0);
        int rG = Color.green(0);
        int rB = Color.blue(0);

        int pixel;

        // iteration through pixels
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                // get current index in 2D-matrix
                int index = y * width + x;

                pixel = pixels[index];
                int rrA = Color.alpha(pixel);
                int rrR = Color.red(pixel);
                int rrG = Color.green(pixel);
                int rrB = Color.blue(pixel);

                if (rA - COLOR_TOLERANCE < rrA && rrA < rA + COLOR_TOLERANCE && rR - COLOR_TOLERANCE < rrR && rrR < rR + COLOR_TOLERANCE && rG - COLOR_TOLERANCE < rrG && rrG < rG + COLOR_TOLERANCE && rB - COLOR_TOLERANCE < rrB && rrB < rB + COLOR_TOLERANCE) {
                    pixels[index] = Color.WHITE;
                }
            }
        }
        oldBitmap.setPixels(pixels,0,width,0,0,width,height);
        //  Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //  newBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return oldBitmap;
    }

    private void activateGestureView() {
        gestureView.getController().getSettings()
                .setMaxZoom(MAX_ZOOM)
                .setDoubleTapZoom(-1f) // Falls back to max zoom level
                .setPanEnabled(true)
                .setZoomEnabled(true)
                .setDoubleTapEnabled(true)
                .setOverscrollDistance(0f, 0f)
                .setOverzoomFactor(2f);
    }

    private void deactivateGestureView() {
        gestureView.getController().getSettings()
                .setPanEnabled(false)
                .setZoomEnabled(false)
                .setDoubleTapEnabled(false);
    }

    private void initializeActionButtons() {
        Button autoClearButton = findViewById(R.id.auto_clear_button);
        Button manualClearButton = findViewById(R.id.manual_clear_button);
        Button zoomButton = findViewById(R.id.zoom_button);

        autoClearButton.setActivated(false);
        autoClearButton.setOnClickListener((buttonView) -> {
            if (!autoClearButton.isActivated()) {
                drawView.setAction(DrawView.DrawViewAction.AUTO_CLEAR);
                manualClearSettingsLayout.setVisibility(INVISIBLE);
                autoClearButton.setActivated(true);
                manualClearButton.setActivated(false);
                zoomButton.setActivated(false);
                deactivateGestureView();
            }
        });

        manualClearButton.setActivated(true);
        drawView.setAction(DrawView.DrawViewAction.MANUAL_CLEAR);
        manualClearButton.setOnClickListener((buttonView) -> {
            if (!manualClearButton.isActivated()) {
                drawView.setAction(DrawView.DrawViewAction.MANUAL_CLEAR);
                manualClearSettingsLayout.setVisibility(VISIBLE);
                manualClearButton.setActivated(true);
                autoClearButton.setActivated(false);
                zoomButton.setActivated(false);
                deactivateGestureView();
            }

        });

        zoomButton.setActivated(false);
        deactivateGestureView();
        zoomButton.setOnClickListener((buttonView) -> {
            if (!zoomButton.isActivated()) {
                drawView.setAction(DrawView.DrawViewAction.ZOOM);
                manualClearSettingsLayout.setVisibility(INVISIBLE);
                zoomButton.setActivated(true);
                manualClearButton.setActivated(false);
                autoClearButton.setActivated(false);
                activateGestureView();
            }

        });
    }

    private void setDrawViewBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(getIntent().getStringExtra("camera").equalsIgnoreCase("imagetaken")){

            try {
                drawView.setBitmap(ImageUtils.rotateImageIfRequired(this,bitmap,uri));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            drawView.setBitmap(bitmap);
        }

    }


    private void undo() {
        drawView.undo();
    }

    private void redo() {
        drawView.redo();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        drawView.clearStacks();
        GlideBitmapPool.clearMemory();
    }
}
