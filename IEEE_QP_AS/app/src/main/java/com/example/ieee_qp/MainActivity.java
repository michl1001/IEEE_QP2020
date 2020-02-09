package com.example.ieee_qp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import devlight.io.library.ArcProgressStackView;
import devlight.io.library.ArcProgressStackView.Model;

public class MainActivity extends AppCompatActivity {

    static int MODEL_COUNT = 4;
    static int DAYS_INDEX = 0;
    static int HOURS_INDEX = 1;
    static int MINUTES_INDEX = 2;
    static int SECONDS_INDEX = 3;

    int TickCount = 0;

    int mCounter = 0;
    ArcProgressStackView mArcProgressStackView;
    CountDownTimer countDownTimer = null;

    EditText daysEditText, hoursEditText, minutesEditText, secondsEditText;

    TextView daysTextView, hoursTextView, minutesTextView, secondsTextView, dueDateTextView;

    int[] time = new int[4];

    String text;

    long durationInMillis;

    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Button btn = (Button) findViewById(R.id.button);
//        daysEditText = (EditText) findViewById(R.id.daysEditText);
//        hoursEditText = (EditText) findViewById(R.id.hoursEditText);
//        minutesEditText = (EditText) findViewById(R.id.minutesEditText);
//        secondsEditText = (EditText) findViewById(R.id.secondsEditText);

        daysTextView = (TextView) findViewById(R.id.daysText);
        hoursTextView = (TextView) findViewById(R.id.hoursText);
        minutesTextView = (TextView) findViewById(R.id.minutesText);
        secondsTextView = (TextView) findViewById(R.id.secondsText);
        dueDateTextView = (TextView) findViewById(R.id.dueDateTextView);

        mArcProgressStackView = (ArcProgressStackView) findViewById(R.id.apsv);
        mArcProgressStackView.setShadowColor(Color.argb(20, 0, 0, 0));
        mArcProgressStackView.setAnimationDuration(800);
        mArcProgressStackView.setSweepAngle(270);

        final int[] colors = new int[MODEL_COUNT];
        final int[] bgColors = new int[MODEL_COUNT];
        for (int i = 0; i < MODEL_COUNT; i++) {
            colors[i] = Color.rgb(255, 215-20*i, 0);
            bgColors[i] = Color.argb(50,132, 92, 64);
        }

        final ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
        models.add(new Model("", 0, bgColors[0], colors[0]));
        models.add(new Model("", 0, bgColors[1], colors[1]));
        models.add(new Model("", 0, bgColors[2], colors[2]));
        models.add(new Model("", 0, bgColors[3], colors[3]));
        mArcProgressStackView.setModels(models);

        thread = new Thread() {
            @Override
            public void run(){
                if (!isInterrupted()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mArcProgressStackView.getProgressAnimator().isRunning())
                                mArcProgressStackView.getProgressAnimator().cancel();
                            mArcProgressStackView.animateProgress();
                        }
                    });
                }
            }
        };

        text = "Due Time: " + TimeManager.getDueDateString();
        dueDateTextView.setText(text);

        durationInMillis = TimeManager.getDurationInMillis();

        durationUpdate();

        updateTextView();

        mArcProgressStackView.getModels().get(DAYS_INDEX).setProgress(100);
        mArcProgressStackView.getModels().get(HOURS_INDEX).setProgress(100);
        mArcProgressStackView.getModels().get(MINUTES_INDEX).setProgress(100);
        mArcProgressStackView.getModels().get(SECONDS_INDEX).setProgress(100);

        mArcProgressStackView.setInterpolator(new AccelerateDecelerateInterpolator());
        mArcProgressStackView.animateProgress();
        mArcProgressStackView.setInterpolator(new LinearInterpolator());


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                // Do something after 1s = 1000ms
                    if(durationInMillis > 0) {
                        thread.interrupt();
                        countDown();
                    }else {
                        mArcProgressStackView.getModels().get(DAYS_INDEX).setProgress(0);
                        mArcProgressStackView.getModels().get(HOURS_INDEX).setProgress(0);
                        mArcProgressStackView.getModels().get(MINUTES_INDEX).setProgress(0);
                        mArcProgressStackView.getModels().get(SECONDS_INDEX).setProgress(0);
                    }
                }
            }, 1000);

