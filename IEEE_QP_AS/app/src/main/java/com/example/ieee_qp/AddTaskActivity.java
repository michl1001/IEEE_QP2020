package com.example.ieee_qp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {


    final int TASK_YEAR = 0;
    final int TASK_MONTH = 1;
    final int TASK_DAY = 2;
    final int TASK_HOUR = 3;
    final int TASK_MINUTE = 4;

    Spinner yearSpinner, monthSpinner, daySpinner, hourSpinner, minuteSpinner;
    TextView yearsTextView, monthsTextView, daysTextView, currentTimeTextView, errorTextView;
    Button addTaskBtn;
    String text;

    Calendar myCalendar = Calendar.getInstance();

    String[] days, years, hours, minutes;

    String[] dateAndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        years = new String[10];
        hours = new String[24];
        minutes = new String[60];

        dateAndTime = new String[5];

        int currentYear = myCalendar.get(Calendar.YEAR);

        for (int i = 0; i < years.length; i++) {
            years[i]= (currentYear+i)+"";
        }

        for (int i = 0; i < hours.length; i++) {
            hours[i]= i +"";
        }

        for (int i = 0; i < minutes.length; i++) {
            if(i < 10)
                minutes[i]= "0" + i;
            else
                minutes[i] = i + "";
        }

        yearSpinner = findViewById(R.id.yearSpinner);
        monthSpinner = findViewById(R.id.monthSpinner);
        daySpinner = findViewById(R.id.daySpinner);
        hourSpinner = findViewById(R.id.hourSpinner);
        minuteSpinner = findViewById(R.id.minuteSpinner);


        yearsTextView = (TextView) findViewById(R.id.yearsTextView);
        monthsTextView = (TextView) findViewById(R.id.monthsTextView);
        daysTextView = (TextView) findViewById(R.id.daysTextView);
        currentTimeTextView = (TextView) findViewById(R.id.currentTimeTextView);
        errorTextView = (TextView) findViewById(R.id.errorTextView);

        addTaskBtn = (Button) findViewById(R.id.addTaskBtn);

        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeManager.taskYear = dateAndTime[TASK_YEAR];
                TimeManager.taskMonth = dateAndTime[TASK_MONTH];
                TimeManager.taskDay = dateAndTime[TASK_DAY];
                TimeManager.taskHour = dateAndTime[TASK_HOUR];
                TimeManager.taskMinute = dateAndTime[TASK_MINUTE];
                TimeManager.totalDayCount = (int) (TimeManager.getDurationInMillis()/(60*60*24*1000));

                if (TimeManager.getDurationInMillis() >= 30000) {
                    errorTextView.setText("");
                    Intent add = new Intent(AddTaskActivity.this, DetailsActivity.class);
                    startActivity(add);
                }else {
                    errorTextView.setText("Needs to be at least 30 seconds from now");
                }

//                Calendar taskDate = Calendar.getInstance();
//                Calendar currentDate = Calendar.getInstance();
//                taskDate.set(Integer.parseInt(dateAndTime[TASK_YEAR]),Integer.parseInt(dateAndTime[TASK_MONTH]),
//                        Integer.parseInt(dateAndTime[TASK_DAY]),
//                        Integer.parseInt(dateAndTime[TASK_HOUR]),Integer.parseInt(dateAndTime[TASK_MINUTE]),0);
//
//                yearsTextView.setText(currentDate.getTime().toString());
//                monthsTextView.setText(taskDate.getTime().toString());
//
//                text = TimeManager.getDurationInMillis()+"";
//
//                daysTextView.setText(text);


            }
        });

        setAdapaters(yearSpinner, years);
        setAdapaters(monthSpinner, getResources().getStringArray(R.array.months));
        setAdapaters(hourSpinner, hours);
        setAdapaters(minuteSpinner, minutes);

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dateAndTime[TASK_YEAR] = years[position];
                yearsTextView.setText(dateAndTime[TASK_YEAR]);

                if(dateAndTime[TASK_YEAR] != null && dateAndTime[TASK_MONTH] != null)
                    generateDaysArray();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dateAndTime[TASK_MONTH] = (position) + "";
                monthsTextView.setText(dateAndTime[TASK_MONTH]);

                if(dateAndTime[TASK_YEAR] != null && dateAndTime[TASK_MONTH] != null)
                    generateDaysArray();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        hourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dateAndTime[TASK_HOUR] = hours[position];
                if(dateAndTime[TASK_MINUTE] != null) {
                    text = dateAndTime[TASK_HOUR] + " : " + dateAndTime[TASK_MINUTE];
                    currentTimeTextView.setText(text);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        minuteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dateAndTime[TASK_MINUTE] = minutes[position];
                text = dateAndTime[TASK_HOUR] + " : " + dateAndTime[TASK_MINUTE];
                currentTimeTextView.setText(text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void generateDaysArray() {

        int maxDays = TimeManager.getMaxDays(Integer.parseInt(dateAndTime[TASK_YEAR]),
                Integer.parseInt(dateAndTime[TASK_MONTH]));

        days = new String[maxDays];

        for (int day = 0; day < maxDays; day++) {
            days[day] = ""+(day+1);
        }

        setAdapaters(daySpinner, days);

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dateAndTime[TASK_DAY] = days[position];
                daysTextView.setText(dateAndTime[TASK_DAY]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setAdapaters(Spinner spinner, String[] array) {
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(AddTaskActivity.this,
                android.R.layout.simple_spinner_item, array);

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(myAdapter);
    }
}
