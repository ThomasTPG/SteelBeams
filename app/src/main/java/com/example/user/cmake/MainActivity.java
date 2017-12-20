package com.example.user.cmake;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java3" );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        ImageView iv = (ImageView) findViewById(R.id.image_view);
        Mat m = new Mat();
        Bitmap rgbImage = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
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

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native String imageTest(long imageAddr);
}
