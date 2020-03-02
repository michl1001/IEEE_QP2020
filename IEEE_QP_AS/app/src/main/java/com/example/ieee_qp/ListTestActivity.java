package com.example.ieee_qp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.sql.Time;
import java.util.ArrayList;

public class ListTestActivity extends AppCompatActivity {

    private ListAdapter listAdapter;
    private ArrayList<Task> taskList;
    private ListView progressListView;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_test);

        progressListView = (ListView) findViewById(R.id.progressListView);

        if(TimeManager.taskList == null) {
            taskList = new ArrayList<>();
            TimeManager.taskList = taskList;
        } else
            taskList = TimeManager.taskList;

        listAdapter = new ListAdapter(ListTestActivity.this, R.layout.progress_list_layout, taskList);
        progressListView.setAdapter(listAdapter);

        progressListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent showDetails = new Intent(ListTestActivity.this, DetailsActivity.class);
                showDetails.putExtra("TASK_POSITION", position);
                startActivity(showDetails);
            }
        });

        Button addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addTask = new Intent(getApplicationContext(), AddTaskActivity.class);
                startActivity(addTask);
            }
        });

        countDownTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                listAdapter = new ListAdapter(ListTestActivity.this, R.layout.progress_list_layout, taskList);
                progressListView.setAdapter(listAdapter);
                for(int i = 0; i < taskList.size(); i++) {
                    if (taskList.get(i).getDurationInMillis() <= 0) {
                        taskList.remove(i);
                    }
                }
                listAdapter = new ListAdapter(ListTestActivity.this, R.layout.progress_list_layout, taskList);
                progressListView.setAdapter(listAdapter);
            }

            public void onFinish() {
                countDownTimer.start();
            }

        }.start();
    }
}
