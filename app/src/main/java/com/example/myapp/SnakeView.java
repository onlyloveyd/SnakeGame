package com.example.myapp;

import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

 public class SnakeView extends View{
    public static final String  TAG = "SnakeView";

    private int mWidth;
    private int mHeight;
    private static final int sXOffset = 0 ;
    private static final int sYOffset = 0 ;

    private final int BOXWIDTH = 30;
    private Random mRandom = new Random();
    private Point mFoodPosition;
    private boolean mIsFoodDone = true;

    private ArrayList<Point> mSnakeList;
    private Paint mSnakePaint;
    private int mSnakeDirection = 0;
    private final int UP = 1;
    private final int DOWN = 2;
    private final int LEFT = 3;
    private final int RIGHT =4;

    private Paint mBgPaint;
    private Paint mFoodPaint;


    public SnakeView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        mSnakeList = new ArrayList<Point>();
        mSnakePaint = new Paint();
        mSnakePaint.setColor(Color.RED);
        mSnakePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mSnakeList.add(new Point(500,500));
        mSnakeList.add(new Point(500,530));

        mSnakeDirection = RIGHT;
        mIsFoodDone = true;
        mFoodPosition= new Point();

        mFoodPaint = new Paint();
        mFoodPaint.setColor(Color.CYAN);
        mFoodPaint.setStyle(Paint.Style.FILL);

        mBgPaint = new Paint();
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int)(event.getX());
        int y = (int)(event.getY());
        Log.e(TAG, "x =" + x + " y = " + y + " mSnakeDirection = " + mSnakeDirection);

        Point head = mSnakeList.get(0);
        Log.e(TAG, "head.x = " + head.x + " head.y = " + head.y);
        if(mSnakeDirection == UP || mSnakeDirection == DOWN) {
            if(x < head.x) mSnakeDirection = LEFT;
            if(x > head.x) mSnakeDirection = RIGHT;
        } else if(mSnakeDirection == LEFT || mSnakeDirection == RIGHT) {
            if(y < head.y) mSnakeDirection = UP;
            if(y > head.y) mSnakeDirection= DOWN;
        }
        //Log.e(TAG, "after adjust mSnakeDirection = " + mSnakeDirection);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBg(canvas, mBgPaint);
        drawFood(canvas, mFoodPaint);
        drawSnake(canvas, mSnakePaint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    private void drawBg(Canvas canvas, Paint paint) {
        canvas.drawColor(Color.WHITE);
    }

    private void drawSnake(Canvas canvas, Paint paint) {
        for(int i = 0 ; i < mSnakeList.size() ; i++ ) {
            Point point = mSnakeList.get(i);
            Rect rect = new Rect(point.x , point.y , point.x + BOXWIDTH , point.y + BOXWIDTH);
            canvas.drawRect(rect, paint);
        }
        snakeMove(mSnakeList, mSnakeDirection);
        if(isFoodEaten()) {
            mIsFoodDone = true;
        } else {
            mSnakeList.remove(mSnakeList.size() - 1);
        }
    }

    private void drawFood(Canvas canvas, Paint paint) {
        if(mIsFoodDone) {
            mFoodPosition.x = mRandom.nextInt(mWidth - BOXWIDTH) + sXOffset ;
            mFoodPosition.y = mRandom.nextInt(mWidth - BOXWIDTH) + sYOffset ;
            mIsFoodDone = false;
        }
        Rect food = new Rect(mFoodPosition.x , mFoodPosition.y , mFoodPosition.x + BOXWIDTH , mFoodPosition.y + BOXWIDTH);
        canvas.drawRect(food, paint);

    }

    public void snakeMove(ArrayList<Point> list , int direction) {
        Point orighead = list.get(0);
        Point newhead = new Point();

        switch(direction) {
            case UP:
                newhead.x = orighead.x;
                newhead.y = orighead.y  - BOXWIDTH ;
                break;
            case DOWN:
                newhead.x = orighead.x;
                newhead.y = orighead.y  + BOXWIDTH ;
                break;
            case LEFT:
                newhead.x = orighead.x  - BOXWIDTH;
                newhead.y = orighead.y;
                break;
            case RIGHT:
                newhead.x = orighead.x + BOXWIDTH ;
                newhead.y = orighead.y;
                break;
            default:
                break;
        }
        adjustHead(newhead);
        list.add(0, newhead);
    }

    private boolean isOutBound(Point point) {
        if(point.x < sXOffset || point.x > mWidth - sXOffset) return true;
        if(point.y < sYOffset || point.y > mHeight - sYOffset) return true;
        return false;
    }

    private void adjustHead(Point point) {
        if(isOutBound(point)){
            if(mSnakeDirection == UP) point.y = mHeight - sYOffset - BOXWIDTH;
            if(mSnakeDirection == DOWN) point.y = sYOffset;
            if(mSnakeDirection == LEFT) point.x = mWidth - sYOffset - BOXWIDTH;
            if(mSnakeDirection == RIGHT) point.x = sXOffset;
        }
    }

    private boolean isFoodEaten() {
        if(!mIsFoodDone) {
            Rect foodrect = new Rect(mFoodPosition.x, mFoodPosition.y, mFoodPosition.x + BOXWIDTH, mFoodPosition.y + BOXWIDTH);
            Point head = mSnakeList.get(0);
            Rect headrect = new Rect(head.x, head.y, head.x + BOXWIDTH , head.y + BOXWIDTH);
            return foodrect.intersect(headrect);
        }
        return false;
    }

}
