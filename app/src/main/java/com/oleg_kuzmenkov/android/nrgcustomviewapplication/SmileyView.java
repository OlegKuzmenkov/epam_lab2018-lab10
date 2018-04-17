package com.oleg_kuzmenkov.android.nrgcustomviewapplication;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SmileyView extends View {

    private final String TAG = "Message";

    private boolean mIsLucky;
    private int radius;
    private int colorFace,colorEyes,colorMouth;

    private Paint paint;
    private Path mMouthPath;
    private RectF mMouthRectF;

    public SmileyView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mIsLucky = true;
        paint = new Paint();
        mMouthPath = new Path();
        mMouthRectF = new RectF();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmileyView, 0, 0);
        try {
            colorFace = typedArray.getColor(R.styleable.SmileyView_colorFace, Color.YELLOW);
            colorEyes = typedArray.getColor(R.styleable.SmileyView_colorEyes, Color.BLACK);
            colorMouth = typedArray.getColor(R.styleable.SmileyView_colorMouth, Color.BLACK);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawSmiley(canvas);
    }

    /**
     * Draw custom smiley
     */

    private void drawSmiley(Canvas canvas) {

        if(radius == 0) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                radius = getWidth() / 3;
            }
            else {
                radius = getHeight() / 3;
            }
        }

        //draw face
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(colorFace);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);

        //draw eyes
        paint.setColor(colorEyes);
        canvas.drawCircle(getWidth() / 2 - radius / 3, getHeight() / 2 - radius / 2, radius / 7, paint);
        canvas.drawCircle(getWidth() / 2 + radius / 3, getHeight() / 2 - radius / 2, radius / 7, paint);

        //draw mouth
        drawMouth(canvas);
    }

    /**
     * Draw mouth of our smiley
     */

    private void drawMouth(Canvas canvas){
        paint.setColor(colorMouth);

        if(mIsLucky == true){
            mMouthRectF.set(getWidth()/2 - radius/2,getHeight() / 2 - radius / 2,getWidth()/2 + radius/2,getHeight() / 2 + radius / 2);
            mMouthPath.arcTo(mMouthRectF, 30, 120, true);
        }
        else{
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(35);
            mMouthRectF.set(getWidth()/2 - radius/2,getHeight()/2+radius/4,getWidth()/2 + radius/2,getHeight()/2+radius);
            mMouthPath.arcTo(mMouthRectF, 210, 120, true);
        }

        canvas.drawPath(mMouthPath,paint);
        mMouthPath.reset();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState",super.onSaveInstanceState());
        bundle.putBoolean("key", mIsLucky);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle){
            Bundle bundle = (Bundle) state;
            mIsLucky = bundle.getBoolean("key");
            state = bundle.getParcelable("superState");
        }

        super.onRestoreInstanceState(state);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            Log.d(TAG, "onTouchEvent()");

            if(mIsLucky == true) {
                mIsLucky = false;
            }
            else {
                mIsLucky = true;
            }
            invalidate();
        }
        return super.onTouchEvent(event);
    }
}
