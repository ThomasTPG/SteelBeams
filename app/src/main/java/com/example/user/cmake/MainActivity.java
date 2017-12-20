package com.example.user.cmake;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.Mat;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    final static int PICK_PHOTO_FROM_GALLERY = 1;
    final static int TAKE_CAMERA_PHOTO = 2;
    Bitmap rgbImage;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java3" );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupButtons();
    }

    private void setupButtons()
    {
        Button gallery = (Button) findViewById(R.id.gallery_button);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGalleryButtonPress();
            }
        });


        Button camera = (Button) findViewById(R.id.camera_button);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCameraPress();
            }
        });

        Button start = (Button) findViewById(R.id.start_button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartPress();
            }
        });
    }

    private void onStartPress()
    {
        ImageView iv = (ImageView) findViewById(R.id.image_view_final);
        Mat m = new Mat();
        Utils.bitmapToMat(rgbImage, m);
        imageTest(m.getNativeObjAddr());
        Bitmap bmp = null;
        try {
            bmp = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(m, bmp);
        }
        catch (CvException e){}
        iv.setImageBitmap(bmp);
    }

    private void onGalleryButtonPress() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FROM_GALLERY);
    }

    private void onCameraPress() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, TAKE_CAMERA_PHOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                rgbImage = BitmapFactory.decodeStream(inputStream);
            }
            catch (Exception e)
            {

            }
        }

        if (requestCode == TAKE_CAMERA_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            rgbImage = (Bitmap) extras.get("data");
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native String imageTest(long imageAddr);
}
