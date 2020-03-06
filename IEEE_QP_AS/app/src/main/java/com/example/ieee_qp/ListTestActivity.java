package com.example.ieee_qp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
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
                    long duration = taskList.get(i).getDurationInMillis();
                    if(duration/1000 == 24*60*60)
                        ListTestActivity.this.notify(taskList.get(i).taskName, "1 day");
                    else if (duration/1000 == 60*60)
                        ListTestActivity.this.notify(taskList.get(i).taskName,"1 hour");
                    else if (duration/1000 == 60*30)
                        ListTestActivity.this.notify(taskList.get(i).taskName,"30 minutes");
                    else if (taskList.get(i).getDurationInMillis() <= 0) {
                        alert(i);
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

    public void notify(String taskName, String time) {
        //Intent intent = new Intent(ListTestActivity.this, ListTestActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //PendingIntent pendingIntent = PendingIntent.getActivity(ListTestActivity.this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ListTestActivity.this, "0")
                .setSmallIcon(R.drawable.ic_access_alarms_black_24dp)
                .setContentTitle("Reminder")
                .setContentText(taskName + " is due in " + time + ".")
                //.setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ListTestActivity.this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());
        notificationManager.cancelAll();
    }

    public void alert(int index){
        Context context;
        DetailsActivity detailsActivity = DetailsActivity.getInstance();

        if (detailsActivity != null && detailsActivity.currentTask == taskList.get(index)) {
            detailsActivity.finish();
            context = ListTestActivity.this;
        }else {
            context = detailsActivity;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setCancelable(false);
        builder.setTitle("Task Due!");
        String message = taskList.get(index).taskName + " is due now.";
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}
