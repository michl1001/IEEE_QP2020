package com.example.ieee_qp;

import java.util.Calendar;

public class Task {
    String taskName;
    String taskYear;
    String taskMonth;
    String taskDay;
    String taskHour;
    String taskMinute;
    long fullDurationInMillis;
    int totalDayCount;

    public Task(String taskName, String taskYear, String taskMonth, String taskDay, String taskHour, String taskMinute, int totalDayCount) {
        this.taskName = taskName;
        this.taskYear = taskYear;
        this.taskMonth = taskMonth;
        this.taskDay = taskDay;
        this.taskHour = taskHour;
        this.taskMinute = taskMinute;
        this.totalDayCount = totalDayCount;
        fullDurationInMillis = getDurationInMillis();
    }

    public long getDurationInMillis() {
        Calendar taskDate = Calendar.getInstance();
        Calendar currentDate = Calendar.getInstance();
        taskDate.set(Integer.parseInt(taskYear),Integer.parseInt(taskMonth),Integer.parseInt(taskDay),
                Integer.parseInt(taskHour),Integer.parseInt(taskMinute), 0);
        return taskDate.getTimeInMillis()-currentDate.getTimeInMillis();
    }

    public String getDueDateString() {
        Calendar taskDate = Calendar.getInstance();
        taskDate.set(Integer.parseInt(taskYear),Integer.parseInt(taskMonth),Integer.parseInt(taskDay),
                Integer.parseInt(taskHour),Integer.parseInt(taskMinute), 0);
        return taskDate.getTime().toString();
    }

    public int getProgressPercentage() {
        return (int)(getDurationInMillis()*100/fullDurationInMillis);
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskYear() {
        return taskYear;
    }

    public String getTaskMonth() {
        return taskMonth;
    }

    public String getTaskDay() {
        return taskDay;
    }

    public String getTaskHour() {
        return taskHour;
    }

    public String getTaskMinute() {
        return taskMinute;
    }

    public int getTotalDayCount() {
        return totalDayCount;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskYear(String taskYear) {
        this.taskYear = taskYear;
    }

    public void setTaskMonth(String taskMonth) {
        this.taskMonth = taskMonth;
    }

    public void setTaskDay(String taskDay) {
        this.taskDay = taskDay;
    }

    public void setTaskHour(String taskHour) {
        this.taskHour = taskHour;
    }

    public void setTaskMinute(String taskMinute) {
        this.taskMinute = taskMinute;
    }

    public void setTotalDayCount(int totalDayCount) {
        this.totalDayCount = totalDayCount;
    }
}
