package com.rockchip.gpadc.demo.yolo;
//import static org.opencv.videoio.Videoio.CAP_PROP_POS_FRAMES;

import android.graphics.RectF;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Core;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InferenceWrapper extends AppCompatActivity{
   //  TextView textview;
   // ImageView img_after;

    private final String  Model_name = "epoch120_quant.rknn";
    // private String fileDir = getCacheDir().getAbsolutePath();
    private final String platform = "rk3568";
    public static final int Input_size = 320;
    public static final int Input_chanmel = 3;
//    public static int camera_width = 640;
//    public static int camera_height = 640;
    public static final int RK_FORMAT_RGBA_8888 = (0x0 << 8);
    public static final int RK_FORMAT_RGB_888 = (0x2 << 8);
    public int flip = -1;

    public static int camera_width = 1280;
    public static int camera_height =720;

    public byte[] Grid0Out;
    public byte[] Grid1Out;
    public byte[] Grid2Out;

    public int[] ids;
    public float[] scores;
    public float[] boxes;
    public int flag = 0;
    public long time_out = 0;
    ArrayList<Object> recog = new ArrayList<>();
    //ArrayList  = new ArrayList<>();
    StringBuffer stringBuffer=new StringBuffer();

    private static final String TAG = "InferenceWrapper";

    // Used to load the 'helloworld' library on application startup.
    static {

        System.loadLibrary("rknnrt");
        System.loadLibrary("rga");
        System.loadLibrary("test");
        System.loadLibrary("rknn4j");
        // System.loadLibrary("opencv_java3");
        System.loadLibrary("RKinf");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //InputStream Model = getResources().openRawResource(R.raw.epoch120_quant);

        String fileDir = getCacheDir().getAbsolutePath();
        String Model = fileDir + "/" + "epoch120_quant.rknn";  //  Model_name;
        //String Model = "fail:///res/raw/epoch120.rknn";
        OpenCVLoader.initDebug();


        if (navite_init(camera_height, camera_width, Input_chanmel, Model) != 0) {
            try {
                throw new IOException("rknn init fail");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // String fileDir = getCacheDir().getAbsolutePath();

        String filename = fileDir + "/" + "1.jpg";  //  Model_name;
        Mat src1 = Imgcodecs.imread(filename);
        if (src1.empty()) return;
//        byte[] b = new byte[ src1.cols()*src1.rows()];
//        Mat img = new Mat(src1.rows(), src1.cols(), CV_8UC3, new BytePointer(b));

        //here song
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String videoname = fileDir + "/" + "test_V.MP4";  //  Model_name;
        VideoCapture cap = new VideoCapture(videoname);
        // boolean a = cap.isOpened();
        Mat frame = new Mat();
        TextView tv = findViewById(R.id.textView_txt);
        TextView textview = findViewById(R.id.textview);
        // long g = System.currentTimeMillis();
        while (cap.isOpened()){
            long stime = System.currentTimeMillis();
            cap.read(frame);
            flag = flag+1;
            if (!cap.read(frame))
                break;
        //double frameCount = cap.get(Videoio.CAP_PROP_FRAME_COUNT);

            //定义mask的区域边界点
            List<Point> list = new ArrayList<>();
            list.add(new Point(1000, 0));
            list.add(new Point(1000, 720));
            list.add(new Point(1280, 0));
            list.add(new Point(1280, 720));

            List<MatOfPoint> maskArea = new ArrayList<>();
            MatOfPoint maskPoints = new MatOfPoint();
            maskPoints.fromList(list);
            maskArea.add(maskPoints);
            Mat mask;
            mask = new Mat(new Size(frame.width(), frame.height()), CvType.CV_8UC3, new Scalar(255, 255, 255));//定义成白色
            Imgproc.fillPoly(mask, maskArea, new Scalar(0, 0, 0));//填充多边形,生成mask,定义成黑色
            Mat img = new Mat(new Size(frame.width(), frame.height()), CvType.CV_8UC3, new Scalar(0, 0, 0));
            frame.copyTo(img, mask);

            Mat src= new Mat();
            Mat src2 = new Mat();
            Imgproc.cvtColor(img,src,Imgproc.COLOR_BGR2RGBA);
            // Imgproc.resize(src,src2,new Size(720,1280));

//            Imgproc.resize(frame,src,new Size(320,320));
//            Imgproc.cvtColor(src,src2,Imgproc.COLOR_BGR2RGBA);
            byte[] ret1 = new byte[src.cols()*src.rows()*4];
            byte[] ret = new byte[src.cols()*src.rows()*4];
            src.get(0,0,ret1); //okk

//            color_convert(ret1, RK_FORMAT_RGB_888, ret, RK_FORMAT_RGBA_8888, camera_width, camera_height, this.flip);


//        Mat gray = new Mat();
//        Imgproc.cvtColor(src,gray,Imgproc.COLOR_BGR2GRAY);//灰度处理
//        byte [] ret = new byte[src.cols()*src.rows()*3];
//        byte [] ret1 = new byte[src.cols()*src.rows()];
//        byte [] ret2 = new byte[src.cols()*src.rows()];
//        byte [] ret3 = new byte[src.cols()*src.rows()];
//        List<Mat> imgList = new ArrayList<>();
//        Core.split(src,imgList);
//        imgList.get(0).get(0,0,ret1);
//        imgList.get(1).get(0,0,ret2);
//        imgList.get(2).get(0,0,ret3);
//        System.arraycopy(ret1,0,ret,0,ret1.length);
//        System.arraycopy(ret2,0,ret,ret1.length,ret1.length);
//        System.arraycopy(ret3,0,ret,ret1.length+ret1.length,ret1.length);
//        for (int i =0; i<3;++i){
//            imgList.get(i).get(0,0,ret);
//        }

            Grid0Out = new byte[255*80*80];
            Grid1Out = new byte[255*40*80];
            Grid2Out = new byte[255*20*80];

            ids = new int[64];
            scores = new float[64];
            boxes = new float[4 * 64];
//        text_view.setText(native_run(ret, Grid0Out, Grid1Out, Grid2Out));

//            TextView tv = findViewById(R.id.textView_txt);
//            TextView textview = findViewById(R.id.textview);
            /*R.id.stong中的stong是我自己在activity_main.xml命名的id，可以自己更改*/
            // tv.setText(test());
           // long stime = System.currentTimeMillis(); //
            // navite_process(camera_height, camera_width, Input_chanmel, Model, ret1, ids, scores, boxes,Grid0Out, Grid1Out, Grid2Out);
            native_run(ret1, Grid0Out, Grid1Out, Grid2Out);
            int count = native_post_process(Grid0Out, Grid1Out, Grid2Out,  ids, scores, boxes);

            // text_view.setText(count);

            for (int i = 0; i < count; ++i){
                RectF rect = new RectF();
                rect.left = boxes[i * 4];
                stringBuffer.append(rect.left);
                stringBuffer.append(" ");

                rect.top = boxes[i*4+1];
                stringBuffer.append(rect.top);
                stringBuffer.append(" ");

                rect.right = boxes[i*4+2];
                stringBuffer.append(rect.right);
                stringBuffer.append(" ");

                rect.bottom = boxes[i*4+3];
                stringBuffer.append(rect.bottom);
                stringBuffer.append(" ");

                recog.add(rect);
                stringBuffer.append("\n");
                // stringBuffer.append(rect);
            }
            long etime = System.currentTimeMillis();
            long time = etime - stime;
            time_out = time_out + time;



//
       }
        // long g1 = System.currentTimeMillis();
        time_out = time_out / flag;
        tv.setText(stringBuffer);
        textview.setText(Long.toString(time_out));

            cap.release();

    }


// end song


//        for(int i=0; i< boxes.length; ++i) {
//            tv.setText((int) boxes[i]);
//        }
   //}

    /**
     * A native method that is implemented by the 'helloworld' native library,
     * which is packaged with this application.
     */
    private native int navite_init(int im_height, int im_width, int im_channel, String modelpath);
    private native void native_deinit();
    private native int native_run(byte[] inData, byte[] grid0Out, byte[] grid1Out, byte[] grid2Out);
    private native int native_post_process(byte[] grid0Out, byte[] grid1Out, byte[] grid2Out, int[] ids, float[] scores, float[] boxes);
    //private native int native_
    private static native int color_convert(byte[]src,int srcFmt, byte[]dst,int dstFmt,int width,int height,int flip);
    private native int navite_process(int im_height, int im_width, int im_channel, String modelpath, byte[] indata, int[] ids, float[] scores, float[] boxes,byte[] grid0Out, byte[] grid1Out, byte[] grid2Out);
}
