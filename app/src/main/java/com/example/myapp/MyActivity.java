package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


public class MyActivity extends Activity {

    private SnakeView mSnakeView;
    private static final int REFRESH = 1;
    private static final int REFRESHINTERVAL = 300;
    private boolean isPaused = false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if(msg.arg1 == REFRESH) {
                if(mSnakeView != null) {
                    mSnakeView.invalidate();
                }
            }
        }

    };

    private Thread mRefreshThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        mSnakeView = (SnakeView) findViewById(R.id.snake_view);
        isPaused = false;

        mRefreshThread = new Thread("TimerThread") {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                super.run();
                while (!isPaused) {
                    Message msg = mHandler.obtainMessage();
                    msg.arg1 = REFRESH;
                    mHandler.sendMessage(msg);
                    try {
                        Thread.sleep(REFRESHINTERVAL);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        };
        mRefreshThread.start();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        isPaused = true;
    }


    @SuppressWarnings("deprecation")
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        isPaused = true;
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        isPaused = false;
        if(!mRefreshThread.isAlive()) {
            mRefreshThread.start();
        }
    }

}
