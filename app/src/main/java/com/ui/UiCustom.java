package com.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Random;

/**
 * @author: xiaxueyi
 * @date: 2017-10-29
 * @time: 10:03
 * @说明:
 */
public class UiCustom extends View {

    private Context mContext;

    private Paint mPaintCircle;  //花圆的画笔

    private Paint mPaintText;   //花园字体大小的画笔

    private Paint mPaintLine;   //花线的画笔

    private int mCircleR=0; //求得半径

    private int mViewWidth=0;   //绘制View的宽度

    private int mViewHeight=0;  //绘制View的高度

    private int dWidth,dHeight,bCount = 5,bHeight=50;//贝塞尔曲线的一些决定量，可以自己设置，我不太会用

    private Random random;//产生随机数，加速球的波浪效果

    private Path path;//

    private final int maxProgress = 100;//当前的进度

    private int progress=0;

    private Bitmap bitmap;//位图

    private Canvas bitmapCanvas;//位图画板

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();//实时更新进度
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public UiCustom(Context context) {
        super(context);
        this.mContext=context;
        init();

    }

    public UiCustom(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        init();
    }

    public UiCustom(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        init();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UiCustom(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext=context;

        init();

    }

    /**
     * 初始化组件
     */
    private void init(){
        /**
         * 初始化园笔
         */
        mPaintCircle=new Paint();
        mPaintCircle.setColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
        mPaintCircle.setStrokeWidth(1);
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setStyle(Paint.Style.FILL);

        /**
         * 初始化画笔
         */
        mPaintText=new Paint();
        mPaintText.setColor(ContextCompat.getColor(mContext,R.color.colorAccent));
        mPaintText.setTextSize(50f);
        mPaintText.setAntiAlias(true);
        mPaintText.setTextAlign(Paint.Align.CENTER);

        /**
         * 初始化贝塞尔曲线
         */
        mPaintLine=new Paint();
        mPaintLine.setStyle(Paint.Style.FILL);
        mPaintLine.setColor(Color.argb(0xff,0x4e,0xc8,0x63));
        mPaintLine.setStrokeWidth(1);
        mPaintLine.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        /**
         *
         */
        path=new Path();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth=this.getMeasuredWidth(); //得到测量的宽度
        mViewHeight=this.getMeasuredHeight();   //得到测量的高度
        setMeasuredDimension(mViewWidth,mViewHeight);   //设置测量的尺寸m
        mCircleR=mViewWidth/2;  //设置园的半径
        dHeight = mViewWidth/5;
        dWidth = mViewHeight/5;

        bitmap=Bitmap.createBitmap(mViewWidth,mViewHeight, Bitmap.Config.ARGB_8888);    //设置图片大小
        bitmapCanvas=new Canvas(bitmap);    //绘制在画布上
        random=new Random();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bitmapCanvas.drawCircle(mViewWidth/2,mViewHeight/2,mCircleR,mPaintCircle);//绘制圆
        //通过Path绘制贝塞尔曲线，此处贝塞尔曲线不是太懂，所以画的东西不是太好，望高手指点
        path.reset();//清除path内容
        path.moveTo(0, mViewHeight);
        path.lineTo(0, ((float) maxProgress-progress)/100*mViewHeight);//此处若在定义时若是int,则必须强制转换，否则结果为零，聪明的你一定能理解。
        for (int i = 0; i < bCount; i++) {
            path.rQuadTo(random.nextFloat()*bHeight, random.nextFloat()*dWidth/2,random.nextFloat()*dWidth, 0);
//            path.rQuadTo(20, -5, 40, 0);//可以是这样的固定值
        }
        path.lineTo(mViewWidth, ((float) maxProgress-progress)/100*((float)mViewHeight));
        Log.e("(maxProgrerogressheight","----------------"+((float) maxProgress-progress)/100*mViewHeight);
        path.lineTo(mViewWidth, mViewHeight);
        path.close();
        //将贝塞尔曲线绘制到Bitmap的Canvas上
        bitmapCanvas.drawPath(path, mPaintLine);
        //将Bitmap绘制到View的Canvas上
        bitmapCanvas.drawText(progress * 100f / maxProgress + "%", mViewWidth / 2, mViewHeight / 2, mPaintText);
        canvas.drawBitmap(bitmap, 0, 0, null);
    }
}
