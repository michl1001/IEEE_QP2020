package com.example.ieee_qp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import devlight.io.library.ArcProgressStackView;
import devlight.io.library.ArcProgressStackView.Model;

public class DetailsActivity extends AppCompatActivity {

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

    Task currentTask;

    static DetailsActivity instance;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.set_done) {
            Calendar currentTime = Calendar.getInstance();
            currentTask.taskYear = ""+currentTime.get(Calendar.YEAR);
            currentTask.taskMonth = ""+currentTime.get(Calendar.MONTH);
            currentTask.taskDay = ""+(currentTime.get(Calendar.DAY_OF_MONTH));
            currentTask.taskHour = ""+currentTime.get(Calendar.HOUR_OF_DAY);
            currentTask.taskMinute= ""+(currentTime.get(Calendar.MINUTE));
            currentTask.taskSecond= ""+(currentTime.get(Calendar.SECOND)+2);
        }else if (item.getItemId() == R.id.remove_task) {
            countDownTimer.onFinish();
            countDownTimer.cancel();
            TimeManager.taskList.remove(getIntent().getExtras().getInt("TASK_POSITION"));
            ListTestActivity listTestActivity = ListTestActivity.getInstance();
            if (listTestActivity != null)
                listTestActivity.updateList();
            finish();
        } else if (item.getItemId() == R.id.notify_one_day) {
            Calendar currentTime = Calendar.getInstance();
            currentTask.taskYear = ""+currentTime.get(Calendar.YEAR);
            currentTask.taskMonth = ""+currentTime.get(Calendar.MONTH);
            currentTask.taskDay = ""+(currentTime.get(Calendar.DAY_OF_MONTH)+1);
            currentTask.taskHour = ""+currentTime.get(Calendar.HOUR_OF_DAY);
            currentTask.taskMinute= ""+(currentTime.get(Calendar.MINUTE));
            currentTask.taskSecond= ""+(currentTime.get(Calendar.SECOND)+2);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        instance = this;

        currentTask = TimeManager.taskList.get(getIntent().getExtras().getInt("TASK_POSITION"));

        getSupportActionBar().setTitle(currentTask.taskName);

        daysTextView = (TextView) findViewById(R.id.daysText);
        hoursTextView = (TextView) findViewById(R.id.hoursText);
        minutesTextView = (TextView) findViewById(R.id.minutesText);
        secondsTextView = (TextView) findViewById(R.id.secondsText);
        dueDateTextView = (TextView) findViewById(R.id.dueDateTextView);

        ImageButton removeTaskBtn = (ImageButton) findViewById(R.id.removeTaskBtn);

        removeTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.onFinish();
                countDownTimer.cancel();
                TimeManager.taskList.remove(getIntent().getExtras().getInt("TASK_POSITION"));
                ListTestActivity listTestActivity = ListTestActivity.getInstance();
                if (listTestActivity != null)
                    listTestActivity.updateList();
                finish();
            }
        });

        mArcProgressStackView = (ArcProgressStackView) findViewById(R.id.apsv);
        mArcProgressStackView.setShadowColor(Color.argb(20, 0, 0, 0));
        mArcProgressStackView.setAnimationDuration(900);
        mArcProgressStackView.setSweepAngle(270);

        final int[] colors = new int[MODEL_COUNT];
        final int[] bgColors = new int[MODEL_COUNT];
        for (int i = 0; i < MODEL_COUNT; i++) {
            colors[i] = Color.rgb(255, 215-20*i, 0);
            bgColors[i] = Color.argb(50,132, 92, 64);
        }

        final ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
        models.add(new Model("", 100, bgColors[0], colors[0]));
        models.add(new Model("", 100, bgColors[1], colors[1]));
        models.add(new Model("", 100, bgColors[2], colors[2]));
        models.add(new Model("", 100, bgColors[3], colors[3]));
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

        text = "Due Time: " + currentTask.getDueDateString();
        dueDateTextView.setText(text);

        durationInMillis = currentTask.getDurationInMillis();

        durationUpdate();

        updateTextView();

        //mArcProgressStackView.getModels().get(DAYS_INDEX).setProgress(100);
        //mArcProgressStackView.getModels().get(HOURS_INDEX).setProgress(100);
        //mArcProgressStackView.getModels().get(MINUTES_INDEX).setProgress(100);
        //mArcProgressStackView.getModels().get(SECONDS_INDEX).setProgress(100);

        mArcProgressStackView.setInterpolator(new AccelerateDecelerateInterpolator());
        //mArcProgressStackView.animateProgress();
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
            }, 500);
    }


    public void countDown(){
        countDownTimer = new CountDownTimer(durationInMillis+1000, 1000) {

            public void onTick(long millisUntilFinished) {
                durationUpdate();
                updateTextView();
                updateProgresses();
                dueDateTextView.setText(currentTask.getDueDateString());
                if(thread.isInterrupted())
                    thread.start();
                thread.run();
            }

            public void onFinish() {
                //thread.interrupt();
            }

        }.start();
    }

    public void updateProgresses() {
        if(currentTask.totalDayCount > 0)
            mArcProgressStackView.getModels().get(0).setProgress(time[0]*100/currentTask.totalDayCount);
        else
            mArcProgressStackView.getModels().get(0).setProgress(0);
        mArcProgressStackView.getModels().get(1).setProgress(time[1]*100/24);
        mArcProgressStackView.getModels().get(2).setProgress(time[2]*100/60);
        mArcProgressStackView.getModels().get(3).setProgress(time[3]*100/60);
    }

    public void durationUpdate() {
        durationInMillis = currentTask.getDurationInMillis();
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

    public void notify(String taskName, String time) {
        Intent intent = new Intent(DetailsActivity.this, DetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(DetailsActivity.this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(DetailsActivity.this, "0")
                .setSmallIcon(R.drawable.ic_access_alarms_black_24dp)
                .setContentTitle("Reminder")
                .setContentText(taskName + " is due in " + time + ".")
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(DetailsActivity.this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());
    }

    public static DetailsActivity getInstance() {
        return instance;
    }

}