//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                int days, hours, minutes, seconds;
//
//                if(!daysEditText.getText().toString().equals(""))
//                    days = Integer.parseInt(daysEditText.getText().toString());
//                else
//                    days = 0;
//
//                if(!hoursEditText.getText().toString().equals(""))
//                    hours = Integer.parseInt(hoursEditText.getText().toString());
//                else
//                    hours = 0;
//
//                if(!minutesEditText.getText().toString().equals(""))
//                    minutes = Integer.parseInt(minutesEditText.getText().toString());
//                else
//                    minutes = 0;
//
//                if(!secondsEditText.getText().toString().equals(""))
//                    seconds = Integer.parseInt(secondsEditText.getText().toString());
//                else seconds = 0;
//
//                if (seconds > 60) {
//                    time[3] = seconds % 60;
//                    time[2] = seconds / 60;
//                }else
//                    time[3] = seconds;
//
//                if (minutes > 60) {
//                    time[2] += minutes % 60;
//                    time[1] = minutes / 60;
//                }else
//                    time[2] = minutes;
//
//                if (hours > 24) {
//                    time[1] += hours % 24;
//                    time[0] = hours / 24;
//                }else
//                    time[1] = hours;
//
//                time[0] += days;
//                totalDays = time[0];
//
//                updateTextView();
//
//                updateProgresses();
//
//                mArcProgressStackView.setInterpolator(new AccelerateDecelerateInterpolator());
//                mArcProgressStackView.animateProgress();
//                mArcProgressStackView.setInterpolator(new LinearInterpolator());
//
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        // Do something after 1s = 1000ms
//                        if(countDownTimer != null)
//                            countDownTimer.cancel();
//
//                            //countDown();
//                    }
//                }, 1000);
//
//            }
//        });

    }


    public void countDown(){
        countDownTimer = new CountDownTimer(durationInMillis+1000, 1000) {

            public void onTick(long millisUntilFinished) {
                durationUpdate();
                updateTextView();
                updateProgresses();
                if(thread.isInterrupted())
                    thread.start();
                thread.run();
            }

            public void onFinish() {
                thread.interrupt();
            }

        }.start();
    }

    public void updateProgresses() {
        if(TimeManager.totalDayCount > 0)
            mArcProgressStackView.getModels().get(0).setProgress(time[0]*100/TimeManager.totalDayCount);
        else
            mArcProgressStackView.getModels().get(0).setProgress(0);
        mArcProgressStackView.getModels().get(1).setProgress(time[1]*100/24);
        mArcProgressStackView.getModels().get(2).setProgress(time[2]*100/60);
        mArcProgressStackView.getModels().get(3).setProgress(time[3]*100/60);
    }

    public void durationUpdate() {
        durationInMillis = TimeManager.getDurationInMillis();
        long temp = durationInMillis/1000;
        time[SECONDS_INDEX] = (int)(temp%60);
        temp = temp/60;
        time[MINUTES_INDEX] = (int)(temp%60);
        temp = temp/60;
        time[HOURS_INDEX] = (int)(temp%24);
        temp = temp/24;
        time[DAYS_INDEX] = (int) temp;
    }

    public void updateTextView() {
        String daysLeft = time[0] + " Days";
        daysTextView.setText(daysLeft);

        String hoursLeft = time[1] + " Hours";
        hoursTextView.setText(hoursLeft);

        String minutesLeft = time[2] + " Minutes";
        minutesTextView.setText(minutesLeft);

        String secondsLeft = time[3] + " Seconds";
        secondsTextView.setText(secondsLeft);
    }

    public ValueAnimator animation() {
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0F, 105.0F);

        valueAnimator.setDuration(800);

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(final Animator animation) {
                animation.removeListener(this);
                animation.addListener(this);
            }
        });

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                for(int i = 0; i < MODEL_COUNT; i++) {
                    if(time[i] != 0)
                        mArcProgressStackView.getModels().get(i)
                                .setProgress((Float) animation.getAnimatedValue());
                }
                mArcProgressStackView.postInvalidate();
            }
        });

        return valueAnimator;
    }


    public void onStop(){
        countDownTimer.cancel();
        thread.interrupt();
        super.onStop();
    }

}

