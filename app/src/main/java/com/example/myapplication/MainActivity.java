//package com.example.myapplication;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//}

package com.example.myapplication;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
// import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.media.tv.AdRequest;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView img_after;
   // TextView text_togray;
    TextView text_view;
    Bitmap srcBitmap;
    Bitmap grayBitmap;

    private static boolean flag = true;
    //private static boolean isFirst = true;
    private static final String TAG = "MainActivity";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //img_after=(ImageView)findViewById(R.id.imageView_after);
        text_view=(TextView)findViewById(R.id.textView_txt);
       // text_togray=(TextView)findViewById(R.id.textView_togray);
       // text_togray.setOnClickListener(this);

    }
    //OpenCV库加载并初始化成功后的回调函数
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {

        @Override
        public void onManagerConnected(int status) {
            // TODO Auto-generated method stub
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                    Log.i(TAG, "成功加载");
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "成功加载！", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    break;
                default:
                    super.onManagerConnected(status);
                    Log.i(TAG, "加载失败");
                    Toast toast1 = Toast.makeText(getApplicationContext(),
                            "加载失败！", Toast.LENGTH_LONG);
                    toast1.setGravity(Gravity.CENTER, 0, 0);
                    toast1.show();
                    break;
            }

        }
    };

    public void procSrc2Gray(){
        Mat rgbMat = new Mat();
        Mat grayMat = new Mat();
        srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.girl);
        grayBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.RGB_565);
        Utils.bitmapToMat(srcBitmap, rgbMat);//convert original bitmap to Mat, R G B.
        Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);//rgbMat to gray grayMat
        Utils.matToBitmap(grayMat, grayBitmap); //convert mat to bitmap
        Log.i(TAG, "procSrc2Gray sucess...");
    }

    private void CannyScan(){
        //边缘检测
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.girl);
        Mat src = new Mat();
        Utils.bitmapToMat(bitmap, src);

        Mat gray = new Mat();
        Imgproc.cvtColor(src,gray,Imgproc.COLOR_BGR2GRAY);//灰度处理
        Mat ret = src.clone();
        // image  输入图像，必须是CV_8U的单通道或者三通道图像。
        // edges  输出图像，与输入图像具有相同尺寸的单通道图像，且数据类型为CV_8U。
        // threshold1  第一个滞后阈值。
        // threshold2  第二个滞后阈值。
        Imgproc.Canny(src, ret, 75, 200);
        Utils.matToBitmap(ret, bitmap);

        img_after.setImageBitmap(bitmap); //
        src.release();
        gray.release();
        ret.release();
    }

    private void Detection(){

       // String filename = Environment.getExternalStorageDirectory() + "/" + "Download/bus.png";
        String fileDir = getCacheDir().getAbsolutePath();
        String filename = fileDir+ "/"+"bus.png";  //  Model_name;
        Mat src = Imgcodecs.imread(filename);
        if(src.empty()) return;

        Mat srcdec = new Mat();
        Imgproc.cvtColor(src, srcdec, Imgproc.COLOR_BGR2GRAY);

        String filesave = "/storage/emulated/0/Download/busguo.jpg";
        Imgcodecs.imwrite(filesave, srcdec);
    }

    private void text() throws IOException {
//        InputStream input= getResources() .openRawResource(R.a)
        InputStream input=getAssets().open("list.txt");
        Reader reader=new InputStreamReader(input);
        StringBuffer stringBuffer=new StringBuffer();
        char b[]=new char[1024];
        int len=-1;
        try {
            while((len = reader.read(b))!=-1){
                stringBuffer.append(b);
            }
        }catch (IOException e){
            Log.e("ReadingFile","IOException");
        }
    text_view.setText(stringBuffer);
    // String string=stringBuffer.toString();
    }

//   public void readJson() throws IOException {
//
//        InputStreamReader isr = new InputStreamReader(getAssets().open("data.json"));
//        BufferedReader br  = new BufferedReader(isr);
//        String line;
//        StringBuffer builder = new StringBuffer();
//        try {
//        while ((line = br.readLine()) != null) {
//           builder.append(line);
//            }
//        br.close();
//        isr.close();
//        } catch (IOException e) {
//            Log.e("ReadingFile","IOException");
//        }
//   text_view.setText(builder);
//    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
          //  case R.id.textView_togray:
//                procSrc2Gray();
//                img_after.setImageBitmap(grayBitmap) ;
//                 CannyScan();
//                try {
//                    text();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
               // Detection();
               // break;
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }
}

