package wang.chunyu.me.testfacedetection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;


/**
 * 检测人脸的控件
 * <p/>
 * Created by wangchenlong on 15/12/15.
 */
public class FacesDisplayView extends View {

    private static final String TAG = "DEBUG-WCL: " + FacesDisplayView.class.getSimpleName();

    private Bitmap mBitmap; // 图片
    private SparseArray<Face> mFaces; // 人脸数组

    // 确保图片居中
    private int mHorizonOffset; // 水平偏移
    private int mVerticalOffset; // 竖直偏移

    public FacesDisplayView(Context context) {
        this(context, null);
    }

    public FacesDisplayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FacesDisplayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 设置显示图片
    @SuppressWarnings("unused")
    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        FaceDetector detector = new FaceDetector.Builder(getContext())
                .setTrackingEnabled(true)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.ACCURATE_MODE)
                .build();

        if (!detector.isOperational()) {
            Log.e(TAG, "加载失败");
            return;
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            mFaces = detector.detect(frame);
            detector.release();
        }

        logFaceData(); // 打印人脸数据
        invalidate();  // 填充
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if ((mBitmap != null) && (mFaces != null)) {
            double scale = drawBitmap(canvas);
            drawFaceBox(canvas, scale);
            drawFaceLandmarks(canvas, scale);
        }
    }

    // 绘制图片, 返回缩放概率
    private double drawBitmap(Canvas canvas) {
        double viewWidth = canvas.getWidth(); // 显示宽度
        double viewHeight = canvas.getHeight(); // 显示高度
        double imageWidth = mBitmap.getWidth(); // 图片宽度
        double imageHeight = mBitmap.getHeight(); // 图片高度

        double wScale = viewWidth / imageWidth;
        double hScale = viewHeight / imageHeight;

        double scale;
        Rect destBounds;

        // 水平竖直缩放
        if (wScale > hScale) {
            mHorizonOffset = (int) ((viewWidth - imageWidth * hScale) / 2.0f);
            destBounds = new Rect(mHorizonOffset, 0,
                    (int) (imageWidth * hScale) + mHorizonOffset, (int) (imageHeight * hScale));
            scale = hScale;
        } else {
            mVerticalOffset = (int) ((viewHeight - imageHeight * wScale) / 2.0f);
            destBounds = new Rect(0, mVerticalOffset,
                    (int) (imageWidth * wScale), (int) (imageHeight * wScale) + mVerticalOffset);
            scale = wScale;
        }

        canvas.drawBitmap(mBitmap, null, destBounds, null); // 添加图片

        return scale;
    }

    // 绘制脸部方形
    private void drawFaceBox(Canvas canvas, double scale) {

        // 画笔
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        float left;
        float top;
        float right;
        float bottom;

        // 绘制每张脸
        for (int i = 0; i < mFaces.size(); i++) {
            Face face = mFaces.valueAt(i);

            left = (float) (face.getPosition().x * scale);
            top = (float) (face.getPosition().y * scale);
            right = (float) scale * (face.getPosition().x + face.getWidth());
            bottom = (float) scale * (face.getPosition().y + face.getHeight());

            canvas.drawRect(left + mHorizonOffset, top + mVerticalOffset,
                    right + mHorizonOffset, bottom + mVerticalOffset, paint);
        }
    }

    // 绘制脸部关键部位
    private void drawFaceLandmarks(Canvas canvas, double scale) {
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        for (int i = 0; i < mFaces.size(); i++) {
            Face face = mFaces.valueAt(i);

            for (Landmark landmark : face.getLandmarks()) {
                int cx = (int) (landmark.getPosition().x * scale);
                int cy = (int) (landmark.getPosition().y * scale);
                canvas.drawCircle(cx + mHorizonOffset, cy + mVerticalOffset, 10, paint);
            }

        }
    }

    // 输出脸部数据
    private void logFaceData() {

        float smilingProbability;
        float leftEyeOpenProbability;
        float rightEyeOpenProbability;
        float eulerY;
        float eulerZ;

        for (int i = 0; i < mFaces.size(); i++) {
            Face face = mFaces.valueAt(i);

            // 可能性
            smilingProbability = face.getIsSmilingProbability();
            leftEyeOpenProbability = face.getIsLeftEyeOpenProbability();
            rightEyeOpenProbability = face.getIsRightEyeOpenProbability();

            eulerY = face.getEulerY(); // 竖直轴偏移
            eulerZ = face.getEulerZ(); // 前后偏移

            Log.e(TAG, "脸数: " + i);
            Log.e(TAG, "微笑概率: " + smilingProbability);
            Log.e(TAG, "左眼睁开概率: " + leftEyeOpenProbability);
            Log.e(TAG, "右眼睁开概率: " + rightEyeOpenProbability);
            Log.e(TAG, "竖直轴偏移: " + eulerY);
            Log.e(TAG, "前后偏移: " + eulerZ);
            Log.e(TAG, "--------------------");
        }
    }
}
