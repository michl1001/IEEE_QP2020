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
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;

import devlight.io.library.ArcProgressStackView;
import devlight.io.library.ArcProgressStackView.Model;

public class MainActivity extends AppCompatActivity {

    int MODEL_COUNT = 4;
    int mCounter = 0;
    ArcProgressStackView mArcProgressStackView;
    CircularProgressBar circularProgressBar1;
    CircularProgressBar circularProgressBar2;
    private TextView timeText;
    private TextView progressText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btn = (Button) findViewById(R.id.button);
        timeText = (TextView) findViewById(R.id.timeText);
        progressText = (TextView) findViewById(R.id.progressText);

        /*
        circularProgressBar1 = (CircularProgressBar) findViewById(R.id.circularProgressBar1);
        circularProgressBar2 = (CircularProgressBar) findViewById(R.id.circularProgressBar2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circularProgressBar1.setProgressWithAnimation(100, (long)1000);
                circularProgressBar2.setProgressWithAnimation(100, (long)1000);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        countDown();

                    }
                }, 1000);

            }
        });

         */

        mArcProgressStackView = (ArcProgressStackView) findViewById(R.id.apsv);
        mArcProgressStackView.setShadowColor(Color.argb(20, 0, 0, 0));
        mArcProgressStackView.setAnimationDuration(1000);
        mArcProgressStackView.setSweepAngle(270);

        final int[] colors = new int[MODEL_COUNT];
        final int[] bgColors = new int[MODEL_COUNT];
        for (int i = 0; i < MODEL_COUNT; i++) {
            colors[i] = Color.rgb(255, 215-20*i, 0);
            bgColors[i] = Color.argb(50,132, 92, 64);
        }

        final ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
        models.add(new Model("", 20, bgColors[0], colors[0]));
        models.add(new Model("", 20, bgColors[1], colors[1]));
        models.add(new Model("", 20, bgColors[2], colors[2]));
        models.add(new Model("", 20, bgColors[3], colors[3]));
        mArcProgressStackView.setModels(models);
        mArcProgressStackView.setInterpolator(new LinearInterpolator());



        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0F, 105.0F);
        valueAnimator.setDuration(800);
        valueAnimator.setStartDelay(200);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(MODEL_COUNT - 1);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(final Animator animation) {
                animation.removeListener(this);
                animation.addListener(this);
                mCounter = 0;

                for (final Model model : mArcProgressStackView.getModels()) model.setProgress(1);
                mArcProgressStackView.animateProgress();
            }

            @Override
            public void onAnimationRepeat(final Animator animation) {
                mCounter++;
            }
        });

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                mArcProgressStackView.getModels().get(Math.min(mCounter, MODEL_COUNT - 1))
                        .setProgress((Float) animation.getAnimatedValue());
                mArcProgressStackView.postInvalidate();
            }
        });

        mArcProgressStackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (valueAnimator.isRunning()) return;
                if (mArcProgressStackView.getProgressAnimator().isRunning()) return;
                valueAnimator.start();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //circularProgressBar1.setProgressWithAnimation(100, (long)1000);
                //circularProgressBar2.setProgressWithAnimation(100, (long)1000);

                /*
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        countDown();

                    }
                }, 1000);

                 */
                mArcProgressStackView.getModels().get(1).setProgress(100);
                countDown();
                //mArcProgressStackView.getModels().get(1).set
            }
        });

    }


    public void countDown(){
        new CountDownTimer(50000, 1000) {

            public void onTick(long millisUntilFinished) {

                //float progress1 = circularProgressBar1.getProgress();
                //float progress2 = circularProgressBar1.getProgress();

                timeText.setText(String.valueOf(millisUntilFinished/1000));
                //circularProgressBar1.setProgressWithAnimation(progress1 - 2, (long) 1000, new BounceInterpolator());
                //circularProgressBar2.setProgressWithAnimation(progress2 - 2, (long) 1000, new LinearInterpolator());

                float progress = mArcProgressStackView.getModels().get(1).getProgress();
                progressText.setText(String.valueOf(progress));
                mArcProgressStackView.getModels().get(1).setProgress(progress-1);
                //mArcProgressStackView.animateProgress();
            }

            public void onFinish() {
                /*
                float progress1 = circularProgressBar1.getProgress();
                float progress2 = circularProgressBar1.getProgress();

                progressText.setText(String.valueOf(progress1));
                timeText.setText("0");
                circularProgressBar1.setProgressWithAnimation(0, (long) 1000, new BounceInterpolator());
                circularProgressBar2.setProgressWithAnimation(0, (long) 1000, new LinearInterpolator());
                */
            }
        }.start();
    }

}

