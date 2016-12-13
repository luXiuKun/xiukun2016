package com.fangzhurapp.technicianport.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import com.fangzhurapp.technicianport.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 2016/9/19.
 *
 * 自定义饼图---lxk
 */
public class LxkCircleView extends View {

    private int mRadius;
    private int mCircleCenter;
    private int mPadding;
    private Paint mPaint;
    private RectF mRectF;


    private volatile float mStartAngle = 0f;
    private int mItemCount = 6;



    private int[] mColors = new int[]{
            new Color().rgb(157,204,251),
            new Color().rgb(190,187,255),
            new Color().rgb(156,205,205),
            new Color().rgb(254,197,75),
            new Color().rgb(255,163,163),
            new Color().rgb(117,237,223)};
    private String[] circleText = new String[]{
            "支  付  宝",
            "微  信",
            "会  员  消  费",
            "现  金  收  入",
            "团  购  消  费",
            "P  O  S"};


    private Paint mTextPaint;
    private Paint mHolePaint;
    private Paint mTransparentPaint;
    private Paint mHoleTextPaint;
    private Paint mYYEPaint;
    private float mTextSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 15, getResources().getDisplayMetrics());

    private float mContentSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 17, getResources().getDisplayMetrics());

    /**
     * 检测按下到抬起时旋转的角度
     */
    private float mTmpAngle;

    /**
     * 内圆的数据
     */
    private String mHoleText = "0";

    /**
     * 外圆上的数据
     */
    private List<String> mDatas;
    /**
     * 计算时间差，用于判断触摸还是点击
     */
    private long downTime;
    private onHoleClickListener mHoleClick;

    private Region[] mRegion = new Region[6];
    private onArcSelection mArcClick;

    /**
     * 防止色块和内圆重叠部分 点击事件冲突
     */
    private Boolean mClick = true;


    public LxkCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        initPaint();

        mDatas = new ArrayList<>();
        mDatas.add("0");
        mDatas.add("0");
        mDatas.add("0");
        mDatas.add("0");
        mDatas.add("0");
        mDatas.add("0");
    }

    public void setHoleText(String  s){
        this.mHoleText = s;
    }

    public void setData(List<String> list){
        this.mDatas = list;
        invalidate();
    }

    private void initPaint() {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(0xFFffffff);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);

        mHolePaint = new Paint();
        mHolePaint.setAntiAlias(true);
        mHolePaint.setDither(true);
        mHolePaint.setColor(Color.WHITE);

        mTransparentPaint = new Paint();
        mTransparentPaint.setAntiAlias(true);
        mTransparentPaint.setDither(true);
        mTransparentPaint.setColor(Color.WHITE);
        mTransparentPaint.setAlpha(100);


        mHoleTextPaint = new Paint();
        mHoleTextPaint.setAntiAlias(true);
        mHoleTextPaint.setDither(true);
        mHoleTextPaint.setColor(Color.BLACK);
        mHoleTextPaint.setTextSize(mTextSize);

        mYYEPaint = new Paint();
        mYYEPaint.setAntiAlias(true);
        mYYEPaint.setDither(true);
        mYYEPaint.setColor(getResources().getColor(R.color.tab_bg));
        mYYEPaint.setTextSize(mContentSize);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        //圆的直径
        mRadius = width - getPaddingLeft() - getPaddingRight();

        //圆的中心点
        mCircleCenter = width /2;

        mPadding = getPaddingLeft();
        mRectF = new RectF(getPaddingLeft(), getPaddingLeft(), getPaddingLeft() + mRadius,
                getPaddingLeft() + mRadius);

        setMeasuredDimension(width,width);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        try {

            if (canvas != null){

                float tempAngle = mStartAngle;
                float sweepAngle = (float) 360 / mItemCount;

                for (int i = 0; i <mItemCount; i++){


                    if (mColors.length == mDatas.size()){
                        mPaint.setColor(mColors[i]);
                        canvas.drawArc(mRectF,tempAngle,sweepAngle,true,mPaint);
                        drawText(canvas,tempAngle,sweepAngle,mDatas.get(i));
                        drawContent(canvas,tempAngle,sweepAngle,circleText[i]);
                        tempAngle+=sweepAngle;

                    }else{

                        throw  new IllegalArgumentException("缺少数据");
                    }


                }

                canvas.drawCircle(mCircleCenter,mCircleCenter,mRadius/5,mHolePaint);
                canvas.drawCircle(mCircleCenter,mCircleCenter,mRadius/4.5f,mTransparentPaint);
                float textWidth = mHoleTextPaint.measureText(mHoleText);
                canvas.drawText(mHoleText,mCircleCenter -textWidth/2,mCircleCenter -20,mHoleTextPaint);


                float v = mYYEPaint.measureText("营业额");
                canvas.drawText("营业额",mCircleCenter -v/2,mCircleCenter +40,mYYEPaint);

            }
        }catch(Exception e){
            e.printStackTrace();
        }


        super.onDraw(canvas);


    }

   /* protected float getStartAngle(){

        return mStartAngle;
    }*/







    private void drawContent(Canvas canvas,float tempAngle, float sweepAngle, String s) {

        Path path = new Path();
        path.addArc(mRectF,tempAngle,sweepAngle);
        mTextPaint.setTextSize(mContentSize);
        float textWidth = mTextPaint.measureText(s);
        float hOffset = (float) (mRadius * Math.PI / mItemCount / 2 - textWidth /2);// 水平偏移
        float vOffset = mRadius / 2 / 2.5f;// 垂直偏移
        canvas.drawTextOnPath(s, path, hOffset, vOffset, mTextPaint);

    }

    private void drawText(Canvas canvas,float tempAngle, float sweepAngle, String s) {


        Path path = new Path();

        path.addArc(mRectF,tempAngle,sweepAngle);

        float textWidth = mTextPaint.measureText(s);
        float hOffset = (float) (mRadius * Math.PI / mItemCount / 2 - textWidth / 2);// 水平偏移
        float vOffset = mRadius / 2 / 5;// 垂直偏移
        canvas.drawTextOnPath(s, path, hOffset, vOffset, mTextPaint);

    }

    /**
     * 构建每个扇形的区域
     * @return
     */
    private Region[] getArcRegion(){


        float tempAngle = mStartAngle;
        for (int i = 0 ; i < mItemCount;i++){

            Path path = new Path();
            //path.addArc(mRectF,tempAngle,60f);
            //path.arcTo(mRectF,tempAngle,60f,true);

            path.moveTo(mCircleCenter,mCircleCenter);

            path.lineTo((float)(mCircleCenter+(mRadius*Math.cos(tempAngle *Math.PI / 180)))
            ,
                    (float)(mCircleCenter+(mRadius*Math.sin(tempAngle *Math.PI / 180))));

            path.lineTo((float)(mCircleCenter+(mRadius*Math.cos((tempAngle+60f) *Math.PI / 180)))
            ,
                    (float)(mCircleCenter+(mRadius*Math.sin((tempAngle+60f) *Math.PI / 180)))  );



            path.close();
            Region region = new Region();

            RectF rectF = new RectF();
            path.computeBounds(rectF,true);
            region.setPath(path,new Region((int) rectF.left,(int)rectF.top,(int)rectF.right,(int)rectF.bottom));
            mRegion[i] = region;
            tempAngle += 60f;
            Log.d(TAG, "getArcRegion: "+tempAngle);
        }


        return mRegion;
    }

    /**
     * 构建内圆区域
     * @param CircleCenter
     * @return
     */
    private Region getRegion(float CircleCenter){
       // ,float r,float startAngle,float sweepAngle
        Path path = new Path();
        path.addCircle(CircleCenter,CircleCenter,mRadius/5, Path.Direction.CW);


        path.close();
        Region region = new Region();

        RectF rectF = new RectF();
        path.computeBounds(rectF,true);
        region.setPath(path,new Region((int) rectF.left,(int)rectF.top,(int)rectF.right,(int)rectF.bottom));
        return region;
    }

    private float mLastX;
    private float mLastY;
    private static final String TAG = "LxkCircleView";

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:


                mLastX = x;
                mLastY = y;
                mTmpAngle = 0f;

                downTime = System.currentTimeMillis();


                break;

            case MotionEvent.ACTION_MOVE:


                //获取开始的角度
                float startAngle = getAngle(mLastX, mLastY);
                //获取当前角度
                float endAngle  = getAngle(x, y);
                if (getQuadrant(x,y) == 1  || getQuadrant(x,y) == 4){

                    mStartAngle += endAngle - startAngle;

                    mTmpAngle += endAngle - startAngle;

                }else{

                    mStartAngle += startAngle - endAngle;

                    mTmpAngle += startAngle - endAngle;

                }


                /**
                 * 解决滑动冲突
                 */
                ViewParent parent = getParent();
                if (parent != null){

                    parent.requestDisallowInterceptTouchEvent(true);
                }

                invalidate();

                mLastY = y;
                mLastX = x;
                break;


            case MotionEvent.ACTION_UP:



                long upTime = System.currentTimeMillis();
                if (upTime - downTime < 200){

                    /**
                     * 内圆的点击事件
                     */
                    Region region = getRegion(mCircleCenter);

                    boolean contains = region.contains((int) x, (int) y);

                    if (contains)if (mHoleClick != null){
                            mClick = false;
                            mHoleClick.holeClick();
                        }


                    /**
                     * 色块的点击事件
                     */
                    Region[] arcRegion = getArcRegion();

                    for (int i = 0; i< arcRegion.length;i++){

                        boolean contains1 = arcRegion[i].contains((int) x, (int) y);
                        if (contains1 && mClick){
                            if (mArcClick != null) {
                                mArcClick.arcClick(i);
                                return true;
                            }

                        }else{
                            Log.d(TAG, "onTouchEvent: 不包含");
                        }

                    }

                }



                mClick = true;

                break;


        }

        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {




        return super.dispatchTouchEvent(event);

    }

    /**
     * 根据触摸的位置，计算角度
     * @param xTouch
     * @param yTouch
     * @return
     */
    private float getAngle(float xTouch,float yTouch){

        double x = xTouch - (mRadius / 2d);
        double y = yTouch - (mRadius / 2d);

        return (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
    }

    /**
     * 根据当前位置计算象限
     * @param x
     * @param y
     * @return
     */
    private int getQuadrant(float x, float y)
    {
        int tmpX = (int) (x - mRadius / 2);
        int tmpY = (int) (y - mRadius / 2);
        if (tmpX >= 0)
        {
            return tmpY >= 0 ? 4 : 1;
        } else
        {
            return tmpY >= 0 ? 3 : 2;
        }

    }

    /**
     * 内圆的点击事件
     * @param listener
     */
    public void setOnHoleCilckListener(onHoleClickListener listener){

        this.mHoleClick = listener;
    }

    public interface onHoleClickListener{


        void holeClick();
    }


    /**
     * 扇形的点击事件
     */
    public void setArcSelection(onArcSelection listener){

        this.mArcClick = listener;
    }


    public interface onArcSelection{
        void arcClick(int select);
    }
}
