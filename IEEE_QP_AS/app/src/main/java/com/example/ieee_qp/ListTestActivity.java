package com.example.ieee_qp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
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
    static ListTestActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_test);
        instance = this;

        getSupportActionBar().setTitle("All Tasks");
        progressListView = (ListView) findViewById(R.id.progressListView);

        if(TimeManager.taskList == null) {
            taskList = new ArrayList<>();
            TimeManager.taskList = taskList;
        } else
            taskList = TimeManager.taskList;

        updateList();

        progressListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent showDetails = new Intent(ListTestActivity.this, DetailsActivity.class);
                showDetails.putExtra("TASK_POSITION", position);
                startActivity(showDetails);
            }
        });

        countDownTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                for(int i = 0; i < taskList.size(); i++) {
                    if (taskList.get(i).getDurationInMillis() <= 0) {
                        taskList.remove(i);
                    }
                }
                updateList();
            }

            public void onFinish() {
                countDownTimer.start();
            }

        }.start();
    }

    public void updateList() {
        listAdapter = new ListAdapter(ListTestActivity.this, R.layout.progress_list_layout, taskList);
        progressListView.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_task) {
            Intent addTask = new Intent(getApplicationContext(), AddTaskActivity.class);
            startActivity(addTask);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static ListTestActivity getInstance() {
        return instance;
    }
}
