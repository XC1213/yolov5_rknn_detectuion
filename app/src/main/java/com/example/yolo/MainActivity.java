package com.example.yolo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


public class MainActivity extends AppCompatActivity {
    TextView text_view;

    private String Model = "res/raw/epoch120_quant.rknn";
    private String platform = "rk3568";
    public static final int Input_size = 320;
    public static final int Input_chanmel = 3;
    public static int camera_width = 673;
    public static int camera_height = 412;

    public byte[] Grid0Out;
    public byte[] Grid1Out;
    public byte[] Grid2Out;

    // Used to load the 'helloworld' library on application startup.
    static {
        System.loadLibrary("rknnrt");
        System.loadLibrary("rga");
        System.loadLibrary("rknn4j");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
            native_init(camera_height, camera_width, Input_chanmel, Model);
        } catch (Exception e ){
            e.printStackTrace();
            System.exit(1);
        }

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.girl);
        Mat src = new Mat();
        Utils.bitmapToMat(bitmap, src);
        byte [] ret = new byte[src.cols()*src.rows()];

        Grid0Out = new byte[255*80*80];
        Grid1Out = new byte[255*40*80];
        Grid2Out = new byte[255*20*80];

        text_view.setText(native_run(ret, Grid0Out, Grid1Out, Grid2Out));

//        TextView tv = findViewById(R.id.textView_txt);
//        /*R.id.stong中的stong是我自己在activity_main.xml命名的id，可以自己更改*/
//        tv.setText(test());
    }
    /**
     * A native method that is implemented by the 'helloworld' native library,
     * which is packaged with this application.
     */
    private native int native_init(int im_height, int im_width, int im_channel, String modelpath);
    private native void native_deinit();
    private native int native_run(byte[] inData, byte[] grid0Out, byte[] grid1Out, byte[] grid2Out);
    private native int native_post_process(byte[] grid0Out, byte[] grid1Out, byte[] grid2Out, int[] ids, float[] scores, float[] boxes);
}
