package com.example.ieee_qp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<Task> {

    private Context mContext;
    int mResource;

    public ListAdapter(@NonNull Context context, int resource, ArrayList<Task> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String taskName = getItem(position).getTaskName();
        long taskDay = getItem(position).getDurationInMillis()/(1000*60*60*24);
        System.out.println("taskDay: " + taskDay);
        long taskHour = getItem(position).getDurationInMillis()/(1000*60*60)%24;
        long taskMinute = getItem(position).getDurationInMillis()/(1000*60)%60;
        long taskSeconds = getItem(position).getDurationInMillis()/(1000)%60;
        String timeRemainingMessage;
        int progressPercentage = getItem(position).getProgressPercentage();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView taskNameTextView = (TextView) convertView.findViewById(R.id.taskNameTextView);
        TextView timeLeftTextView = convertView.findViewById(R.id.timeLeftTextView);
        ProgressBar progressBar = convertView.findViewById(R.id.horizontalProgressBar);

        taskNameTextView.setText(taskName);
        if(taskDay > 0)
            timeRemainingMessage = taskDay + " days, " + taskHour + " hours";
        else
            timeRemainingMessage = taskMinute + " minutes, " + taskSeconds + " seconds";

        timeLeftTextView.setText(timeRemainingMessage);

        progressBar.setProgress(progressPercentage);

        return convertView;
    }
}
