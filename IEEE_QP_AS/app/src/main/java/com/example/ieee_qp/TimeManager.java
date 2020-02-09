package com.example.ieee_qp;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimeManager {

    static String taskYear;
    static String taskMonth;
    static String taskDay;
    static String taskHour;
    static String taskMinute;
    static int totalDayCount;

    public static int getMaxDays(int year, int month) {
        boolean leapYear = true;
        if (year%100 == 0 && year%400 != 0)
            leapYear = false;

        if (month == 1 || month == 3 || month == 5 | month == 7 || month == 8 || month == 10 || month == 12)
            return 31;
        else if (month == 2)
            return leapYear ? 29 : 28;
        else
            return 30;

    }

    public static long getDurationInMillis() {

        Calendar taskDate = Calendar.getInstance();
        Calendar currentDate = Calendar.getInstance();
        taskDate.set(Integer.parseInt(taskYear),Integer.parseInt(taskMonth),Integer.parseInt(taskDay),
                Integer.parseInt(taskHour),Integer.parseInt(taskMinute), 0);
        return taskDate.getTimeInMillis()-currentDate.getTimeInMillis();

    }

    public static String getDueDateString() {

        Calendar taskDate = Calendar.getInstance();
        taskDate.set(Integer.parseInt(taskYear),Integer.parseInt(taskMonth),Integer.parseInt(taskDay),
                Integer.parseInt(taskHour),Integer.parseInt(taskMinute), 0);
        return taskDate.getTime().toString();

    }
}
